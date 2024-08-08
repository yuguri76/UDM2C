package com.example.livealone.order.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepositoryQuery {
  Long sumQuantityByBroadcastId(Long broadcastId);
}
