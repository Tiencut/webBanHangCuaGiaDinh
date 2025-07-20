package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventoryTransactionDto {
    private Long id;
    private Long productId;
    private String productName;
    private Long supplierId;
    private String supplierName;
    private String transactionType;
    private String documentCode;
    private String documentType;
    private String partnerName;
    private LocalDateTime transDate;
    private BigDecimal transPrice;
    private BigDecimal cost;
    private Integer quantity;
    private Integer endingStocks;
    private String note;
} 