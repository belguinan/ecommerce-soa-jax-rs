package com.example.cartservice.pagination;

import lombok.Data;
import java.util.List;

@Data
public class Page<T> {
    private List<T> data;
    private PaginationMeta meta;

    /**
     * @param data
     * @param meta
     */
    public Page(List<T> data, PaginationMeta meta) {
        this.data = data;
        this.meta = meta;
    }
}