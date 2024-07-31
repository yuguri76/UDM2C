package com.example.livealone;

import com.example.livealone.product.entity.Product;
import com.example.livealone.order.entity.Order;
import com.example.livealone.user.entity.User;
import com.example.livealone.product.repository.ProductRepository;
import com.example.livealone.order.repository.OrderRepository;
import com.example.livealone.user.repository.UserRepository;
import com.example.livealone.user.entity.Social;
import com.example.livealone.order.entity.OrderStatus;
import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.entity.BroadcastCode;
import com.example.livealone.broadcast.entity.BroadcastStatus;
import com.example.livealone.broadcast.repository.BroadcastRepository;
import com.example.livealone.broadcast.repository.BroadcastCodeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BroadcastRepository broadcastRepository;

	@Autowired
	private BroadcastCodeRepository broadcastCodeRepository;

	@Override
	public void run(String... args) throws Exception {
		// 유저 생성
		User seller = User.builder()
			.username("seller")
			.nickname("BestSeller")
			.email("seller@example.com")
			.social(Social.GOOGLE)
			.birthDay(LocalDate.of(1990, 1, 1))
			.address("123 Seller St.")
			.build();
		userRepository.save(seller);

		User buyer = User.builder()
			.username("buyer")
			.nickname("BestBuyer")
			.email("buyer@example.com")
			.social(Social.GOOGLE)
			.birthDay(LocalDate.of(1995, 1, 1))
			.address("456 Buyer St.")
			.build();
		userRepository.save(buyer);

		// 더미 Product 데이터 추가
		Product product1 = Product.builder()
			.name("Product 1")
			.price(10000)
			.quantity(2L)
			.introduction("This is product 1")
			.seller(seller)
			.build();
		productRepository.save(product1);

//		Product product2 = Product.builder()
//			.name("Product 2")
//			.price(20000)
//			.quantity(50L)
//			.introduction("This is product 2")
//			.seller(seller)
//			.build();
//		productRepository.save(product2);

		// BroadcastCode 생성
		BroadcastCode broadcastCode = BroadcastCode.builder()
			.airTime(LocalDateTime.now())
			.code("CODE123")
			.build();
		broadcastCodeRepository.save(broadcastCode);

		// Broadcast 생성
		Broadcast broadcast = Broadcast.builder()
			.title("Sample Broadcast")
			.broadcastStatus(BroadcastStatus.ONAIR)
			.streamer(seller)
			.product(product1)
			.broadcastCode(broadcastCode)
			.build();
		broadcastRepository.save(broadcast);

		// 더미 Order 데이터 추가
//		Order order1 = Order.builder()
//			.quantity(1)
//			.orderStatus(OrderStatus.READY)
//			.user(buyer)
//			.product(product1)
//			.broadcast(broadcast)
//			.build();
//		orderRepository.save(order1);
//		System.out.println("Order 1 ID: " + order1.getId());
//
//		Order order2 = Order.builder()
//			.quantity(2)
//			.orderStatus(OrderStatus.READY)
//			.user(buyer)
//			.product(product2)
//			.broadcast(broadcast)
//			.build();
//		orderRepository.save(order2);
//		System.out.println("Order 2 ID: " + order2.getId());
	}
}