package com.example.livealone.payment.repository;

import com.example.livealone.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByUserId(Long userId);
	Payment findByOrder_Id(Long orderId);

	boolean existsByTid(String tid);

	Payment findByTid(String paymentKey);
}
