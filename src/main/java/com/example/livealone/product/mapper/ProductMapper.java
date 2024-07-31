package com.example.livealone.product.mapper;

import com.example.livealone.product.dto.ProductRequestDto;
import com.example.livealone.product.dto.ProductResponseDto;
import com.example.livealone.product.entity.Product;
import com.example.livealone.user.entity.User;

public class ProductMapper {
  public static Product toProduct(ProductRequestDto requestDto, User seller) {
    return Product.builder()
        .name(requestDto.getName())
        .price(requestDto.getPrice())
        .quantity(requestDto.getQuantity())
        .introduction(requestDto.getIntroduction())
        .seller(seller)
        .build();
  }

  public static ProductResponseDto toProductResponseDto(Product product) {
    return ProductResponseDto.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .quantity(product.getQuantity())
        .introduction(product.getIntroduction())
        .build();
  }
}
