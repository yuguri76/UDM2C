package com.example.livealone.broadcast.service;

import com.example.livealone.broadcast.dto.BroadcastRequestDto;
import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.entity.BroadcastCode;
import com.example.livealone.broadcast.entity.BroadcastStatus;
import com.example.livealone.broadcast.mapper.BroadcastMapper;
import com.example.livealone.broadcast.repository.BroadcastCodeRepository;
import com.example.livealone.broadcast.repository.BroadcastRepository;
import com.example.livealone.global.exception.CustomException;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.repository.ProductRepository;
import com.example.livealone.user.entity.Social;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BroadcastService {

  private final BroadcastRepository broadcastRepository;
  private final BroadcastCodeRepository broadcastCodeRepository;
  private final ProductRepository productRepository;
  private final MessageSource messageSource;

  private static final int BROADCAST_BEFORE_STARTING = 10;
  private static final int BROADCAST_AFTER_STARTING = 60;
  private final UserRepository userRepository;

  public void createBroadcast(BroadcastRequestDto boardRequestDto/*, User user*/) {

    BroadcastCode code = broadcastCodeRepository.findByCode(boardRequestDto.getCode()).orElseThrow(
        () -> new CustomException(messageSource.getMessage(
            "broadcast.code.not.found",
            null,
            CustomException.DEFAULT_ERROR_MESSAGE,
            Locale.getDefault()
        ), HttpStatus.NOT_FOUND)
    );

    if(!isWithinBroadcastTime(code.getAirTime(), LocalDateTime.now())) {
      throw new CustomException(messageSource.getMessage(
          "not.air.time",
          null,
          CustomException.DEFAULT_ERROR_MESSAGE,
          Locale.getDefault()
      ), HttpStatus.FORBIDDEN);
    }

//    Product product = productRepository.findById(boardRequestDto.getProductId()).orElseThrow(
//        () -> new CustomException(messageSource.getMessage(
//          "product.not.found",
//          null,
//          CustomException.DEFAULT_ERROR_MESSAGE,
//          Locale.getDefault()
//        ), HttpStatus.NOT_FOUND)
//    );

    // 더미 유저 등록 입니다. 삭제 예정.
    User user = User.builder()
        .username("홍길동")
        .email("test@gmail.com")
        .social(Social.NAVER)
        .build();
    userRepository.save(user);

    // 더미 상품 등록 입니다. 삭제 예정.
    Product product = Product.builder()
        .name("이름")
        .introduction("설명")
        .price(10000)
        .quantity(99L)
        .seller(user)
        .build();
    productRepository.save(product);

    Broadcast broadcast = BroadcastMapper.toBroadcast(boardRequestDto.getTitle(), user, product, code);
    broadcastRepository.save(broadcast);

  }

  @Transactional(readOnly = true)
  public BroadcastResponseDto inquiryCurrentBroadcast() {
    Broadcast broadcast = broadcastRepository.findByBroadcastStatus(BroadcastStatus.ONAIR).orElseThrow(() ->
      new CustomException(messageSource.getMessage(
        "broadcast.not.found",
        null,
        CustomException.DEFAULT_ERROR_MESSAGE,
        Locale.getDefault()
    ), HttpStatus.NOT_FOUND)
    );

    BroadcastResponseDto responseDto = BroadcastMapper.toBroadcastResponseDto(broadcast);
    return responseDto;
  }

  private boolean isWithinBroadcastTime(LocalDateTime airTime, LocalDateTime now) {

    return now.isAfter(airTime.minusMinutes(BROADCAST_BEFORE_STARTING)) &&
        now.isBefore(airTime.plusMinutes(BROADCAST_AFTER_STARTING));

  }

}
