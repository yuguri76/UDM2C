package com.example.livealone.delivery.controller;

import com.example.livealone.delivery.dto.DeliveryHistoryResponseDto;
import com.example.livealone.delivery.service.DeliveryService;
import com.example.livealone.global.dto.CommonResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/user/{userId}/delivery")
    public ResponseEntity<CommonResponseDto<Page<DeliveryHistoryResponseDto>>> getUserDeliveryHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page) {

        Page<DeliveryHistoryResponseDto> deliveryHistoryResponseDto = deliveryService.getUserDeliveryHistory(userId, page - 1);
        CommonResponseDto<Page<DeliveryHistoryResponseDto>> commonResponseDto = CommonResponseDto.<Page<DeliveryHistoryResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("User Delivery History inquiry successfully")
                .data(deliveryHistoryResponseDto)
                .build();

        return ResponseEntity.ok().body(commonResponseDto);
    }
}
