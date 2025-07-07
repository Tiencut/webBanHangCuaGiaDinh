package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    
    @Size(max = 100)
    @Column(name = "city")
    private String city;
    
    @Size(max = 100)
    @Column(name = "district")
    private String district;
    
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
    
    // KiotViet compatibility fields
    @Size(max = 100)
    @Column(name = "region")
    private String region; // Khu vực
    
    @Size(max = 100)
    @Column(name = "ward")
    private String ward; // Phường/Xã
    
    @Size(max = 100)
    @Column(name = "supplier_group")
    private String supplierGroup; // Nhóm nhà cung cấp
    
    @Column(name = "total_purchased", precision = 19, scale = 2)
    private BigDecimal totalPurchased = BigDecimal.ZERO; // Tổng mua
    
    @Column(name = "current_debt", precision = 19, scale = 2)
    private BigDecimal currentDebt = BigDecimal.ZERO; // Nợ cần trả hiện tại
    
    @Column(name = "last_transaction_date")
    private LocalDate lastTransactionDate; // Ngày giao dịch cuối cùng
    
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
