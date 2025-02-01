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
    name = "sales_stats",
    indexes = {
        @Index(name = "idx_created_at", columnList = "created_at")
    }
)
public class SalesStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public SalesStats(Long totalOrders, Long totalProducts, BigDecimal totalSales) {
        this.totalOrders = totalOrders;
        this.totalProducts = totalProducts;
        this.totalSales = totalSales;
    }

    public SalesStats() {}

    @Column(nullable = false)
    private Long totalOrders = 0L;

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