package com.example.livealone.oauth2.userinfo;

import java.util.Map;

public class GoogleOAuth2Userinfo implements OAuth2Userinfo {

	@Override
	public String getEmailFromAttributes(Map<String, Object> attributes) {

		return (String) attributes.get("email");

	}

	@Override
	public String getNameFromAttributes(Map<String, Object> attributes) {

		return (String) attributes.get("name");

	}

}
