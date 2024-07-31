package com.example.livealone.user.entity;

import org.springframework.data.annotation.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "refresh_token", timeToLive = 3600)
public class RefreshToken {

	@Id
	private Long userId;

	@Indexed
	private String token;

	@Builder
	public RefreshToken(Long userId, String token) {
		this.userId = userId;
		this.token = token;
	}

}
