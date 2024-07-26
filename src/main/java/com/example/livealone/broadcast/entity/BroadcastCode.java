package com.example.livealone.broadcast.entity;

import java.time.LocalDateTime;

import com.example.livealone.global.entity.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "broadcast_codes")
public class BroadcastCode extends Timestamp {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String code;

	@Column(name = "air_time", nullable = false, unique = true)
	private LocalDateTime airTime;

	@Builder
	public BroadcastCode(String code, LocalDateTime airTime) {
		this.code = code;
		this.airTime = airTime;
	}

	public BroadcastCode(LocalDateTime now) {

	}
}
