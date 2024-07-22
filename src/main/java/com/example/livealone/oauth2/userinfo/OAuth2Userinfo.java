package com.example.livealone.oauth2.userinfo;

import java.util.Map;

public interface OAuth2Userinfo {

	String getEmailFromAttributes(Map<String, Object> attributes);

	String getNameFromAttributes(Map<String, Object> attributes);

}
