package com.example.livealone.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentInfoDto {
	private Long paymentId;
	private int amount;
	private String status;
	private String paymentMethod;
	private String createdAt;
	private String productName;
	private int quantity;
}
