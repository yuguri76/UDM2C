package com.example.livealone.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonResponseDto<T> {

	private final Integer statusCode;
	private final String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	@Builder
	public CommonResponseDto(Integer status, String message, T data) {
		this.statusCode = status;
		this.message = message;
		this.data = data;
	}
}
