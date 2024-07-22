package com.example.livealone.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.livealone.user.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
