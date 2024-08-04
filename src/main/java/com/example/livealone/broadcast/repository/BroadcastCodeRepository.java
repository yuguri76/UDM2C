package com.example.livealone.broadcast.repository;

import com.example.livealone.broadcast.entity.BroadcastCode;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BroadcastCodeRepository extends JpaRepository<BroadcastCode, Long> {

  Optional<BroadcastCode> findByCode(String code);
  Optional<BroadcastCode> findByAirTimeBetween(LocalDateTime airTime, LocalDateTime airTime2);

  Optional<BroadcastCode> findByAirTime(LocalDateTime airtime);
}
