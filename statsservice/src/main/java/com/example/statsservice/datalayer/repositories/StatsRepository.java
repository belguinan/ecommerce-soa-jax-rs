package com.example.statsservice.datalayer.repositories;

import com.example.statsservice.datalayer.entities.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatsRepository extends JpaRepository<Stats, Long> {

       @Query(value = """
        SELECT
            COALESCE(COUNT(DISTINCT o.id), 0) as orders_count,
            COALESCE(SUM(o.total), 0) as total_sales,
            COALESCE(COUNT(DISTINCT o.user_id), 0) as users_count,
            COALESCE(COUNT(DISTINCT oi.product_id), 0) as products_sold_count
        FROM orders o 
        LEFT JOIN order_items oi ON o.id = oi.order_id
        WHERE o.created_at >= :startDate
        AND o.status = 'COMPLETED'
    """, nativeQuery = true)
    Map<String, Object> getDashboardStats(@Param("startDate") LocalDateTime startDate);

    @Query(value = """
        SELECT date_info.hour as date,
            COALESCE(COUNT(DISTINCT o.id), 0) as orders_count,
            COALESCE(SUM(o.total), 0) as total_sales,
            COALESCE(COUNT(DISTINCT o.user_id), 0) as unique_users,
            COALESCE(COUNT(oi.id), 0) as items_sold
        FROM (
            SELECT date_trunc('hour', dd)::timestamp as hour
            FROM generate_series(:startDate, now(), '1 hour'::interval) dd
        ) date_info
        LEFT JOIN orders o ON date_trunc('hour', o.created_at) = date_info.hour
            AND o.status = 'COMPLETED'
        LEFT JOIN order_items oi ON o.id = oi.order_id
        GROUP BY date_info.hour
        ORDER BY date_info.hour DESC
    """, nativeQuery = true)
    List<Map<String, Object>> getHourlySalesStats(@Param("startDate") LocalDateTime startDate);

    @Query(value = """
        SELECT date_info.day as date,
            COALESCE(COUNT(DISTINCT o.id), 0) as orders_count,
            COALESCE(SUM(o.total), 0) as total_sales,
            COALESCE(COUNT(DISTINCT o.user_id), 0) as unique_users,
            COALESCE(COUNT(oi.id), 0) as items_sold
        FROM (
            SELECT date_trunc('day', dd)::timestamp as day
            FROM generate_series(:startDate, now(), '1 day'::interval) dd
        ) date_info
        LEFT JOIN orders o ON date_trunc('day', o.created_at) = date_info.day
            AND o.status = 'COMPLETED'
        LEFT JOIN order_items oi ON o.id = oi.order_id
        GROUP BY date_info.day
        ORDER BY date_info.day DESC
    """, nativeQuery = true)
    List<Map<String, Object>> getDailySalesStats(@Param("startDate") LocalDateTime startDate);

    @Query(value = """
        SELECT 
            p.id,
            p.name,
            p.type,
            COUNT(oi.id) as items_sold,
            COALESCE(SUM(oi.total), 0) as total_sales,
            p.stock as current_stock
        FROM products p
        LEFT JOIN order_items oi ON p.id = oi.product_id
        LEFT JOIN orders o ON o.id = oi.order_id 
            AND o.status = 'COMPLETED'
            AND o.created_at >= :startDate
        GROUP BY p.id, p.name, p.type, p.stock
        HAVING COUNT(oi.id) > 0
        ORDER BY total_sales DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Map<String, Object>> getTopProducts(@Param("startDate") LocalDateTime startDate);

    @Query(value = """
        SELECT 
            u.id,
            u.username,
            COUNT(DISTINCT o.id) as orders_count,
            COALESCE(SUM(o.total), 0) as total_spent,
            COUNT(oi.id) as items_bought
        FROM users u
        LEFT JOIN orders o ON u.id = o.user_id 
            AND o.status = 'COMPLETED'
            AND o.created_at >= :startDate
        LEFT JOIN order_items oi ON o.id = oi.order_id
        GROUP BY u.id, u.username
        HAVING COUNT(DISTINCT o.id) > 0
        ORDER BY total_spent DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Map<String, Object>> getTopUsers(@Param("startDate") LocalDateTime startDate);
}