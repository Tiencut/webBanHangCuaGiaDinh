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
@Table(name = "customers")
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseEntity {
    
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
    
    @Column(name = "company_name")
    private String companyName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type")
    private CustomerType customerType = CustomerType.RETAIL;
    
    @Column(name = "credit_limit", precision = 19, scale = 2)
    private BigDecimal creditLimit = BigDecimal.ZERO;
    
    @Column(name = "current_debt", precision = 19, scale = 2)
    private BigDecimal currentDebt = BigDecimal.ZERO;
    
    @Column(name = "discount_percentage")
    private Double discountPercentage = 0.0;
    
    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;
    
    @Column(name = "first_purchase_date")
    private LocalDate firstPurchaseDate;
    
    @Column(name = "last_purchase_date")
    private LocalDate lastPurchaseDate;
    
    @Column(name = "total_orders")
    private Integer totalOrders = 0;
    
    @Column(name = "total_spent", precision = 19, scale = 2)
    private BigDecimal totalSpent = BigDecimal.ZERO;
    
    @Column(name = "preferred_vehicle_brands")
    private String preferredVehicleBrands; // Hino,Isuzu,Hyundai
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CustomerStatus status = CustomerStatus.ACTIVE;
    
    public enum CustomerType {
        RETAIL,      // Khách lẻ
        WHOLESALE,   // Khách sỉ
        GARAGE,      // Garage sửa chữa
        DEALER,      // Đại lý
        VIP          // Khách VIP
    }
    
    public enum CustomerStatus {
        ACTIVE, INACTIVE, BLACKLISTED
    }
    
    // Business methods
    public boolean canPurchase(BigDecimal orderAmount) {
        return currentDebt.add(orderAmount).compareTo(creditLimit) <= 0;
    }
    
    public boolean isVip() {
        return customerType == CustomerType.VIP || 
               totalSpent.compareTo(new BigDecimal("50000000")) >= 0; // 50M VND
    }
}
