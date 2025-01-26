package com.example.productservice.database.repositories;

import com.example.productservice.database.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p WHERE p.sellerId = :sellerId AND p.id = :id")
    Optional<Product> findBySellerIdAndId(
        @Param("sellerId") Long sellerId,
        @Param("id") Long id
    );
}