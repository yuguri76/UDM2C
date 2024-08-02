package com.example.livealone.product.controller;

import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.product.dto.ProductRequestDto;
import com.example.livealone.product.dto.ProductResponseDto;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.mapper.ProductMapper;
import com.example.livealone.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<CommonResponseDto<ProductResponseDto>> createProduct(@AuthenticationPrincipal UserDetailsImpl userPrincipal,
                                                          @Valid @RequestBody ProductRequestDto requestDto) {
    ProductResponseDto productResponseDto = productService.createProduct(userPrincipal.getUser(), requestDto);
    CommonResponseDto<ProductResponseDto> responseDto = new CommonResponseDto(HttpStatus.CREATED.value(), "성공적으로 상품이 등록되었습니다.", productResponseDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<CommonResponseDto<ProductResponseDto>> getProduct(@PathVariable Long productId) {
    Product product = productService.findByProductId(productId);
    ProductResponseDto productResponseDto = ProductMapper.toProductResponseDto(product);
    CommonResponseDto<ProductResponseDto> responseDto = new CommonResponseDto<>(HttpStatus.OK.value(), "상품 정보 조회 성공", productResponseDto);

    return ResponseEntity.ok(responseDto);
  }

}
