package com.example.orderservice.services;

import com.example.orderservice.auth.AuthContext;
import com.example.orderservice.contracts.OrderServiceContract;
import com.example.orderservice.database.entities.Order;
import com.example.orderservice.database.entities.OrderItem;
import com.example.orderservice.database.entities.Product;
import com.example.orderservice.database.repositories.OrderRepository;
import com.example.orderservice.database.repositories.OrderItemRepository;
import com.example.orderservice.database.repositories.ProductRepository;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.helpers.SpecificationFactory;
import com.example.orderservice.pagination.Page;
import com.example.orderservice.pagination.PaginationFactory;
import com.example.orderservice.pagination.PaginationParams;
import com.example.orderservice.requests.CheckoutRequest;
import com.example.orderservice.requests.FilterRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.BadRequestException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements OrderServiceContract {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final AuthContext authContext;

    public OrderService(
        OrderRepository orderRepository,
        OrderItemRepository orderItemRepository,
        ProductRepository productRepository,
        AuthContext authContext
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.authContext = authContext;
    }

    @Override
    public Page<Order> index(PaginationParams paginationParams, FilterRequest filterRequest) {
        // Get current user orders
        filterRequest.setUserId(String.valueOf(this.authContext.getCurrentUserId()));

        // Do not show cart order
        Specification<Order> specification = SpecificationFactory.create(filterRequest);
        specification = specification.and((root, query, cb) -> cb.notEqual(root.get("status"), OrderStatus.CART));

        return (new PaginationFactory<Order>(this.orderRepository)).create(paginationParams, specification);
    }

    public Order show(Long id) {
        return this.orderRepository.findByIdAndUserId(id, this.authContext.getCurrentUserId()).orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public Order checkout(CheckoutRequest request) {
        Order dbOrder = this.orderRepository
                .findByUserIdAndStatusWithLock(this.authContext.getCurrentUserId(), OrderStatus.CART)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        List<OrderItem> items = dbOrder.getItems();

        if (items.isEmpty()) {
            throw new BadRequestException("No items found in your cart.");
        }

        // Validate and update stock
        for (OrderItem item : items) {
            Product product = this.productRepository
                    .findByIdWithLock(item.getProduct().getId())
                    .orElseThrow(() -> new BadRequestException("Product not found: " + item.getProduct().getId()));

            if (product.getStock() < item.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - item.getQuantity());
            this.productRepository.save(product);
        }

        // Update order details
        dbOrder.setStatus(OrderStatus.COMPLETED);
        dbOrder.setAddress(request.getAddress());
        dbOrder.setName(request.getName());
        dbOrder.setPhoneNumber(request.getPhoneNumber());
        dbOrder.setCity(request.getCity());
        dbOrder.recalculateTotal();

        return this.orderRepository.save(dbOrder);
    }
}