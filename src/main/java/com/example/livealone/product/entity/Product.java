package com.example.livealone.product.entity;

import com.example.livealone.global.entity.Timestamp;
import com.example.livealone.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="products")
@NoArgsConstructor
public class Product extends Timestamp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private Long quantity;

	@Column(nullable = false)
	private String introduction;

	@ManyToOne
	@JoinColumn(name = "seller_id", nullable = false)
	private User seller;

	@Builder
	public Product(String name, int price, Long quantity, String introduction, User seller) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.introduction = introduction;
		this.seller = seller;
	}

	public void setQuantity(int i) {
	}

	public void setIntroduction(String s) {
	}

	public void setPrice(int i) {
	}

	public void setName(String s) {
	}
}
