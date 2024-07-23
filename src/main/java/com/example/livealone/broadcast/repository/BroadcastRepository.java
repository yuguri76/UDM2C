package com.example.livealone.broadcast.repository;

import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.entity.BroadcastStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BroadcastRepository extends JpaRepository<Broadcast, Long> {
  Optional<Broadcast> findByBroadcastStatus(BroadcastStatus status);
}
