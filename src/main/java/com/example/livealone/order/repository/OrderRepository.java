package com.example.livealone.order.repository;

import com.example.livealone.order.entity.Order;
import com.example.livealone.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long>, OrderRepositoryQuery {
    Optional<Order> findByUser(User user);
}
