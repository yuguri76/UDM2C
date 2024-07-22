package com.example.livealone.order.entity;

import com.example.livealone.broadcast.entity.Broadcast;
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
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "broadcasts")
public class Order extends Timestamp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "broadcast_id", nullable = false)
	private Broadcast broadcast;


	@Builder
	public Order(int quantity, OrderStatus orderStatus, User user, Product product, Broadcast broadcast) {
		this.quantity = quantity;
		this.orderStatus = orderStatus;
		this.user = user;
		this.product = product;
		this.broadcast = broadcast;
	}
}
