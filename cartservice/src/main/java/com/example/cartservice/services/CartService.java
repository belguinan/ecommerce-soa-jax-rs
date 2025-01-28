package com.example.cartservice.services;

import com.example.cartservice.auth.AuthContext;
import com.example.cartservice.contracts.CartServiceContract;
import com.example.cartservice.database.entities.Order;
import com.example.cartservice.database.entities.OrderItem;
import com.example.cartservice.database.entities.Product;
import com.example.cartservice.database.repositories.OrderRepository;
import com.example.cartservice.database.repositories.OrderItemRepository;
import com.example.cartservice.database.repositories.ProductRepository;
import com.example.cartservice.enums.OrderStatus;
import org.springframework.stereotype.Service;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.BadRequestException;

import java.math.BigDecimal;

@Service
public class CartService implements CartServiceContract {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final AuthContext authContext;

    public CartService(
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
    public Order index() {
        return this.orderRepository.findByUserIdAndStatus(
            this.authContext.getCurrentUserId(),
            OrderStatus.CART
        ).orElseGet(() -> {
            Order order = new Order();
            order.setStatus(OrderStatus.CART);
            order.setTotal(BigDecimal.ZERO);
            order.setUserId(this.authContext.getCurrentUserId());
            return this.orderRepository.save(order);
        });
    }

    @Override
    public Order store(Long productId, OrderItem orderItem) {
        Product product = this.productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product not found"));

        Order cart = this.index();

        try {
            OrderItem existingItem = this.orderItemRepository
                .findCartItem(cart.getId(), productId, this.authContext.getCurrentUserId())
                .orElseThrow(NotFoundException::new);

            int newQuantity = existingItem.getQuantity() + orderItem.getQuantity();

            if (newQuantity > product.getStock()) {
                throw new BadRequestException("Not enough stock available. Available: " + product.getStock());
            }

            existingItem.setQuantity(newQuantity);
            return this.update(existingItem.getId(), existingItem);

        } catch (NotFoundException e) {
            if (orderItem.getQuantity() > product.getStock()) {
                throw new BadRequestException("Not enough stock available. Available: " + product.getStock());
            }

            orderItem.setOrderId(cart.getId());
            orderItem.setUserId(this.authContext.getCurrentUserId());
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
            cart.addItem(orderItem);
            this.orderItemRepository.save(orderItem);

            return this.orderRepository.save(cart);
        }
    }

    @Override
    public Order update(Long itemId, OrderItem updatedItem) {
        OrderItem existingItem = this.orderItemRepository
            .findByIdAndUserId(itemId, this.authContext.getCurrentUserId())
            .orElseThrow(() -> new NotFoundException("Cart item not found"));

        Product product = existingItem.getProduct();

        if (updatedItem.getQuantity() > product.getStock()) {
            throw new BadRequestException("Not enough stock available. Available: " + product.getStock());
        }

        existingItem.setQuantity(updatedItem.getQuantity());
        this.orderItemRepository.save(existingItem);

        return this.index();
    }

    @Override
    public Order destroy(Long itemId) {
        OrderItem orderItem = this.orderItemRepository
            .findByIdAndUserId(itemId, this.authContext.getCurrentUserId())
            .orElseThrow(() -> new NotFoundException("Cart item not found"));

        this.orderItemRepository.delete(orderItem);
        return this.index();
    }
}