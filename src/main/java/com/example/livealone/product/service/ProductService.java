package com.example.livealone.product.service;

import com.example.livealone.global.exception.CustomException;
import com.example.livealone.product.dto.ProductRequestDto;
import com.example.livealone.product.dto.ProductResponseDto;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.mapper.ProductMapper;
import com.example.livealone.product.repository.ProductRepository;
import com.example.livealone.user.entity.Social;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.repository.UserRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final MessageSource messageSource;

  @Transactional
  public ProductResponseDto createProduct(User user, ProductRequestDto requestDto) {

    Product newProduct = ProductMapper.toProduct(requestDto, user);

    Product saveProduct = productRepository.save(newProduct);
    ProductResponseDto responseDto = ProductMapper.toProductResponseDto(saveProduct);

    return responseDto;
  }

  @Transactional(readOnly = true)
  public ProductResponseDto inquiryProduct(Long productId) {

    Product product = productRepository.findById(productId).orElseThrow(() ->
        new CustomException(messageSource.getMessage(
        "product.not.found",
        null,
        CustomException.DEFAULT_ERROR_MESSAGE,
        Locale.getDefault()
    ), HttpStatus.NOT_FOUND)
    );

    ProductResponseDto responseDto = ProductMapper.toProductResponseDto(product);

    return responseDto;
  }

  /**
   * 더미 유저 생성 코드
   * 단지 로그인 구현 전 테스트를 위한 것
   * 로그인 테스트 가능할 시 제거 예정
   * @return
   */
  private final UserRepository userRepository;

  @Transactional
  public User exampleCreateUser() {

    User user = User.builder()
            .username("testUser")
            .nickname("testNick")
            .email("test@email.com")
            .social(Social.KAKAO)
            .build();

    return userRepository.save(user);
  }

}
