package com.example.livealone.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.livealone.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
