package com.example.livealone.delivery.service;

import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.entity.BroadcastCode;
import com.example.livealone.broadcast.entity.BroadcastStatus;
import com.example.livealone.delivery.dto.DeliveryHistoryResponseDto;
import com.example.livealone.order.entity.Order;
import com.example.livealone.order.entity.OrderStatus;
import com.example.livealone.product.entity.Product;
import com.example.livealone.user.entity.Social;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final UserRepository userRepository;

    public List<DeliveryHistoryResponseDto> getUserDeliveryHistory(/*user*/) {

        //임의로 userId 입력
        return userRepository.findDeliveryHistoryByUserId(2L);
    }
}
