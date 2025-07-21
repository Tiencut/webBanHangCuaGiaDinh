package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySearchRequest {
    private String keyword;
    private Long parentId;
    private Integer level;
    private Boolean isActive;
    private String productCountRange; // "0", "1-5", "6-10", "10+"
    private String sortBy = "name"; // "name", "code", "level", "sortOrder", "productCount"
    private String sortDirection = "asc"; // "asc", "desc"
    private Integer page;
    private Integer size;
} 