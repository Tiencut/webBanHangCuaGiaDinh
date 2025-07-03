package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "suppliers")
@Data
@EqualsAndHashCode(callSuper = true)
public class Supplier extends BaseEntity {
    
    @NotBlank
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    
    @Column(name = "code", unique = true)
    private String code;
    
    @Size(max = 500)
    @Column(name = "address")
    private String address;
    
    @Size(max = 15)
    @Column(name = "phone")
    private String phone;
    
    @Email
    @Column(name = "email")
    private String email;
    
    @Column(name = "contact_person")
    private String contactPerson;
    
    @Column(name = "tax_code")
    private String taxCode;
    
    @Column(name = "bank_account")
    private String bankAccount;
    
    @Column(name = "bank_name")
    private String bankName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_terms")
    private PaymentTerms paymentTerms = PaymentTerms.CASH;
    
    @Column(name = "credit_limit", precision = 19, scale = 2)
    private BigDecimal creditLimit;
    
    @Column(name = "delivery_time_days")
    private Integer deliveryTimeDays;
    
    @Column(name = "rating")
    private Double rating = 0.0;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SupplierStatus status = SupplierStatus.ACTIVE;
    
    public enum PaymentTerms {
        CASH, NET_7, NET_15, NET_30, NET_45, NET_60
    }
    
    public enum SupplierStatus {
        ACTIVE, INACTIVE, BLACKLISTED
    }
}
