package com.example.livealone.order.mapper;

import com.example.livealone.order.dto.OrderQuantityResponseDto;
import com.example.livealone.product.entity.Product;
import lombok.Getter;

@Getter
public class OrderMapper {

  public static OrderQuantityResponseDto toOrderQuantityResponseDto(Product product) {
    return OrderQuantityResponseDto.builder()
        .quantity(product.getQuantity())
        .build();
  }
}
