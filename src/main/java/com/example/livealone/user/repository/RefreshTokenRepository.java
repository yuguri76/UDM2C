package com.example.livealone.user.repository;

import com.example.livealone.user.entity.RefreshToken;
import org.apache.kafka.common.protocol.types.Field.Str;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
