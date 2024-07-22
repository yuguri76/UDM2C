package com.example.livealone.global.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.livealone.user.entity.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails, OAuth2User {

	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of();

	}

	@Override
	public Map<String, Object> getAttributes() {

		return Map.of();

	}

	@Override
	public String getPassword() {

		return "";

	}

	@Override
	public String getUsername() {

		return user.getEmail();

	}

	@Override
	public String getName() {

		return user.getUsername();

	}

}
