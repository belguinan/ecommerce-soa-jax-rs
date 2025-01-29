package com.example.statsservice.datalayer.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SalesTimeSeriesDto {
    private LocalDateTime date;
    private long ordersCount;
    private BigDecimal totalSales;
    private long uniqueUsers;
    private long itemsSold;
}