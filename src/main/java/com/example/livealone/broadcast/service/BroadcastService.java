package com.example.livealone.broadcast.service;

import static com.example.livealone.global.entity.SocketMessageType.BROADCAST;
import static com.example.livealone.global.entity.SocketMessageType.ERROR;

import com.example.livealone.admin.dto.AdminBroadcastListResponseDto;
import com.example.livealone.admin.mapper.AdminMapper;
import com.example.livealone.alert.service.AlertService;
import com.example.livealone.broadcast.dto.BroadcastRequestDto;
import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import com.example.livealone.broadcast.dto.BroadcastTitleResponseDto;
import com.example.livealone.broadcast.dto.CreateBroadcastResponseDto;
import com.example.livealone.broadcast.dto.StreamKeyResponseDto;
import com.example.livealone.broadcast.dto.UserBroadcastResponseDto;
import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.entity.BroadcastStatus;
import com.example.livealone.product.dto.ProductResponseDto;
import com.example.livealone.product.service.ProductService;
import com.example.livealone.reservation.entity.Reservations;
import com.example.livealone.broadcast.mapper.BroadcastMapper;
import com.example.livealone.broadcast.repository.BroadcastRepository;
import com.example.livealone.global.dto.SocketMessageDto;
import com.example.livealone.global.exception.CustomException;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.repository.ProductRepository;
import com.example.livealone.reservation.service.ReservationService;
import com.example.livealone.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RTransaction;
import org.redisson.api.RedissonClient;
import org.redisson.api.TransactionOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BroadcastService {

  private final ReservationService reservationService;
  private final AlertService alertService;

  private final BroadcastRepository broadcastRepository;
  private final ProductRepository productRepository;

  private final ObjectMapper objectMapper;
  private final MessageSource messageSource;
  private final RedissonClient redissonClient;
  private final SimpMessagingTemplate messagingTemplate;

  private static final int PAGE_SIZE = 5;
  public static final String REDIS_ONAIR_BROADCAST_KEY = "OnAirBroadcast";

  @Value("${default.stream-key}")
  private String DEFAULT_STREAM_KEY;


  public CreateBroadcastResponseDto createBroadcast(BroadcastRequestDto boardRequestDto, User user)
      throws JsonProcessingException {
    RBucket<BroadcastResponseDto> bucket = redissonClient.getBucket(REDIS_ONAIR_BROADCAST_KEY);

    Reservations reservations = reservationService.findReservation(user);

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

    BroadcastResponseDto redis = BroadcastMapper.toBroadcastResponseDto(saveBroadcast, saveBroadcast.getProduct());
    bucket.set(redis, 1, TimeUnit.HOURS);

    sendStreamKey(
        BroadcastMapper.toStreamKeyResponseDto(true, broadcast.getReservation().getCode()));

    alertService.sendBroadcastStartAlert(BroadcastMapper.toBroadcastTitleResponseDto(saveBroadcast));

    return BroadcastMapper.toCreateBroadcastResponseDto(saveBroadcast);
  }

  public List<UserBroadcastResponseDto> getBroadcast(int page, User user) {
    return broadcastRepository.findAllByUserId(user.getId(), page, PAGE_SIZE);
  }

  @Transactional(readOnly = true)
  public BroadcastResponseDto inquiryCurrentBroadcast() {
    RBucket<BroadcastResponseDto> bucket = redissonClient.getBucket(REDIS_ONAIR_BROADCAST_KEY);
    if (bucket.isExists()) {
      return bucket.get();
    }

    Broadcast broadcast = broadcastRepository.findByBroadcastStatus(BroadcastStatus.ONAIR)
        .orElseThrow(() ->
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

    RTransaction redisTransaction = redissonClient.createTransaction(TransactionOptions.defaults());

    try {
      Broadcast broadcast = broadcastRepository.findByBroadcastStatus(BroadcastStatus.ONAIR)
          .orElseThrow(() ->
              new CustomException(messageSource.getMessage(
                  "no.exit.current.broadcast",
                  null,
                  CustomException.DEFAULT_ERROR_MESSAGE,
                  Locale.getDefault()
              ), HttpStatus.NOT_FOUND)
          );

      RBucket<BroadcastResponseDto> broadcastBucket = redissonClient.getBucket(REDIS_ONAIR_BROADCAST_KEY);
      RBucket<ProductResponseDto> productBucket = redissonClient.getBucket(ProductService.REDIS_PRODUCT_KEY + broadcast.getProduct().getId());

      if (!Objects.equals(broadcast.getStreamer().getId(), user.getId())) {
        throw new CustomException(messageSource.getMessage(
            "user.not.match",
            null,
            CustomException.DEFAULT_ERROR_MESSAGE,
            Locale.getDefault()
        ), HttpStatus.FORBIDDEN);
      }

      broadcastRepository.save(broadcast.closeBroadcast());

      deleteCache(broadcastBucket);
      deleteCache(productBucket);

      redisTransaction.commit();

      sendStreamKey(BroadcastMapper.toStreamKeyResponseDto(false, ""));
    } catch (Exception error) {
      redisTransaction.rollback();
      throw error;
    }
  }

  /**
   * 매 정각마다 방송을 중단하고 스트림 키를 보내는 스케쥴러 입니다. 유저 테스트 용으로 0, 20, 40분에 실행 되도록 하였습니다.
   */
  @Scheduled(cron = "0 0,20,40 * * * *")
  public void forceCloseBroadcast() throws JsonProcessingException {
    RTransaction redisTransaction = redissonClient.createTransaction(TransactionOptions.defaults());

    try {
      RBucket<BroadcastResponseDto> broadcastBucket = redissonClient.getBucket(REDIS_ONAIR_BROADCAST_KEY);
      if(!broadcastBucket.isExists())
        return;
      RBucket<Product> productBucket = redissonClient.getBucket(ProductService.REDIS_PRODUCT_KEY + broadcastBucket.get().getProductId());

      broadcastRepository.findByBroadcastStatus(BroadcastStatus.ONAIR)
          .ifPresent(broadcast -> broadcastRepository.save(broadcast.closeBroadcast()));

      deleteCache(broadcastBucket);
      deleteCache(productBucket);

      redisTransaction.commit();

      sendStreamKey(BroadcastMapper.toStreamKeyResponseDto(false, ""));
    } catch (Exception error) {
      redisTransaction.rollback();
      throw error;
    }

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

  public String requestStreamKey() throws JsonProcessingException {
    try {
      String messageJSON = objectMapper.writeValueAsString(getStreamKey());
      SocketMessageDto socketMessageDto = new SocketMessageDto(BROADCAST, "back-server", messageJSON);

      return objectMapper.writeValueAsString(socketMessageDto);
    } catch (Exception e) {
      log.error(e.getMessage());
      SocketMessageDto socketMessageDto = new SocketMessageDto(ERROR,"back-server",e.getMessage());
      return objectMapper.writeValueAsString(socketMessageDto);
    }
  }

  private StreamKeyResponseDto getStreamKey() {
    Optional<Broadcast> broadcast = broadcastRepository.findByBroadcastStatus(
        BroadcastStatus.ONAIR);

    if (broadcast.isPresent()) {
      return BroadcastMapper.toStreamKeyResponseDto(true,
          broadcast.get().getReservation().getCode());
    } else {
      return BroadcastMapper.toStreamKeyResponseDto(false, DEFAULT_STREAM_KEY);
    }
  }

  protected void sendStreamKey(StreamKeyResponseDto responseDto) throws JsonProcessingException {
    String messageJSON = objectMapper.writeValueAsString(responseDto);
    SocketMessageDto socketMessageDto = new SocketMessageDto(BROADCAST, "server", messageJSON);

    messagingTemplate.convertAndSend("/queue/message",socketMessageDto);
  }

  public BroadcastTitleResponseDto getBroadcastTitle(Long broadcastId) {
    Broadcast broadcast = broadcastRepository.findById(broadcastId).orElseThrow(() ->
        new CustomException(messageSource.getMessage(
            "broadcast.not.found",
            null,
            CustomException.DEFAULT_ERROR_MESSAGE,
            Locale.getDefault()
        ), HttpStatus.FORBIDDEN)
    );

    return BroadcastMapper.toBroadcastTitleResponseDto(broadcast);
  }

  public Page<AdminBroadcastListResponseDto> getAllBroadcastListPageable(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Broadcast> broadcastPage = broadcastRepository.findAll(pageable);

    List<AdminBroadcastListResponseDto> adminBroadcastListResponseDtoList = broadcastPage.stream()
        .map(AdminMapper::toAdminBroadcastListResponseDto)
        .collect(Collectors.toList());

    return new PageImpl<>(adminBroadcastListResponseDtoList, pageable,
        broadcastPage.getTotalElements());
  }

  private void deleteCache(RBucket<?> bucket) {
    if (bucket.isExists()) {
      bucket.delete();
    }
  }
}