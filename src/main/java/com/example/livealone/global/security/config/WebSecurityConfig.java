package com.example.livealone.global.security.config;

import com.example.livealone.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.livealone.global.security.AuthenticationEntryPointImpl;
import com.example.livealone.global.security.JwtService;
import com.example.livealone.global.security.UserDetailsServiceImpl;
import com.example.livealone.global.security.filter.JwtAuthentication;

import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtService jwtService;
	private final UserDetailsServiceImpl userDetailsService;
	private final AuthenticationEntryPointImpl authenticationEntryPointImpl;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Bean
	public JwtAuthentication jwtAuthenticationFilter() {
		return new JwtAuthentication(jwtService,userDetailsService);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();

	}

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {

		http.csrf(AbstractHttpConfigurer::disable);
		http.formLogin(AbstractHttpConfigurer::disable);

		http.cors(cors -> cors.configurationSource(corsConfigurationSource));

		http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// http.authorizeHttpRequests(request ->
		// 	request
		// 		.requestMatchers("/ws").permitAll()
		// 		.requestMatchers("/auth/kakao/login").permitAll()
		// 		.requestMatchers("/auth/naver/login").permitAll()
		// 		.requestMatchers("/auth/google/login").permitAll()
		// 		.requestMatchers("/auth/reissue").permitAll()
		// 		.requestMatchers( HttpMethod.GET,"/broadcast").permitAll()
		// 		.requestMatchers( HttpMethod.GET,"/product/**").permitAll()
		// 		.anyRequest().authenticated());

		http.authorizeHttpRequests(request ->
			request
				.anyRequest().permitAll() // 모든 요청에 대해 인증을 요구하지 않음
		);

		http.exceptionHandling(e -> e
			.authenticationEntryPoint(authenticationEntryPointImpl));

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		http.oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
				.successHandler(oAuth2AuthenticationSuccessHandler));

		return http.build();

	}
}
