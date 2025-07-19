package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleModelDto {
    private Long id;
    private String brand;
    private String model;
    private String year;
    private String engine;
    private String transmission;
    private String fuelType;
    private String bodyType;
    private String description;
    private String code;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
} 