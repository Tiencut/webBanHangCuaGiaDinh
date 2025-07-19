package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private String code;
    private Integer sortOrder;
    private Integer level;
    private String path;
    private Long parentId;
    private String parentName;
    private List<CategoryDto> children;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
} 