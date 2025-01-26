package com.example.productservice.resources;

import com.example.productservice.contracts.ProductResourceContract;
import com.example.productservice.contracts.ProductServiceContract;
import com.example.productservice.database.entities.Product;
import com.example.productservice.pagination.PaginationParams;
import com.example.productservice.requests.LoginRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

public class ProductResource implements ProductResourceContract {

    private final ProductServiceContract productService;

    @Inject
    public ProductResource(ProductServiceContract productServiceContract) {
        this.productService = productServiceContract;
    }

    @Override
    public Response index(
        int page,
        int perPage,
        String sortBy,
        String sortOrder
    ) {
        PaginationParams params = new PaginationParams(page, perPage, sortBy, sortOrder);
        return Response.ok(this.productService.index(params)).build();
    }

    @Override
    public Response sellerProducts(
        int page,
        int perPage,
        String sortBy,
        String sortOrder
    ) {
        PaginationParams params = new PaginationParams(page, perPage, sortBy, sortOrder);
        return Response.ok(this.productService.sellerProducts(params)).build();
    }

    @Override
    public Response store(Product product) {
        return Response.ok(this.productService.store(product)).build();
    }

    @Override
    public Response update(Long id, Product product) {
        return Response.ok(this.productService.update(id, product)).build();
    }

    @Override
    public Response destroy(Long id) {
        return Response.ok(this.productService.destroy(id)).build();
    }
}