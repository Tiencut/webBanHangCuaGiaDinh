package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Long categoryId;
    private String categoryName;
    private BigDecimal basePrice;
    private BigDecimal sellingPrice;
    private BigDecimal costPrice;
    private Double weight;
    private String dimensions;
    private String brand;
    private String model;
    private String vehicleType;
    private String partNumber;
    private String oemNumber;
    private Integer warrantyPeriod;
    private Product.ProductStatus status;
    private Integer minStockLevel;
    private Integer reorderPoint;
    private Boolean isCombo;
    private Boolean isSubstitutable;
    private List<String> imageUrls;
    private List<String> tags;
    private List<Long> compatibleVehicleIds;
    private Integer totalStock;
    private Integer availableStock;
    private Integer reservedStock;
    private Integer supplierCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
} 