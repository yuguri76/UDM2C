package com.example.livealone.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ExceptionResponseDto> CustomExceptionHandler(HttpServletRequest request, CustomException e) {

		ExceptionResponseDto responseDto = ExceptionResponseDto.builder()
			.message(e.getMessage())
			.statusCode(e.getStatusCode())
			.path(request.getRequestURI())
			.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
	}
}
