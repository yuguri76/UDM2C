package com.example.livealone.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionResponseDto {
	HttpStatus statusCode;
	String message;
	String path;
}
