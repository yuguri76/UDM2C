package com.example.livealone.order.repository;

import com.example.livealone.admin.dto.AdminConsumerResponseDto;
import com.example.livealone.admin.mapper.AdminMapper;
import com.example.livealone.order.entity.Order;
import com.example.livealone.order.entity.QOrder;
import com.example.livealone.product.entity.QProduct;
import com.example.livealone.user.entity.QUser;
import com.example.livealone.user.entity.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryQueryImpl implements OrderRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long sumQuantityByBroadcastId(Long broadcastId) {
        QOrder order = QOrder.order;

        Integer sum = jpaQueryFactory.select(order.quantity.sum())
                .from(order)
                .where(order.broadcast.id.eq(broadcastId))
                .fetchOne();

        if (sum == null) {
            sum = 0;
        }

        return (long) sum;
    }

    @Override
    public Order findCurrentOrderByUserAndProduct(User user, Long productId) {

        QProduct qProduct = QProduct.product;
        QUser qUser = QUser.user;
        QOrder qOrder = QOrder.order;
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(com.querydsl.core.types.Order.DESC, qOrder.id);
        return jpaQueryFactory
                .select(qOrder)
                .from(qOrder)
                .where(qOrder.user.eq(user).and(qOrder.product.id.eq(productId)))
                .leftJoin(qUser).on(qOrder.user.eq(user)).fetchJoin()
                .leftJoin(qProduct).on(qOrder.product.id.eq(productId)).fetchJoin()
                .orderBy(orderSpecifier)
                .fetchFirst();
    }

    @Override
    public Page<AdminConsumerResponseDto> findAllByBroadcastId(Long broadcastId, int page, int size) {
        QOrder qOrder = QOrder.order;
        Pageable pageable = PageRequest.of(page, size);

        List<Order> orderList = jpaQueryFactory.selectFrom(qOrder)
            .where(qOrder.broadcast.id.eq(broadcastId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        List<AdminConsumerResponseDto> adminConsumerResponseDtoList = orderList.stream()
            .map(AdminMapper::toAdminConsumerResponseDto)
            .collect(Collectors.toList());

        return new PageImpl<>(adminConsumerResponseDtoList, pageable, orderList.size());
    }
}
