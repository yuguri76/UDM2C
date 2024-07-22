package com.example.livealone.oauth2.handler;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.example.livealone.global.security.JwtService;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.user.entity.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Value("${jwt.refresh-expire-time}")
	private int EXPIRE_TIME;

	private final JwtService jwtService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

		String accessToken = jwtService.generateToken(user.getEmail());
		String refreshToken = UUID.randomUUID().toString();

		response.setHeader("Authorization", accessToken);
		Cookie cookie = new Cookie("refresh", refreshToken);
		cookie.setMaxAge(EXPIRE_TIME);
		response.addCookie(cookie);

	}

}
