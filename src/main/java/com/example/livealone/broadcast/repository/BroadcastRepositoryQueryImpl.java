package com.example.livealone.broadcast.repository;

import static com.example.livealone.broadcast.entity.QBroadcast.broadcast;
import static com.example.livealone.order.entity.QOrder.order;

import com.example.livealone.broadcast.dto.QUserBroadcastResponseDto;
import com.example.livealone.broadcast.dto.UserBroadcastResponseDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BroadcastRepositoryQueryImpl implements BroadcastRepositoryQuery {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<UserBroadcastResponseDto> findAllByUserId(Long userId, int page, int size) {

    Pageable pageable = PageRequest.of(page,size);

    List<UserBroadcastResponseDto> fetch = queryFactory.select(new QUserBroadcastResponseDto(
          broadcast.title,
          broadcast.broadcastStatus,
          broadcast.product.name,
          broadcast.reservation.airTime,
          broadcast.product.price.multiply(
              JPAExpressions.select(order.quantity.sum().coalesce(0).as("quentity"))
                  .from(order)
                  .where(order.broadcast.id.eq(broadcast.id))
          )
        ))
        .from(broadcast)
        .where(broadcast.streamer.id.eq(userId))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(new OrderSpecifier<>(Order.DESC, broadcast.id))
        .fetch();

    Long count = queryFactory
        .select(broadcast.count())
        .from(broadcast)
        .where(broadcast.streamer.id.eq(userId))
        .fetchOne();

    return new PageImpl<>(fetch, pageable, count);

  }

}
