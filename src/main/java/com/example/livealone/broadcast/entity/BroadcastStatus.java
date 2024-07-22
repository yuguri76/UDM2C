package com.example.livealone.broadcast.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BroadcastStatus {
	ONAIR("on_air"),
	CLOSE("close");

	private final String status;
}
