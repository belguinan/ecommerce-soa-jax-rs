package com.example.statsservice.events;

import com.example.statsservice.datalayer.entities.ProductStats;
import com.example.statsservice.datalayer.entities.SalesStats;
import com.example.statsservice.datalayer.entities.UserStats;
import com.example.statsservice.datalayer.repositories.ProductStatsRepository;
import com.example.statsservice.datalayer.repositories.SalesStatsRepository;
import com.example.statsservice.datalayer.repositories.UserStatsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderCreatedHandler {

    private final SalesStatsRepository salesStatsRepository;
    private final ProductStatsRepository productStatsRepository;
    private final UserStatsRepository userStatsRepository;

    public OrderCreatedHandler(SalesStatsRepository salesStatsRepository, ProductStatsRepository productStatsRepository, UserStatsRepository userStatsRepository) {
        this.salesStatsRepository = salesStatsRepository;
        this.productStatsRepository = productStatsRepository;
        this.userStatsRepository = userStatsRepository;
    }

    public void updateOrCreateStats(OrderCompletedEvent event) {
        this.updateOrCreateSalesStats(event);
        this.updateOrCreateProductStats(event);
        this.updateOrCreateUserStats(event);
    }

    public SalesStats updateOrCreateSalesStats(OrderCompletedEvent event)
    {
        SalesStats stats = this.salesStatsRepository
                .findByCreatedAt(LocalDate.now())
                .orElse(new SalesStats());

        stats.setTotalOrders(stats.getTotalOrders() + 1);
        stats.setTotalSales(stats.getTotalSales().add(event.getTotal()));

        // Total qty sold
        stats.setTotalProducts(
            stats.getTotalProducts() + event.getItems().stream()
                    .reduce(0, (total, item) -> item.getQuantity() + total, Integer::sum)
        );

        return this.salesStatsRepository.save(stats);
    }

    public List<ProductStats> updateOrCreateProductStats(OrderCompletedEvent event)
    {
        List<ProductStats> list = new ArrayList<>();

        event.getItems().forEach(item -> {

            ProductStats stats = this.productStatsRepository
                    .findByProductId(item.getProductId())
                    .orElse(new ProductStats());

            System.out.println(stats);

            // Set default attributes
            stats.setProductName(item.getProductName());
            stats.setProductId(item.getProductId());

            stats.setTotalOrders(stats.getTotalOrders() + 1);
            stats.setTotalSales(stats.getTotalSales().add(item.getTotal()));
            stats.setTotalQuantity(stats.getTotalQuantity() + item.getQuantity());

            list.add(this.productStatsRepository.save(stats));
        });

        return list;
    }

    public UserStats updateOrCreateUserStats(OrderCompletedEvent event) {
        UserStats stats = this.userStatsRepository.findByUserId(event.getUserId()).orElse(new UserStats());

        stats.setUserId(event.getUserId());
        stats.setUserName(event.getUserName());
        stats.setTotalOrders(stats.getTotalOrders() + 1);
        stats.setTotalSales(stats.getTotalSales().add(event.getTotal()));
        stats.setTotalProducts(stats.getTotalProducts() + event.getItems().size());
        stats.setTotalQuantity(stats.getTotalQuantity() + event.getItems().stream().reduce(0, (total, item) -> item.getQuantity() + total, Integer::sum));

        return this.userStatsRepository.save(stats);
    }
}