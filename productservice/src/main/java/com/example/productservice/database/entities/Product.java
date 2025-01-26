package com.example.productservice.database.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long sellerId;

    @NotNull
    @NotEmpty
    private String type;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String reference;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @NotNull
    @Column(columnDefinition = "integer default 0")
    private Integer stock;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}