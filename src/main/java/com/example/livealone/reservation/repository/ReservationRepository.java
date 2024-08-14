package com.example.livealone.reservation.repository;

import com.example.livealone.reservation.entity.Reservations;
import com.example.livealone.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservations, Long>, ReservationRepositoryQuery {
  Optional<Reservations> findByAirTime(LocalDateTime airtime);

  Optional<Reservations> findByAirTimeBetweenAndStreamer(LocalDateTime start, LocalDateTime end, User user);

  List<Reservations> findByAirTimeGreaterThanEqualAndStreamer(LocalDateTime time, User user);

}
