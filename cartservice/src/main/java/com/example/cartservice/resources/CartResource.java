package com.example.cartservice.resources;

import com.example.cartservice.contracts.CartResourceContract;
import com.example.cartservice.contracts.CartServiceContract;
import com.example.cartservice.database.entities.OrderItem;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

public class CartResource implements CartResourceContract {

    private final CartServiceContract cartService;

    @Inject
    public CartResource(CartServiceContract cartService) {
        this.cartService = cartService;
    }

    @Override
    public Response index() {
        return Response.ok(this.cartService.index()).build();
    }

    @Override
    public Response store(Long id, OrderItem orderItem) throws Exception {
        return Response.ok(this.cartService.store(id, orderItem)).build();
    }

    @Override
    public Response update(Long id, OrderItem orderItem) throws Exception {
        return Response.ok(this.cartService.update(id, orderItem)).build();
    }

    @Override
    public Response destroy(Long id) {
        return Response.ok(this.cartService.destroy(id)).build();
    }
}