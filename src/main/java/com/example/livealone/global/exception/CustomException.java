package com.example.livealone.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

	public static final String DEFAULT_ERROR_MESSAGE = "에러가 발생했습니다.";
	private HttpStatus statusCode;

	public CustomException(String message, HttpStatus statusCode){
		super(message);
		this.statusCode = statusCode;
	}
}
