package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailDto {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private String productCode;
    private Long supplierId;
    private String supplierName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private BigDecimal lineTotal;
    private String notes;
    private Boolean isActive;
} 