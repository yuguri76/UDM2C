package com.example.livealone.broadcast.repository;

import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import com.example.livealone.broadcast.dto.UserBroadcastResponseDto;
import java.util.List;

public interface BroadcastRepositoryQuery {

  List<UserBroadcastResponseDto> findAllByUserId(Long userId, int page, int size);

}
