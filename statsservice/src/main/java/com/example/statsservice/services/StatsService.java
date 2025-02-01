package com.example.statsservice.services;

import com.example.statsservice.contracts.StatsServiceContract;
import com.example.statsservice.datalayer.entities.ProductStats;
import com.example.statsservice.datalayer.entities.SalesStats;
import com.example.statsservice.datalayer.entities.UserStats;
import com.example.statsservice.datalayer.repositories.ProductStatsRepository;
import com.example.statsservice.datalayer.repositories.SalesStatsRepository;
import com.example.statsservice.datalayer.repositories.UserStatsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatsService implements StatsServiceContract {

    private final SalesStatsRepository salesStatsRepository;
    private final ProductStatsRepository productStatsRepository;
    private final UserStatsRepository userStatsRepository;

    public StatsService(SalesStatsRepository salesStatsRepository, ProductStatsRepository productStatsRepository, UserStatsRepository userStatsRepository) {
        this.salesStatsRepository = salesStatsRepository;
        this.productStatsRepository = productStatsRepository;
        this.userStatsRepository = userStatsRepository;
    }

    @Override
    public SalesStats dashboard(String interval) {

        LocalDate startDate = switch (interval) {
            case "week" -> LocalDate.now().minusDays(6); // Today included
            case "month" -> LocalDate.now().minusDays(29);
            default -> LocalDate.now();
        };

        return this.salesStatsRepository.findDashboardStatsByStartDate(startDate);
    }

    @Override
    public List<SalesStats> sales(String interval) {
        LocalDate startDate = switch (interval) {
            case "week" -> LocalDate.now().minusDays(6);
            case "month" -> LocalDate.now().minusDays(29);
            default -> LocalDate.now();
        };

        return this.salesStatsRepository.findSalesByStartDate(startDate);
    }

    @Override
    public List<ProductStats> products(String interval) {
        return this.productStatsRepository.findTopProducts(PageRequest.of(0, 10));
    }

    @Override
    public List<UserStats> users(String interval) {
        return this.userStatsRepository.findTopUsers(PageRequest.of(0, 10));
    }
}