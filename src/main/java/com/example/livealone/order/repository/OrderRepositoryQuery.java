package com.example.livealone.order.repository;

import com.example.livealone.order.entity.Order;
import com.example.livealone.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepositoryQuery {
  Long sumQuantityByBroadcastId(Long broadcastId);

    Order findCurrentOrderByUserAndProduct(User user, Long productId);
}
