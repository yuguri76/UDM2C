package com.example.livealone.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepositoryQuery {
  List<LocalDateTime> findByAirTimeBetween(LocalDateTime start, LocalDateTime end);
}
