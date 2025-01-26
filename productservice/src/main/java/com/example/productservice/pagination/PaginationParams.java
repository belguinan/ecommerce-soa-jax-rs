package com.example.productservice.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationParams {
    private int page = 1;
    private int perPage = 15;
    private String sortBy = "id";
    private String sortOrder = "DESC";
}