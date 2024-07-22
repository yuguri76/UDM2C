package com.example.livealone.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
	READY("ready"),
	SHIPPING("shipping"),
	COMPLETED("completed");

	private final String status;
}