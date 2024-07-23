package com.example.livealone.broadcast.repository;

import com.example.livealone.broadcast.entity.Broadcast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BroadcastRepository extends JpaRepository<Broadcast, Long> {
  
}
