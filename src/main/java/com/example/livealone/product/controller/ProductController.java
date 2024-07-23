package com.example.livealone.product.controller;

import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.product.dto.ProductRequestDto;
import com.example.livealone.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  @PostMapping()
  public ResponseEntity<CommonResponseDto> createProduct(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @RequestBody ProductRequestDto requestDto) {
    /**
     * 더미 유저 만들어 테스트
     * 로그인 가능 시, 주석된 코드로 수정 예정
     */
    CommonResponseDto responseDto = productService.createProduct(productService.exampleCreateUser(), requestDto);
//    CommonResponseDto responseDto = productService.createProduct(userDetails.getUser(), requestDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }
}
