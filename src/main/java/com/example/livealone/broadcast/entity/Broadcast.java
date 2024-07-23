package com.example.livealone.broadcast.entity;

import java.time.LocalDateTime;

import com.example.livealone.global.entity.Timestamp;
import com.example.livealone.product.entity.Product;
import com.example.livealone.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "broadcasts")
public class Broadcast extends Timestamp {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Enumerated(EnumType.STRING)
	private BroadcastStatus broadcastStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User streamer;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "broadcast_code_id", referencedColumnName = "air_time", nullable = false, unique = true)
	private BroadcastCode broadcastCode;

	@Builder
	public Broadcast(String title, BroadcastStatus broadcastStatus, User streamer, Product product, BroadcastCode broadcastCode) {
		this.title = title;
		this.broadcastStatus = broadcastStatus;
		this.streamer = streamer;
		this.product = product;
		this.broadcastCode = broadcastCode;
	}

	public Broadcast closeBroadcast() {
		this.broadcastStatus = BroadcastStatus.CLOSE;
		return this;
	}

}
