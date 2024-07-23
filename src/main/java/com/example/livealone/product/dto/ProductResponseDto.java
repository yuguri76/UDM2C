package com.example.livealone.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponseDto {
  private Long id;
  private String name;
  private Integer price;
  private Long quantity;
  private String introduction;

  @Builder
  public ProductResponseDto(Long id, String name, Integer price, Long quantity, String introduction) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.introduction = introduction;
  }
}
