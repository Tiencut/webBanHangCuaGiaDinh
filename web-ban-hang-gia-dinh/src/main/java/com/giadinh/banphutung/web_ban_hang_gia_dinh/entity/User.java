package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    
    @NotBlank
    @Size(max = 50)
    @Column(name = "username", unique = true)
    private String username;
    
    @NotBlank
    @Size(max = 100)
    @Email
    @Column(name = "email", unique = true)
    private String email;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "password")
    private String password;
    
    @Size(max = 100)
    @Column(name = "full_name")
    private String fullName;
    
    @Size(max = 15)
    @Column(name = "phone")
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;
    
    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;
    
    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission")
    private Set<Permission> permissions = new HashSet<>();
    
    public enum UserRole {
        ADMIN, MANAGER, STAFF, CUSTOMER, SHIPPER, USER
    }
    
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED, DELETED
    }
    
    public enum Permission {
        PRODUCT_CREATE, PRODUCT_UPDATE, PRODUCT_DELETE, PRODUCT_VIEW,
        ORDER_CREATE, ORDER_UPDATE, ORDER_DELETE, ORDER_VIEW,
        CUSTOMER_CREATE, CUSTOMER_UPDATE, CUSTOMER_DELETE, CUSTOMER_VIEW,
        SUPPLIER_CREATE, SUPPLIER_UPDATE, SUPPLIER_DELETE, SUPPLIER_VIEW,
        INVENTORY_CREATE, INVENTORY_UPDATE, INVENTORY_DELETE, INVENTORY_VIEW,
        REPORT_SALES, REPORT_INVENTORY, REPORT_FINANCIAL,
        USER_MANAGEMENT, SYSTEM_CONFIG
    }
}
