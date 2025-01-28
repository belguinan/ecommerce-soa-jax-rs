package com.example.cartservice.database.repositories;

import com.example.cartservice.database.entities.Order;
import com.example.cartservice.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("SELECT p FROM Order p WHERE p.userId = :userId AND p.status = :orderStatus")
    Optional<Order> findByUserIdAndStatus(
        @Param("userId") Long userId,
        @Param("orderStatus") OrderStatus orderStatus
    );
}