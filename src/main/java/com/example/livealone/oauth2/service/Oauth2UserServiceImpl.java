package com.example.livealone.oauth2.service;

import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.oauth2.userinfo.GoogleOAuth2Userinfo;
import com.example.livealone.oauth2.userinfo.KakaoOAuth2Userinfo;
import com.example.livealone.oauth2.userinfo.NaverOAuth2Userinfo;
import com.example.livealone.oauth2.userinfo.OAuth2Userinfo;
import com.example.livealone.user.entity.Social;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2UserServiceImpl extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		String provider = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Userinfo userInfo = getOAuth2User(userRequest.getClientRegistration().getRegistrationId());

		String email = userInfo.getEmailFromAttributes(oAuth2User.getAttributes());
		String name = userInfo.getNameFromAttributes(oAuth2User.getAttributes());

		Optional<User> optionalUser = userRepository.findByEmail(email);
		User user;

		if (optionalUser.isEmpty()) {
			user = User.builder()
				.username(name)
				.nickname(name)
				.email(email)
				.social(Social.fromValue(provider))
				.build();
			userRepository.save(user);
		}
		else {
			user = optionalUser.get();
		}
		return setAuthentication(user);

	}

	private OAuth2Userinfo getOAuth2User(String provider) {
		return switch (provider) {
			case "google" -> new GoogleOAuth2Userinfo();
			case "naver" -> new NaverOAuth2Userinfo();
			case "kakao" -> new KakaoOAuth2Userinfo();
			default -> throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
		};
	}

	private UserDetailsImpl setAuthentication(User user) {

		UserDetailsImpl userDetails = new UserDetailsImpl(user);
		Authentication authentication =
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

		return userDetails;

	}

}
