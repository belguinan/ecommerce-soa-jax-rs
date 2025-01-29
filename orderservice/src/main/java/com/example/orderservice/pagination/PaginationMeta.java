package com.example.orderservice.pagination;

import lombok.Data;

@Data
public class PaginationMeta {
    private int currentPage;
    private int perPage;
    private long total;
    private int lastPage;
    private boolean hasMorePages;
    private int from;
    private int to;

    /**
     * @param currentPage
     * @param perPage
     * @param total
     */
    public PaginationMeta(int currentPage, int perPage, long total) {
        this.currentPage = currentPage;
        this.perPage = perPage;
        this.total = total;
        this.lastPage = (int) Math.ceil((double) total / perPage);
        this.hasMorePages = currentPage < lastPage;
        this.from = (currentPage - 1) * perPage + 1;
        this.to = Math.min(currentPage * perPage, (int) total);
    }
}

