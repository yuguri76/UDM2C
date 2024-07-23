package com.example.livealone.broadcast.repository;

import static com.example.livealone.broadcast.entity.QBroadcast.broadcast;

import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import com.example.livealone.broadcast.dto.QBroadcastResponseDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BroadcastRepositoryQueryImpl implements BroadcastRepositoryQuery {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<BroadcastResponseDto> findAllByUserId(Long userId, int page, int size) {

    return queryFactory.select(new QBroadcastResponseDto(
          broadcast.title,
          broadcast.broadcastStatus,
          broadcast.product.name,
          broadcast.broadcastCode.airTime
        ))
        .from(broadcast)
        .where(broadcast.streamer.id.eq(userId))
        .offset(page)
        .orderBy(new OrderSpecifier<>(Order.DESC, broadcast.createdAt))
        .limit(size)
        .fetch();

  }

}
