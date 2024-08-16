package com.example.livealone.broadcast.repository;

import com.example.livealone.broadcast.dto.UserBroadcastResponseDto;
import java.awt.print.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;

public interface BroadcastRepositoryQuery {

  Page<UserBroadcastResponseDto> findAllByUserId(Long userId, int page, int size);

}
