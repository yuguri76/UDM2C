package com.example.livealone.broadcast.repository;

import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.entity.Reservations;
import com.example.livealone.broadcast.entity.BroadcastStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BroadcastRepository extends JpaRepository<Broadcast, Long>, BroadcastRepositoryQuery {
  Optional<Broadcast> findByBroadcastStatus(BroadcastStatus status);

  Optional<Broadcast> findByReservation(Reservations code);
}
