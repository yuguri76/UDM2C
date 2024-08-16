package com.example.livealone.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PaymentHistoryDto {
	private Long paymentId;
	private int amount;
	private String status;
	private String paymentMethod;
	private String createdAt;
	private int currentPage;
	private int totalPages;
	private long totalElements;
	private List<PaymentHistoryDto> content;
	private String productName;
	private int quantity;
}