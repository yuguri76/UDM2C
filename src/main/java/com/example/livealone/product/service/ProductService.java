package com.example.livealone.product.service;

import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.product.dto.ProductRequestDto;
import com.example.livealone.product.dto.ProductResponseDto;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.repository.ProductRepository;
import com.example.livealone.user.entity.Social;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional
  public CommonResponseDto createProduct(User user, ProductRequestDto requestDto) {
    Product newProduct = new Product(requestDto);
    newProduct.addSeller(user);

    Product saveProduct = productRepository.save(newProduct);
    ProductResponseDto productResponseDto = new ProductResponseDto(saveProduct);
    CommonResponseDto responseDto = new CommonResponseDto(HttpStatus.CREATED.value(), "성공적으로 상품이 등록되었습니다.", productResponseDto);

    return responseDto;
  }

  /**
   * 더미 유저 생성 코드
   * 단지 로그인 구현 전 테스트를 위한 것
   * 로그인 테스트 가능할 시 제거 예정
   * @return
   */
  private final UserRepository userRepository;

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
