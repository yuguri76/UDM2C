package com.example.livealone.reservation.entity;

import com.example.livealone.user.entity.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "reservations")
public class Reservations extends Timestamp {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String code;

	@Column(name = "air_time", nullable = false, unique = true)
	private LocalDateTime airTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User streamer;

	@Builder
	public Reservations(String code, LocalDateTime airTime, User streamer) {
		this.code = code;
		this.airTime = airTime;
		this.streamer = streamer;
	}

}
