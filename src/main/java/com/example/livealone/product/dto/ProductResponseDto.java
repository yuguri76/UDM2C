package com.example.livealone.product.dto;

import com.example.livealone.product.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponseDto {
  private Long id;
  private String name;
  private int price;
  private long quantity;
  private String introduction;

  public ProductResponseDto(Product product) {
    this.id = product.getId();
    this.name = product.getName();
    this.price = product.getPrice();
    this.quantity = product.getQuantity();
    this.introduction = product.getIntroduction();
  }
}
