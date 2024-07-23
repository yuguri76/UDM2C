package com.example.livealone.product.dto;

import lombok.Getter;

@Getter
public class ProductRequestDto {
  private String name;
  private int price;
  private long quantity;
  private String introduction;
}
