package com.example.statsservice.resources;

import com.example.statsservice.contracts.StatsResourceContract;
import com.example.statsservice.contracts.StatsServiceContract;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

public class StatsResource implements StatsResourceContract {

    private final StatsServiceContract orderService;

    @Inject
    public StatsResource(StatsServiceContract orderService) {
        this.orderService = orderService;
    }

    @Override
    public Response dashboard(String interval) {
        return Response.ok(this.orderService.getDashboardStats(interval)).build();
    }

    @Override
    public Response sales(String interval) {
        return Response.ok(this.orderService.getSalesStats(interval)).build();
    }

    @Override
    public Response products(String interval) {
        return Response.ok(this.orderService.getProductStats(interval)).build();
    }

    @Override
    public Response users(String interval) {
        return Response.ok(this.orderService.getUserStats(interval)).build();
    }
}