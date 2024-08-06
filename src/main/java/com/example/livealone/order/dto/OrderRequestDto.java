package com.example.livealone.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OrderRequestDto {

    @NotBlank(message = "주문 수량을 입력해주세요.")
    @Min(value = 1, message = "최소 주문 가능 수량은 1개입니다.")
    private int quantity;
}
