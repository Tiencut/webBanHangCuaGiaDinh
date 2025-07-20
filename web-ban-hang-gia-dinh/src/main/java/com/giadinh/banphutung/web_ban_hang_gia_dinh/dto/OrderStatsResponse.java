package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO để trả về thống kê đơn hàng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatsResponse {
    
    private LocalDate date;
    private int totalOrders;
    private int pendingOrders;
    private int confirmedOrders;
    private int processingOrders;
    private int shippedOrders;
    private int deliveredOrders;
    private int cancelledOrders;
    private int returnedOrders;
    
    private BigDecimal totalRevenue;
    private BigDecimal pendingRevenue;
    private BigDecimal confirmedRevenue;
    private BigDecimal processingRevenue;
    private BigDecimal shippedRevenue;
    private BigDecimal deliveredRevenue;
    private BigDecimal cancelledRevenue;
    private BigDecimal returnedRevenue;
    
    private Map<String, Integer> ordersByStatus;
    private Map<String, BigDecimal> revenueByStatus;
    
    // Constructor cho thống kê theo ngày
    public OrderStatsResponse(LocalDate date) {
        this.date = date;
        this.totalOrders = 0;
        this.totalRevenue = BigDecimal.ZERO;
    }
    
    // Constructor cho thống kê theo tháng
    public OrderStatsResponse(int year, int month) {
        this.date = LocalDate.of(year, month, 1);
        this.totalOrders = 0;
        this.totalRevenue = BigDecimal.ZERO;
    }
} 