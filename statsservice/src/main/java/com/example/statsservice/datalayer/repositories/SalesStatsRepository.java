package com.example.statsservice.datalayer.repositories;

import com.example.statsservice.datalayer.entities.SalesStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalesStatsRepository extends JpaRepository<SalesStats, Long> {

    @Query("SELECT p FROM SalesStats p WHERE CAST(p.createdAt as localdate) = :createdAt")
    Optional<SalesStats> findByCreatedAt(@Param("createdAt") LocalDate createdAt);

    @Query("""
        SELECT 
            NEW SalesStats(
                COALESCE(SUM(s.totalOrders), 0),
                COALESCE(SUM(s.totalProducts), 0),
                COALESCE(SUM(s.totalSales), 0)
            ) 
        FROM 
            SalesStats s 
        WHERE CAST(s.createdAt as localdate) >= :startDate        
    """)
    SalesStats findDashboardStatsByStartDate(@Param("startDate") LocalDate startDate);

    @Query("SELECT s FROM SalesStats s WHERE CAST(s.createdAt as localdate) >= :startDate ORDER BY s.createdAt")
    List<SalesStats> findSalesByStartDate(@Param("startDate") LocalDate startDate);

}