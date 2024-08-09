package com.example.livealone.order.repository;

import com.example.livealone.order.entity.Order;
import com.example.livealone.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order,Long>, OrderRepositoryQuery {
    Optional<Order> findByUser(User user);

    @Query("SELECT o FROM Order o JOIN FETCH o.product WHERE o.id = :orderId")
    Optional<Order> findByIdWithProduct(@Param("orderId") Long orderId);

}
