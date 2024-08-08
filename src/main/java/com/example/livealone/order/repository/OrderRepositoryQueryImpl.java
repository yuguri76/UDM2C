package com.example.livealone.order.repository;

import com.example.livealone.order.entity.QOrder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryQueryImpl implements OrderRepositoryQuery {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Long sumQuantityByBroadcastId(Long broadcastId) {
    QOrder order = QOrder.order;

    Integer sum = jpaQueryFactory.select(order.quantity.sum())
        .from(order)
        .where(order.broadcast.id.eq(broadcastId))
        .fetchOne();

    if (sum == null) {
      sum = 0;
    }

    return (long) sum;
  }
}
