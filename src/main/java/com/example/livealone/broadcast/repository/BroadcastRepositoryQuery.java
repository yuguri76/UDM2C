package com.example.livealone.broadcast.repository;

import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import java.util.List;

public interface BroadcastRepositoryQuery {

  List<BroadcastResponseDto> findAllByUserId(Long userId, int page, int size);

}
