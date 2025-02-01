package com.example.statsservice.datalayer.repositories;

import com.example.statsservice.datalayer.entities.ProductStats;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductStatsRepository extends JpaRepository<ProductStats, Long> {
    Optional<ProductStats> findByProductId(Long productId);

    @Query("SELECT p FROM ProductStats p ORDER BY p.totalSales DESC")
    List<ProductStats> findTopProducts(Pageable pageable);
}