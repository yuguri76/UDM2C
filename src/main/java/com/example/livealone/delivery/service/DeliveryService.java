package com.example.livealone.delivery.service;

import com.example.livealone.delivery.dto.DeliveryHistoryResponseDto;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.repository.UserRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final UserRepository userRepository;

    public List<DeliveryHistoryResponseDto> getUserDeliveryHistory(User user,int page) {
        return userRepository.findDeliveryHistoryByUserId(user.getId(), page);
    }
}
