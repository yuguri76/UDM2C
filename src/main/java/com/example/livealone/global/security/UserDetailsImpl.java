package com.example.livealone.global.security;

import com.example.livealone.user.entity.User;
import com.example.livealone.user.entity.UserRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails, OAuth2User {

	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		UserRole role = user.getRole();
		String authority = role.getAuthority();

		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(simpleGrantedAuthority);

		return authorities;
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
