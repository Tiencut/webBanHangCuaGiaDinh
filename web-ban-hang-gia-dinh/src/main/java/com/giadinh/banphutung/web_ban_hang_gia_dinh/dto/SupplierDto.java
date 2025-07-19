package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SupplierDto {
    private Long id;
    private String name;
    private String code;
    private String address;
    private String phone;
    private String email;
    private String contactPerson;
    private String taxCode;
    private String bankAccount;
    private String bankName;
    private Supplier.PaymentTerms paymentTerms;
    private BigDecimal creditLimit;
    private Integer deliveryTimeDays;
    private String vehicleBrands;
    private Double rating;
    private String notes;
    private Supplier.SupplierStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
} 