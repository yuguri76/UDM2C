package com.example.livealone.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Social {

	KAKAO("kakao"),
	NAVER("naver"),
	GOOGLE("google");

	private final String value;
	public static Social fromValue(String value) {
		for (Social social : values()) {
			if (social.value.equals(value)) {
				return social;
			}
		}
    return null;
  }

}
