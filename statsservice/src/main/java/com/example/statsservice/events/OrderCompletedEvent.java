package com.example.statsservice.events;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderCompletedEvent {
    private Long orderId;
    private Long userId;
    private String userName;
    private BigDecimal total;
    private LocalDateTime completedAt;
    private List<OrderItemEvent> items;

    @Data
    public static class OrderItemEvent {
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal total;
    }
}