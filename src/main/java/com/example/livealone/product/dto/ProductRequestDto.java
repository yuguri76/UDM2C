package com.example.livealone.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductRequestDto {
  @NotBlank(message = "상품 이름을 입력해주세요.")
  private String name;

  @NotNull(message = "상품 가격을 입력해주세요.")
  @Min(0)
  private Integer price;

  @NotNull(message = "상품 수량을 입력해주세요.")
  @Min(0)
  private Long quantity;

  @NotBlank(message = "상품 소개 글을 입력해주세요.")
  private String introduction;
}
