package com.example.livealone.broadcast.service;

import com.example.livealone.broadcast.dto.BroadcastRequestDto;
import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import com.example.livealone.broadcast.dto.UserBroadcastResponseDto;
import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.entity.BroadcastCode;
import com.example.livealone.broadcast.entity.BroadcastStatus;
import com.example.livealone.broadcast.mapper.BroadcastMapper;
import com.example.livealone.broadcast.repository.BroadcastCodeRepository;
import com.example.livealone.broadcast.repository.BroadcastRepository;
import com.example.livealone.global.exception.CustomException;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.repository.ProductRepository;
import com.example.livealone.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BroadcastService {

  private final BroadcastRepository broadcastRepository;
  private final BroadcastCodeRepository broadcastCodeRepository;
  private final ProductRepository productRepository;

  private final MessageSource messageSource;

  private static final int BROADCAST_AFTER_STARTING = 60;

  private static final int PAGE_SIZE = 5;

  public void createBroadcast(BroadcastRequestDto boardRequestDto, User user) {

    BroadcastCode code = broadcastCodeRepository.findByCode(boardRequestDto.getCode()).orElseThrow(
        () -> new CustomException(messageSource.getMessage(
            "broadcast.code.not.found",
            null,
            CustomException.DEFAULT_ERROR_MESSAGE,
            Locale.getDefault()
        ), HttpStatus.NOT_FOUND)
    );

    isWithinBroadcastTime(code.getAirTime(), LocalDateTime.now());

    Product product = productRepository.findById(boardRequestDto.getProductId()).orElseThrow(
        () -> new CustomException(messageSource.getMessage(
          "product.not.found",
          null,
          CustomException.DEFAULT_ERROR_MESSAGE,
          Locale.getDefault()
        ), HttpStatus.NOT_FOUND)
    );

    Optional<Broadcast> optionalBroadcast = broadcastRepository.findByBroadcastCode(code);

    broadcastRepository.save(optionalBroadcast.isPresent() ?
        optionalBroadcast.get().updateBroadcast(boardRequestDto.getTitle(), user, product) :
        BroadcastMapper.toBroadcast(boardRequestDto.getTitle(), user, product, code));

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

    return BroadcastMapper.toBroadcastResponseDto(broadcast);
  }

  public void closeBroadcast(User user) {
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
  }

  private void isWithinBroadcastTime(LocalDateTime airTime, LocalDateTime now) {
    if(now.isAfter(airTime.plusMinutes(BROADCAST_AFTER_STARTING)) || now.isBefore(airTime)) {
      throw new CustomException(messageSource.getMessage(
          "not.air.time",
          null,
          CustomException.DEFAULT_ERROR_MESSAGE,
          Locale.getDefault()
      ), HttpStatus.FORBIDDEN);
    }
  }

  /**
   * 매 정각마다 방송을 중단하는 스케쥴러 입니다.
   */
  @Scheduled(cron = "0 0 * * * *")
  public void forceCloseBroadcast() {
    broadcastRepository.findByBroadcastStatus(BroadcastStatus.ONAIR)
        .ifPresent(broadcast -> broadcastRepository.save(broadcast.closeBroadcast()));
  }

}
