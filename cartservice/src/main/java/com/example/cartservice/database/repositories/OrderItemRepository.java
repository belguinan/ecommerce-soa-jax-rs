package com.example.cartservice.database.repositories;

import com.example.cartservice.database.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {
    Optional<OrderItem> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT p FROM OrderItem p WHERE p.product.id = :productId AND p.userId = :currentUserId AND p.orderId = :orderId")
    Optional<OrderItem> findCartItem(
            @Param("orderId") Long orderId,
            @Param("productId") Long productId,
            @Param("currentUserId") Long currentUserId
    );
}