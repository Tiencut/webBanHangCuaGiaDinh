package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class CreateCustomerRequest {
    
    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(max = 200, message = "Tên khách hàng không được quá 200 ký tự")
    private String name;
    
    private String code;
    
    @Size(max = 500, message = "Địa chỉ không được quá 500 ký tự")
    private String address;
    
    @Size(max = 15, message = "Số điện thoại không được quá 15 ký tự")
    private String phone;
    
    @Email(message = "Email không hợp lệ")
    private String email;
    
    private String contactPerson;
    private String taxCode;
    private String companyName;
    private Customer.CustomerType customerType = Customer.CustomerType.RETAIL;
    private BigDecimal creditLimit = BigDecimal.ZERO;
    private BigDecimal currentDebt = BigDecimal.ZERO;
    private Double discountPercentage = 0.0;
    private Integer loyaltyPoints = 0;
    private String preferredVehicleBrands;
    private String notes;
    private Customer.CustomerStatus status = Customer.CustomerStatus.ACTIVE;
    private Long parentId;
} 