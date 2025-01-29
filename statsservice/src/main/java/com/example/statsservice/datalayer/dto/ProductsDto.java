package com.example.statsservice.datalayer.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductsDto {
    private List<TopProductDto> topProducts;
}