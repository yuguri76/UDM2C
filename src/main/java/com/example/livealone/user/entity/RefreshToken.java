package com.example.livealone.user.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "refresh_token", timeToLive = 120)
public class RefreshToken {

	@Id
	private String token;
	private Long userId;

	@Builder
	public RefreshToken(Long userId, String token) {
		this.userId = userId;
		this.token = token;
	}

}
