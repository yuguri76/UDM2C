package com.example.livealone.delivery.dto;

import com.example.livealone.order.entity.OrderStatus;
import lombok.Getter;

@Getter
public class DeliveryHistoryResponseDto {

    private OrderStatus orderStatus;
    private String productName;
    private String address;

}
