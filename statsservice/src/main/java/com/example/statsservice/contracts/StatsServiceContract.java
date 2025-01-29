package com.example.statsservice.contracts;

import com.example.statsservice.datalayer.dto.*;

public interface StatsServiceContract {

    DashboardDto getDashboardStats(String interval);

    SalesDto getSalesStats(String interval);

    ProductsDto getProductStats(String interval);

    UsersDto getUserStats(String interval);
}