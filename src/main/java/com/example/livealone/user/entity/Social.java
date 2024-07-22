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

}
