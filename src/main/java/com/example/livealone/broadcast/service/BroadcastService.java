package com.example.livealone.broadcast.service;

import com.example.livealone.broadcast.dto.BroadcastRequestDto;
import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.entity.BroadcastCode;
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
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BroadcastService {

  private final BroadcastRepository broadcastRepository;
  private final BroadcastCodeRepository broadcastCodeRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  private final MessageSource messageSource;

  private static final int BROADCAST_BEFORE_STARTING = 10;
  private static final int BROADCAST_AFTER_STARTING = 60;

  private static final int PAGE_SIZE = 10;

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

  public List<BroadcastResponseDto> getBroadcast(int page/*, User user*/) {

    // 현재 user 를 가져올 수 없어 일단 임의로 user id를 입력하였습니다. 이후 변경 예정
    return broadcastRepository.findAllByUserId(1L, page, PAGE_SIZE);

  }

  private boolean isWithinBroadcastTime(LocalDateTime airTime, LocalDateTime now) {

    return now.isAfter(airTime.minusMinutes(BROADCAST_BEFORE_STARTING)) &&
        now.isBefore(airTime.plusMinutes(BROADCAST_AFTER_STARTING));

  }

}
