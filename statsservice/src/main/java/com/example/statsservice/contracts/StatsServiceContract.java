package com.example.statsservice.contracts;

import com.example.statsservice.datalayer.dto.*;
import com.example.statsservice.datalayer.entities.ProductStats;
import com.example.statsservice.datalayer.entities.SalesStats;
import com.example.statsservice.datalayer.entities.UserStats;

import java.util.List;

public interface StatsServiceContract {

    SalesStats dashboard(String interval);
    List<SalesStats> sales(String interval);
    List<ProductStats> products(String interval);
    List<UserStats> users(String interval);
}