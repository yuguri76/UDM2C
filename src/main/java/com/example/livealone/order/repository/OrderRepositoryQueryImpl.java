package com.example.livealone.order.repository;

import com.example.livealone.order.entity.Order;
import com.example.livealone.order.entity.QOrder;
import com.example.livealone.product.entity.QProduct;
import com.example.livealone.user.entity.QUser;
import com.example.livealone.user.entity.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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
}
