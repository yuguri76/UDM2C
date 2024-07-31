package com.example.livealone.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponseDto {
    private final Long orderId;

    @Builder
    public OrderResponseDto(Long orderId) {
        this.orderId = orderId;
    }
}
