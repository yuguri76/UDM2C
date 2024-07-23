package com.example.livealone.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductRequestDto {
  @NotBlank(message = "상품 이름을 입력해주세요.")
  @Size(max = 50, message = "이름은 최대 50글자까지 입력할 수 있습니다.")
  private String name;

  @NotNull(message = "상품 가격을 입력해주세요.")
  @Min(0)
  private Integer price;

  @NotNull(message = "상품 수량을 입력해주세요.")
  @Min(0)
  private Long quantity;

  @NotBlank(message = "상품 소개 글을 입력해주세요.")
  @Size(max = 255, message = "소개글은 최대 255글자까지 입력할 수 있습니다.")
  private String introduction;
}
