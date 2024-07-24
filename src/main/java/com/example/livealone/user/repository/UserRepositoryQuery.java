package com.example.livealone.user.repository;

import com.example.livealone.delivery.dto.DeliveryHistoryResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryQuery {

    List<DeliveryHistoryResponseDto> findDeliveryHistoryByUserId(Long userId);

}
