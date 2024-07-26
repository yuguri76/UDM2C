package com.example.livealone.oauth2.userinfo;

import java.util.Map;

public class KakaoOAuth2Userinfo implements OAuth2Userinfo {


	@Override
	public String getEmailFromAttributes(Map<String, Object> attributes) {

		return (String)((Map<String, Object>) attributes.get("kakao_account")).get("email");

	}

	@Override
	public String getNameFromAttributes(Map<String, Object> attributes) {

		return (String)((Map<String, Object>) attributes.get("properties")).get("nickname");

	}

}
