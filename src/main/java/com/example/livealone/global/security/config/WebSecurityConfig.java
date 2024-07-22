package com.example.livealone.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtService jwtService;
	private final UserDetailsServiceImpl userDetailsService;
	private final AuthenticationEntryPointImpl authenticationEntryPointImpl;

	@Bean
	public JwtAuthentication jwtAuthenticationFilter() throws Exception {
		return new JwtAuthentication(jwtService,userDetailsService);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();

	}

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

		http.csrf(AbstractHttpConfigurer::disable);
		http.formLogin(AbstractHttpConfigurer::disable);

		http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(request ->
			request
				.requestMatchers("/**").permitAll()
				.anyRequest().authenticated());


		http.exceptionHandling(e -> e
			.authenticationEntryPoint(authenticationEntryPointImpl));

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}
}
