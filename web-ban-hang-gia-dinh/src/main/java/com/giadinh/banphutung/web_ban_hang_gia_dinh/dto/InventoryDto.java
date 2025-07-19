package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventoryDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productCode;
    private Long supplierId;
    private String supplierName;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private BigDecimal averageCost;
    private BigDecimal lastPurchasePrice;
    private LocalDateTime lastStockIn;
    private LocalDateTime lastStockOut;
    private String location;
    private String batchNumber;
    private LocalDateTime expiryDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
} 