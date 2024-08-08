package com.example.livealone.reservation.repository;

import static com.example.livealone.reservation.entity.QReservations.reservations;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryQueryImpl implements ReservationRepositoryQuery {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<LocalDateTime> findByAirTimeBetween(LocalDateTime start, LocalDateTime end) {
    return queryFactory.select(reservations.airTime)
        .from(reservations)
        .where(reservations.airTime.between(start, end))
        .fetch();
  }
}
