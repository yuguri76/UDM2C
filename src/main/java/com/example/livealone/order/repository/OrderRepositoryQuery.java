package com.example.livealone.order.repository;

import com.example.livealone.admin.dto.AdminConsumerResponseDto;
import com.example.livealone.order.entity.Order;
import com.example.livealone.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepositoryQuery {
  Long sumQuantityByBroadcastId(Long broadcastId);

  Order findCurrentOrderByUserAndProduct(User user, Long productId);

  Page<AdminConsumerResponseDto> findAllByBroadcastId(Long broadcastId, int page, int size);
}
