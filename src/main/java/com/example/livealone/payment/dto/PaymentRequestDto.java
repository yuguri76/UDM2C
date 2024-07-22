package com.example.livealone.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentRequestDto {
	private Long userId;
	private Long orderId;
	private int amount;
	private String paymentMethod;
}
