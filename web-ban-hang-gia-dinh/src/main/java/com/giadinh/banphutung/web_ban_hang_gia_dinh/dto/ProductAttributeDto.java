package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Data;

@Data
public class ProductAttributeDto {
    private Long id;
    private String name;
    private String type;
    private String unit;
    private Long categoryId;
    private String categoryName;
} 