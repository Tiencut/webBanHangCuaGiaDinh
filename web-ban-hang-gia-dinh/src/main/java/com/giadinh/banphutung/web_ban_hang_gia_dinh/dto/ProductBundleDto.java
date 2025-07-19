package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductBundle;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductBundleDto {
    private Long id;
    private Long parentProductId;
    private String parentProductName;
    private Long childProductId;
    private String childProductName;
    private String childProductCode;
    private Integer quantity;
    private BigDecimal bundlePrice;
    private Boolean isSubstitutable;
    private Integer sortOrder;
    private String notes;
    private String compatibilityGroup;
    private Long defaultSubstituteId;
    private String defaultSubstituteName;
    private List<Long> substituteProductIds;
    private List<String> substituteProductNames;
    private ProductBundle.BundleStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
} 