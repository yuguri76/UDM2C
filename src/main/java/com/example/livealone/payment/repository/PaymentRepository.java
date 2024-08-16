package com.example.livealone.payment.repository;

import com.example.livealone.payment.entity.Payment;
import com.example.livealone.payment.entity.PaymentStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	Page<Payment> findByUserIdAndStatus(Long userId, PaymentStatus status, Pageable pageable);

	Payment findByOrder_Id(Long orderId);

	boolean existsByTid(String tid);

}
