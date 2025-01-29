package com.example.orderservice.contracts;

import com.example.orderservice.database.entities.Order;
import com.example.orderservice.pagination.Page;
import com.example.orderservice.pagination.PaginationParams;
import com.example.orderservice.requests.CheckoutRequest;
import com.example.orderservice.requests.FilterRequest;

public interface OrderServiceContract {

    /**
     * @return Order
     */
    public Page<Order> index(PaginationParams paginationParams, FilterRequest filterRequest);

    public Order show(Long id);

    public Order checkout(CheckoutRequest request);
}
