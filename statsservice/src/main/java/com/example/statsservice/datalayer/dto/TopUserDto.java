package com.example.statsservice.datalayer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopUserDto {
    private Long id;
    private String username;
    private long ordersCount;
    private BigDecimal totalSpent;
    private long itemsBought;
}