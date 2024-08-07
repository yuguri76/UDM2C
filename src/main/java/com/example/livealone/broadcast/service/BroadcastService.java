package com.example.livealone.broadcast.service;

import static com.example.livealone.global.entity.SocketMessageType.BROADCAST;

import com.example.livealone.broadcast.dto.BroadcastRequestDto;
import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import com.example.livealone.broadcast.dto.CreateBroadcastResponseDto;
import com.example.livealone.broadcast.dto.ReservationRequestDto;
import com.example.livealone.broadcast.dto.ReservationResponseDto;
import com.example.livealone.broadcast.dto.StreamKeyResponseDto;
import com.example.livealone.broadcast.dto.UserBroadcastResponseDto;
import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.entity.BroadcastStatus;
import com.example.livealone.broadcast.entity.Reservations;
import com.example.livealone.broadcast.mapper.BroadcastMapper;
import com.example.livealone.broadcast.mapper.ReservationMapper;
import com.example.livealone.broadcast.repository.BroadcastRepository;
import com.example.livealone.broadcast.repository.ReservationRepository;
import com.example.livealone.global.aop.DistributedLock;
import com.example.livealone.global.dto.SocketMessageDto;
import com.example.livealone.global.exception.CustomException;
import com.example.livealone.global.handler.WebSocketHandler;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.repository.ProductRepository;
import com.example.livealone.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
public class BroadcastService {

  private final BroadcastRepository broadcastRepository;
  private final ReservationRepository reservationRepository;
  private final ProductRepository productRepository;

  private final ObjectMapper objectMapper;
  private final MessageSource messageSource;

  private static final int BROADCAST_AFTER_STARTING = 20;

  private static final int PAGE_SIZE = 5;

  @Value("${default.stream-key}")
  private String DEFAULT_STREAM_KEY;


  public CreateBroadcastResponseDto createBroadcast(BroadcastRequestDto boardRequestDto, User user)
      throws JsonProcessingException {

    LocalDateTime now = ZonedDateTime.now().toLocalDateTime();

    Reservations reservations = reservationRepository
        .findByAirTimeBetweenAndStreamer(now.minusMinutes(BROADCAST_AFTER_STARTING), now, user)
        .orElseThrow(
            () -> new CustomException(messageSource.getMessage(
                "reservation.not.found",
                null,
                CustomException.DEFAULT_ERROR_MESSAGE,
                Locale.getDefault()
            ), HttpStatus.NOT_FOUND)
        );

    Product product = productRepository.findById(boardRequestDto.getProductId()).orElseThrow(
        () -> new CustomException(messageSource.getMessage(
          "product.not.found",
          null,
          CustomException.DEFAULT_ERROR_MESSAGE,
          Locale.getDefault()
        ), HttpStatus.NOT_FOUND)
    );

    Optional<Broadcast> optionalBroadcast = broadcastRepository.findByReservation(reservations);

    Broadcast broadcast = optionalBroadcast.isPresent() ?
        optionalBroadcast.get().updateBroadcast(boardRequestDto.getTitle(), user, product) :
        BroadcastMapper.toBroadcast(boardRequestDto.getTitle(), user, product, reservations);

    Broadcast saveBroadcast = broadcastRepository.save(broadcast);

    sendStreamKey(BroadcastMapper.toStreamKeyResponseDto(true, broadcast.getReservation().getCode()));

    return BroadcastMapper.toCreateBroadcastResponseDto(saveBroadcast);
  }

  public List<UserBroadcastResponseDto> getBroadcast(int page, User user) {
    return broadcastRepository.findAllByUserId(user.getId(), page, PAGE_SIZE);
  }

  @Transactional(readOnly = true)
  public BroadcastResponseDto inquiryCurrentBroadcast() {
    Broadcast broadcast = broadcastRepository.findByBroadcastStatus(BroadcastStatus.ONAIR).orElseThrow(() ->
        new CustomException(messageSource.getMessage(
            "no.exit.current.broadcast",
            null,
            CustomException.DEFAULT_ERROR_MESSAGE,
            Locale.getDefault()
        ), HttpStatus.NOT_FOUND)
    );

    Product product = broadcast.getProduct();

    return BroadcastMapper.toBroadcastResponseDto(broadcast, product);
  }

