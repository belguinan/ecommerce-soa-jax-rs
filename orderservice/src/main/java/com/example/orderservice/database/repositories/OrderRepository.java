package com.example.orderservice.database.repositories;

import com.example.orderservice.database.entities.Order;
import com.example.orderservice.enums.OrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("SELECT p FROM Order p WHERE p.userId = :userId AND p.status = :orderStatus")
    Optional<Order> findByUserIdAndStatus(
        @Param("userId") Long userId,
        @Param("orderStatus") OrderStatus orderStatus
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.status = :status")
    Optional<Order> findByUserIdAndStatusWithLock(
        @Param("userId") Long userId,
        @Param("status") OrderStatus status
    );

    @Query("SELECT o FROM Order o WHERE o.id = :id AND o.userId = :userId")
    Optional<Order> findByIdAndUserId(
        @Param("id") Long id, @Param("userId") Long userId
    );
}