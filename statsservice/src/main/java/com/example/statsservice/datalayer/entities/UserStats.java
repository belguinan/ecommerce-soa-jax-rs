package com.example.statsservice.datalayer.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(
    name = "user_stats",
    uniqueConstraints = {
        @UniqueConstraint(name ="user_id_unique", columnNames = {"userId"})
    }
)
public class UserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String userName;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long totalOrders = 0L;

    @Column(nullable = false)
    private Long totalQuantity = 0L;

    @Column(nullable = false)
    private Long totalProducts = 0L;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default 0.00")
    private BigDecimal totalSales = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}