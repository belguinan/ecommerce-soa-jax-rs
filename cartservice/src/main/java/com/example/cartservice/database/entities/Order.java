package com.example.cartservice.database.entities;

import com.example.cartservice.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(
    name = "orders",
    indexes = {
        @Index(name = "idx_orders_user_id", columnList = "userId"),
        @Index(name = "idx_orders_user_id_status", columnList = "userId,status")
    }
)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CART;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @NotNull
    @Column(nullable = false, columnDefinition = "decimal(10,2) default 0.00")
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "orderId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addItem(OrderItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        recalculateTotal();
    }

    public void removeItem(OrderItem item) {
        if (items != null) {
            items.remove(item);
            recalculateTotal();
        }
    }

    public void recalculateTotal() {
        if (items == null || items.isEmpty()) {
            this.total = BigDecimal.ZERO;
            return;
        }

        this.total = items.stream()
            .map(item -> item.getTotal() != null ? item.getTotal() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isCart() {
        return OrderStatus.CART.equals(this.status);
    }

    public boolean isPending() {
        return OrderStatus.PENDING.equals(this.status);
    }

    public boolean isCompleted() {
        return OrderStatus.COMPLETED.equals(this.status);
    }

    public boolean isCancelled() {
        return OrderStatus.CANCELLED.equals(this.status);
    }
}