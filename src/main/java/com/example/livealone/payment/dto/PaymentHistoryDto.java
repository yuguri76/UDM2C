package com.example.livealone.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PaymentHistoryDto {
	private int currentPage;
	private int totalPages;
	private long totalElements;
	private List<PaymentInfoDto> contents;
}