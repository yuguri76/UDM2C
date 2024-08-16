package com.example.livealone.user.repository;

import com.example.livealone.delivery.dto.DeliveryHistoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryQuery {

    Page<DeliveryHistoryResponseDto> findDeliveryHistoryByUserId(Long userId, int page);

}
