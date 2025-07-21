package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Data;

@Data
public class ProductAttributeValueDto {
    private Long id;
    private Long productId;
    private Long attributeId;
    private String attributeName;
    private String value;
    private String unit;
    private String type;
} 