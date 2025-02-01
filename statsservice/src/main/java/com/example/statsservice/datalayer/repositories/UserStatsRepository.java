package com.example.statsservice.datalayer.repositories;

import com.example.statsservice.datalayer.entities.ProductStats;
import com.example.statsservice.datalayer.entities.UserStats;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    Optional<UserStats> findByUserId(Long userId);

    @Query("SELECT u FROM UserStats u ORDER BY u.totalSales DESC")
    List<UserStats> findTopUsers(Pageable pageable);
}