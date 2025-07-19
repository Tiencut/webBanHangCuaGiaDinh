package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CustomerDto {
    private Long id;
    private String name;
    private String code;
    private String address;
    private String phone;
    private String email;
    private String contactPerson;
    private String taxCode;
    private String companyName;
    private Customer.CustomerType customerType;
    private BigDecimal creditLimit;
    private BigDecimal currentDebt;
    private Double discountPercentage;
    private Integer loyaltyPoints;
    private LocalDate firstPurchaseDate;
    private LocalDate lastPurchaseDate;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private String preferredVehicleBrands;
    private String notes;
    private Customer.CustomerStatus status;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
} 