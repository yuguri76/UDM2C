package com.example.livealone.order.repository;

import com.example.livealone.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.livealone.order.entity.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long>, OrderRepositoryQuery {
    Optional<Order> findByUser(User user);
}