  public void closeBroadcast(User user) throws JsonProcessingException {
    Broadcast broadcast = broadcastRepository.findByBroadcastStatus(BroadcastStatus.ONAIR).orElseThrow(() ->
        new CustomException(messageSource.getMessage(
            "no.exit.current.broadcast",
            null,
            CustomException.DEFAULT_ERROR_MESSAGE,
            Locale.getDefault()
        ), HttpStatus.NOT_FOUND)
    );

    if(!Objects.equals(broadcast.getStreamer().getId(), user.getId())) {
      throw new CustomException(messageSource.getMessage(
          "user.not.match",
          null,
          CustomException.DEFAULT_ERROR_MESSAGE,
          Locale.getDefault()
      ), HttpStatus.FORBIDDEN);
    }

    broadcastRepository.save(broadcast.closeBroadcast());

    sendStreamKey(BroadcastMapper.toStreamKeyResponseDto(false, ""));
  }

  /**
   * 매 정각마다 방송을 중단하고 스트림 키를 보내는 스케쥴러 입니다.
   * 유저 테스트 용으로 0, 20, 40분에 실행 되도록 하였습니다.
   */
  @Scheduled(cron = "0 0,20,40 * * * *")
  public void forceCloseBroadcast() throws JsonProcessingException {
    broadcastRepository.findByBroadcastStatus(BroadcastStatus.ONAIR)
        .ifPresent(broadcast -> broadcastRepository.save(broadcast.closeBroadcast()));

    sendStreamKey(BroadcastMapper.toStreamKeyResponseDto(false, ""));
  }

  public Broadcast findByBroadcastId(Long broadcastId) {

    return broadcastRepository.findById(broadcastId).orElseThrow(
            () -> new CustomException(messageSource.getMessage(
                    "broadcast.not.found",
                    null,
                    CustomException.DEFAULT_ERROR_MESSAGE,
                    Locale.getDefault()
            ), HttpStatus.NOT_FOUND)
    );

  }

  public Broadcast saveBroadcast(Broadcast broadcast) {

    return broadcastRepository.save(broadcast);
  }

  public void requestStreamKey(WebSocketSession session) {
    try{
      String messageJSON = objectMapper.writeValueAsString(getStreamKey());
      SocketMessageDto socketMessageDto = new SocketMessageDto(BROADCAST, "server", messageJSON);

      String result = objectMapper.writeValueAsString(socketMessageDto);
      TextMessage text = new TextMessage(result);
      session.sendMessage(text);
    }catch (IOException ignored){
    }
  }

  @DistributedLock(key = "'createReservation-' + #user.getId()")
  public ReservationResponseDto createReservation(ReservationRequestDto requestDto, User user) {
    if(reservationRepository.findByAirTime(requestDto.getAirtime()).isPresent()) {
      throw new CustomException(messageSource.getMessage(
          "already.occupied.reservation",
          null,
          CustomException.DEFAULT_ERROR_MESSAGE,
          Locale.getDefault()
      ), HttpStatus.FORBIDDEN);
    }

    return ReservationMapper.toReservationResponseCodeDto(reservationRepository
        .save(ReservationMapper.toReservation(requestDto, user)));
  }

  private StreamKeyResponseDto getStreamKey() {
    Optional<Broadcast> broadcast = broadcastRepository.findByBroadcastStatus(BroadcastStatus.ONAIR);

    if(broadcast.isPresent()) {
      return BroadcastMapper.toStreamKeyResponseDto(true, broadcast.get().getReservation().getCode());
    } else {
      return BroadcastMapper.toStreamKeyResponseDto(false, DEFAULT_STREAM_KEY);
    }
  }

  protected void sendStreamKey(StreamKeyResponseDto responseDto) throws JsonProcessingException {
    String messageJSON = objectMapper.writeValueAsString(responseDto);
    SocketMessageDto socketMessageDto = new SocketMessageDto(BROADCAST, "server", messageJSON);

    String result = objectMapper.writeValueAsString(socketMessageDto);
    TextMessage text = new TextMessage(result);
    WebSocketHandler.broadcast(text);
  }
  
  public Page<AdminBroadcastListResponseDto> getAllBroadcastListPageable(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Broadcast> broadcastPage = broadcastRepository.findAll(pageable);

    List<AdminBroadcastListResponseDto> adminBroadcastListResponseDtoList = broadcastPage.stream()
        .map(broadcast -> BroadcastMapper.toAdminBroadcastListResponseDto(broadcast))
        .collect(Collectors.toList());

    return new PageImpl<>(adminBroadcastListResponseDtoList, pageable, broadcastPage.getTotalElements());
  }
}
