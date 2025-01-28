package com.example.cartservice.contracts;

import com.example.cartservice.database.entities.Order;
import com.example.cartservice.database.entities.OrderItem;

public interface CartServiceContract {

    /**
     * @return Order
     */
    public Order index();

    public Order store(Long id, OrderItem orderItem) throws Exception;

    public Order update(Long id, OrderItem orderItem) throws Exception;

    public Order destroy(Long id);
}
