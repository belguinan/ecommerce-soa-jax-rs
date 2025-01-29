package com.example.statsservice.datalayer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardDto {
    private long ordersCount;
    private BigDecimal totalSales;
    private long usersCount;
    private long productsSoldCount;
}