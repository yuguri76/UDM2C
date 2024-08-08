package com.example.livealone.order.service;

import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.service.BroadcastService;
import com.example.livealone.global.aop.DistributedLock;
import com.example.livealone.global.config.RedissonConfig;
import com.example.livealone.global.exception.CustomException;
import com.example.livealone.order.dto.OrderRequestDto;
import com.example.livealone.order.dto.OrderResponseDto;
import com.example.livealone.order.entity.Order;
import com.example.livealone.order.entity.OrderStatus;
import com.example.livealone.order.repository.OrderRepository;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.service.ProductService;
import com.example.livealone.user.entity.User;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final BroadcastService broadcastService;
    private final MessageSource messageSource;

    private final RedissonClient redissonClient;

    @DistributedLock(key = "'createOrder-' + #user.getId()")
    public OrderResponseDto createOrder(Long productId, Long broadcastId, User user, OrderRequestDto orderRequestDto) {

        Broadcast broadcast = broadcastService.findByBroadcastId(broadcastId);
        Product product = productService.findByProductId(productId);
        int orderQuantity = orderRequestDto.getQuantity();

        if (product.getQuantity() < orderQuantity) {
            throw new CustomException(messageSource.getMessage(
                    "no.exit.enough.product",
                    null,
                    CustomException.DEFAULT_ERROR_MESSAGE,
                    Locale.getDefault()
            ), HttpStatus.NOT_FOUND);
        }

        product.decreaseStock(orderQuantity);

        Order order = Order.builder()
                .user(user)
                .product(product)
                .quantity(orderQuantity)
                .orderStatus(OrderStatus.READY)
                .broadcast(broadcast)
                .build();

        productService.saveProduct(product);
        broadcastService.saveBroadcast(broadcast);
        Order curOder = orderRepository.save(order);

        return OrderResponseDto.builder().orderId(curOder.getId()).build();

    }

    @DistributedLock(key = "'checkStock-' + #productId")
    public void checkStock(Long productId) {

        Product product = productService.findByProductId(productId);

        if (product.getQuantity() < 1) {
            throw new CustomException(messageSource.getMessage(
                    "no.exit.enough.product",
                    null,
                    CustomException.DEFAULT_ERROR_MESSAGE,
                    Locale.getDefault()
            ), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 제한 시간 지났는지 확인 하는 메서드
     * @param user
     */
    @DistributedLock(key = "'checkTimeExpired-' + #productId")
    public void checkTimeExpired(User user, Long productId) {
        Order order = orderRepository.findCurrentOrderByUserAndProduct(user,productId);

        if(order == null){
            throw new CustomException(messageSource.getMessage(
                    "order.not.found",
                    null,
                    CustomException.DEFAULT_ERROR_MESSAGE,
                    Locale.getDefault()
            ), HttpStatus.NOT_FOUND);
        }

        long timeDifference = ChronoUnit.MINUTES.between(order.getCreatedAt(), LocalDateTime.now());

        if (timeDifference >= 10) {

            Product product = productService.findByProductId(productId);
            product.rollbackStock(order.getQuantity());
            productService.saveProduct(product);
            orderRepository.delete(order);

        } else {
            throw new CustomException(messageSource.getMessage(
                    "ten.minutes.yet",
                    null,
                    CustomException.DEFAULT_ERROR_MESSAGE,
                    Locale.getDefault()
            ), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 해당 방송의 총 주문 개수를 반환하는 메서드
     * @param broadcastId
     * @return
     */
    public Long sumOrderQuantity(Long broadcastId) {
        return orderRepository.sumQuantityByBroadcastId(broadcastId);
    }
}
