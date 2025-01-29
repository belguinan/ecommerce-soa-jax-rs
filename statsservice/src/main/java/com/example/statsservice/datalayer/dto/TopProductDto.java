package com.example.statsservice.datalayer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopProductDto {
    private Long id;
    private String name;
    private String type;
    private long itemsSold;
    private BigDecimal totalSales;
    private int currentStock;
}