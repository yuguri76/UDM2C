package com.example.livealone.order.controller;

import com.example.livealone.delivery.dto.DeliveryHistoryResponseDto;
import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.order.dto.OrderRequestDto;
import com.example.livealone.order.dto.OrderResponseDto;
import com.example.livealone.order.service.OrderService;
import com.example.livealone.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 수량 입력 완료 버튼에 사용하는 API
     * @param productId
     * @param broadcastId
     * @param userDetails
     * @param orderRequestDto
     * @return
     */
    @PostMapping("/order/broadcast/{broadcastId}/product/{productId}")
    public ResponseEntity<CommonResponseDto<OrderResponseDto>> createOrder(
        @PathVariable("productId") Long productId,
        @PathVariable("broadcastId") Long broadcastId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody OrderRequestDto orderRequestDto
    ) throws JsonProcessingException {

        User user = userDetails.getUser();

        OrderResponseDto orderResponseDto = orderService.createOrder(productId, broadcastId, user, orderRequestDto);

        CommonResponseDto<OrderResponseDto> commonResponseDto = CommonResponseDto.<OrderResponseDto>builder()
            .status(HttpStatus.OK.value())
            .message("create order successfully")
            .data(orderResponseDto)
            .build();

        return ResponseEntity.ok().body(commonResponseDto);
    }

    /**
     * 구매하기 버튼에 사용하는 API
     * @param productId
     * @return
     */
    @PostMapping("/broadcast/{broadcastId}/product/{productId}")
    public ResponseEntity<CommonResponseDto<Void>> checkStock(
            @PathVariable("productId") Long productId
    ) {
        orderService.checkStock(productId);

        CommonResponseDto<Void> commonResponseDto = CommonResponseDto.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("check stock successfully")
                .data(null)
                .build();

        return ResponseEntity.ok().body(commonResponseDto);
    }

    /**
     * 결제 페이지에 10분이상 머무를 때 생성되었던 Order를 삭제하고 상품 재고를 복구하는 API
     * @param productId
     * @param userDetails
     * @return
     */
    @DeleteMapping("/order/product/{productId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteOrder(
            @PathVariable("productId") Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();

        orderService.checkTimeExpired(user, productId);

        CommonResponseDto<Void> commonResponseDto = CommonResponseDto.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("delete order successfully")
                .data(null)
                .build();

        return ResponseEntity.ok().body(commonResponseDto);
    }
}
