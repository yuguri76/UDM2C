package com.example.livealone.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentHistoryDto {
	private Long paymentId;
	private int amount;
	private String status;
	private String paymentMethod;
	private String createdAt;
}