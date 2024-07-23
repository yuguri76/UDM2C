package com.example.livealone.payment.entity;

import com.example.livealone.global.entity.Timestamp;
import com.example.livealone.order.entity.Order;
import com.example.livealone.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
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
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@Column(nullable = false)
	private int amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentMethod paymentMethod;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;

	@Column(name = "tid")
	private String tid;

	@Builder
	public Payment(User user, Order order, int amount, PaymentMethod paymentMethod, PaymentStatus status, String tid) {
		this.user = user;
		this.order = order;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.status = status;
		this.tid = tid;
	}

	public void updateStatus(PaymentStatus status) {
		this.status = status;
	}
}
