package com.example.livealone.oauth2.handler;

import com.example.livealone.global.security.JwtService;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.user.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Value("${jwt.refresh-expire-time}")
	private int EXPIRE_TIME;

	private final JwtService jwtService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

		User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

		String accessToken = jwtService.generateToken(user);
		String refreshToken = UUID.randomUUID().toString();

		response.setHeader("Authorization", accessToken);
		Cookie cookie = new Cookie("refresh", refreshToken);
		cookie.setMaxAge(EXPIRE_TIME);
		response.addCookie(cookie);

		String redirectUrl = "http://localhost:3000/oauth2/redirect?token=" + accessToken;
		response.sendRedirect(redirectUrl);

	}

}
