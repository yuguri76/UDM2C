package com.example.livealone.payment.entity;

import com.example.livealone.global.entity.Timestamp;
import com.example.livealone.order.entity.Order;
import com.example.livealone.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name="payments")
@NoArgsConstructor
@Getter
public class Payment extends Timestamp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToOne
	@JoinColumn(name = "order_id",nullable = false)
	private Order order;

	@Column(nullable = false)
	private int amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentMethod payment_method;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStauts status;

	@Builder
	public Payment(User user, Order order, int amount, PaymentMethod payment_method, PaymentStauts status) {
		this.user = user;
		this.order = order;
		this.amount = amount;
		this.payment_method = payment_method;
		this.status = status;
	}
}
