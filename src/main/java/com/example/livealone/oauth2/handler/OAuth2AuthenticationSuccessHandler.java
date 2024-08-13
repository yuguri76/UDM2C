package com.example.livealone.oauth2.handler;

import com.example.livealone.global.config.URIConfig;
import com.example.livealone.global.security.JwtService;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtService jwtService;
	private final AuthService authService;

	private final URIConfig uriConfig;

	@Value("${spring.security.oauth2.redirect.url}")
	private String url;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

		User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

		response.sendRedirect(UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("access", jwtService.generateToken(user))
				.queryParam("refresh", authService.reissueRefreshToken(user))
				.build()
				.toUriString()
		);
	}

}
