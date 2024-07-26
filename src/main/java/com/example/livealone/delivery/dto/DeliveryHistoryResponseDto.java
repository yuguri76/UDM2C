package com.example.livealone.delivery.dto;

import com.example.livealone.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryHistoryResponseDto {

    private OrderStatus orderStatus;
    private String productName;
    private String address;

}
