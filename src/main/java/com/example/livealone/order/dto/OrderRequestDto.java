package com.example.livealone.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OrderRequestDto {

    @NotBlank(message = "주문 수량을 입력해주세요.")
    private int quantity;
}
