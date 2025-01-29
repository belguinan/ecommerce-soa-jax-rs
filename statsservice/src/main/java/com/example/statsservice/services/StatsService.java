package com.example.statsservice.services;

import com.example.statsservice.contracts.StatsServiceContract;
import com.example.statsservice.datalayer.dto.*;
import com.example.statsservice.datalayer.repositories.StatsRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatsService implements StatsServiceContract {

    private final StatsRepository statsRepository;

    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public DashboardDto getDashboardStats(String interval) {

        Map<String, Object> stats = Optional.ofNullable(
            statsRepository.getDashboardStats(getStartDate(interval))
        ).orElseThrow(() -> new RuntimeException("Failed to fetch dashboard stats"));

        DashboardDto dto = new DashboardDto();

        dto.setOrdersCount(Optional.ofNullable(stats.get("orders_count"))
            .map(value -> ((Number) value).longValue())
            .orElse(0L));

        dto.setTotalSales(Optional.ofNullable(stats.get("total_sales"))
            .map(value -> new BigDecimal(value.toString()))
            .orElse(BigDecimal.ZERO));

        dto.setUsersCount(Optional.ofNullable(stats.get("users_count"))
            .map(value -> ((Number) value).longValue())
            .orElse(0L));

        dto.setProductsSoldCount(Optional.ofNullable(stats.get("products_sold_count"))
            .map(value -> ((Number) value).longValue())
            .orElse(0L));

        return dto;
    }


    @Override
    public SalesDto getSalesStats(String interval) {

        List<Map<String, Object>> timeSeries = switch (interval) {
            case "today" -> this.statsRepository.getHourlySalesStats(this.getStartDate(interval));
            default -> this.statsRepository.getDailySalesStats(this.getStartDate(interval));
        };

        List<SalesTimeSeriesDto> timeSeriesData = timeSeries.stream()
            .map(data -> {
                SalesTimeSeriesDto dto = new SalesTimeSeriesDto();
                Timestamp timestamp = (Timestamp) data.get("date");
                dto.setDate(timestamp.toLocalDateTime());
                dto.setOrdersCount(((Number) data.get("orders_count")).longValue());
                dto.setTotalSales(new BigDecimal(data.get("total_sales").toString()));
                dto.setUniqueUsers(((Number) data.get("unique_users")).longValue());
                dto.setItemsSold(((Number) data.get("items_sold")).longValue());
                return dto;
            }).collect(Collectors.toList());

        SalesDto dto = new SalesDto();
        dto.setTimeSeries(timeSeriesData);

        return dto;
    }

    @Override
    public ProductsDto getProductStats(String interval) {
        List<Map<String, Object>> products = this.statsRepository.getTopProducts(this.getStartDate(interval));

        List<TopProductDto> topProducts = products.stream()
            .map(data -> {
                TopProductDto dto = new TopProductDto();
                dto.setId(((Number) data.get("id")).longValue());
                dto.setName((String) data.get("name"));
                dto.setType((String) data.get("type"));
                dto.setItemsSold(((Number) data.get("items_sold")).longValue());
                dto.setTotalSales(new BigDecimal(data.get("total_sales").toString()));
                dto.setCurrentStock(((Number) data.get("current_stock")).intValue());
                return dto;
            }).collect(Collectors.toList());

        ProductsDto dto = new ProductsDto();
        dto.setTopProducts(topProducts);

        return dto;
    }

    @Override
    public UsersDto getUserStats(String interval) {
        List<Map<String, Object>> users = this.statsRepository.getTopUsers(this.getStartDate(interval));

        List<TopUserDto> topUsers = users.stream()
            .map(data -> {
                TopUserDto dto = new TopUserDto();
                dto.setId(((Number) data.get("id")).longValue());
                dto.setUsername((String) data.get("username"));
                dto.setOrdersCount(((Number) data.get("orders_count")).longValue());
                dto.setTotalSpent(new BigDecimal(data.get("total_spent").toString()));
                dto.setItemsBought(((Number) data.get("items_bought")).longValue());
                return dto;
            }).collect(Collectors.toList());

        UsersDto dto = new UsersDto();
        dto.setTopUsers(topUsers);

        return dto;
    }

    private LocalDateTime getStartDate(String interval) {
        LocalDateTime now = LocalDateTime.now();
        return switch (interval) {
            case "today" -> now.with(LocalTime.MIN);
            case "week" -> now.minusWeeks(1);
            case "month" -> now.minusMonths(1);
            default -> now.with(LocalTime.MIN);
        };
    }
}