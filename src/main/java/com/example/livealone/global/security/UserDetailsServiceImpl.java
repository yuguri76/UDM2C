package com.example.livealone.global.security;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.livealone.global.exception.CustomException;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	private final MessageSource messageSource;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(messageSource.getMessage(
				"user.not.found",
				null,
				CustomException.DEFAULT_ERROR_MESSAGE,
				Locale.getDefault()
			), HttpStatus.NOT_FOUND));

		return new UserDetailsImpl(user);

	}

}
