package com.example.livealone.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponseDto {
	private String status;
	private String message;
	private Long paymentId;
	private Long userId;
	private Long orderId;
	private int amount;
	private String paymentMethod;
	private String createdAt;
	private String updateAt;
}
