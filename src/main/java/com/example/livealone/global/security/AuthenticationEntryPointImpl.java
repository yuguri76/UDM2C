package com.example.livealone.global.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.livealone.global.exception.ExceptionResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
		throws IOException {
		log.info("인증 예외 처리");

		boolean jwtError = request.getAttribute("error") != null;

		String error = jwtError ? request.getAttribute("error").toString() : HttpStatus.UNAUTHORIZED.getReasonPhrase();
		String message = jwtError ? error : "인증되지 않은 사용자입니다.";

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(
			ExceptionResponseDto.builder()
				.statusCode(HttpStatus.UNAUTHORIZED)
				.message(message)
				.path(request.getRequestURI())
				.build().toString()
		);
	}
}
