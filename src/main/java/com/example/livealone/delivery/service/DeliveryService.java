package com.example.livealone.delivery.service;

import com.example.livealone.delivery.dto.DeliveryHistoryResponseDto;
import com.example.livealone.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final UserRepository userRepository;

    public List<DeliveryHistoryResponseDto> getUserDeliveryHistory(Long userId,int page) {
        return userRepository.findDeliveryHistoryByUserId(userId, page);
    }
}
