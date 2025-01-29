package com.example.orderservice.resources;

import com.example.orderservice.contracts.OrderResourceContract;
import com.example.orderservice.contracts.OrderServiceContract;
import com.example.orderservice.database.entities.Order;
import com.example.orderservice.pagination.PaginationParams;
import com.example.orderservice.requests.CheckoutRequest;
import com.example.orderservice.requests.FilterRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

public class OrderResource implements OrderResourceContract {

    private final OrderServiceContract orderService;

    @Inject
    public OrderResource(OrderServiceContract orderService) {
        this.orderService = orderService;
    }

    @Override
    public Response index(
        int page,
        int perPage,
        String sortBy,
        String sortOrder
    ) {
        FilterRequest filterRequest = new FilterRequest();
        PaginationParams params = new PaginationParams(page, perPage, sortBy, sortOrder);
        return Response.ok(this.orderService.index(params, filterRequest)).build();
    }

    @Override
    public Response show(Long id) {
        return Response.ok(this.orderService.show(id)).build();
    }

    @Override
    public Response checkout(CheckoutRequest request) {
        return Response.ok(this.orderService.checkout(request)).build();
    }
}