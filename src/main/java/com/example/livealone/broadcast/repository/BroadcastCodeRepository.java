package com.example.livealone.broadcast.repository;

import com.example.livealone.broadcast.entity.BroadcastCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BroadcastCodeRepository extends JpaRepository<BroadcastCode, Long> {

  Optional<BroadcastCode> findByCode(String code);
}
