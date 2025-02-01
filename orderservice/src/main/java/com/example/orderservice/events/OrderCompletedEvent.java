package com.example.orderservice.events;

import com.example.orderservice.contracts.EventContract;
import com.example.orderservice.database.entities.Order;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderCompletedEvent implements EventContract<Order> {
    private Long orderId;
    private Long userId;
    private String userName;
    private BigDecimal total;
    private LocalDateTime completedAt;
    private List<OrderItemEvent> items;

    @Override
    public String getEventId() {
        return this.orderId.toString();
    }

    @Override
    public String getEventTopic() {
        return "order.completed";
    }

    @Data
    public static class OrderItemEvent {
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal total;
    }

    public static OrderCompletedEvent from(Order order) {

        OrderCompletedEvent event = new OrderCompletedEvent();

        event.setOrderId(order.getId());
        event.setUserId(order.getUserId());
        event.setTotal(order.getTotal());
        event.setUserName(order.getUserName());
        event.setCompletedAt(order.getCompletedAt());

        List<OrderCompletedEvent.OrderItemEvent> items = order
            .getItems()
            .stream()
            .map(item -> {
                OrderCompletedEvent.OrderItemEvent itemEvent = new OrderCompletedEvent.OrderItemEvent();
                itemEvent.setProductId(item.getProduct().getId());
                itemEvent.setProductName(item.getProduct().getName());
                itemEvent.setQuantity(item.getQuantity());
                itemEvent.setPrice(item.getPrice());
                itemEvent.setTotal(item.getTotal());
                return itemEvent;
            })
            .collect(Collectors.toList());

        event.setItems(items);

        return event;
    }
}