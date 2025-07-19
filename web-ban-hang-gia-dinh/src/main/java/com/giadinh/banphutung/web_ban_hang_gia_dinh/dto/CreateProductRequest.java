package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateProductRequest {
    
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 200, message = "Tên sản phẩm không được quá 200 ký tự")
    private String name;
    
    private String code;
    
    @Size(max = 1000, message = "Mô tả không được quá 1000 ký tự")
    private String description;
    
    private Long categoryId;
    
    @NotNull(message = "Giá cơ bản không được để trống")
    @DecimalMin(value = "0.0", message = "Giá cơ bản phải lớn hơn 0")
    private BigDecimal basePrice;
    
    @DecimalMin(value = "0.0", message = "Giá bán phải lớn hơn 0")
    private BigDecimal sellingPrice;
    
    @DecimalMin(value = "0.0", message = "Giá vốn phải lớn hơn 0")
    private BigDecimal costPrice;
    
    private Double weight;
    private String dimensions;
    private String brand;
    private String model;
    private String vehicleType;
    private String partNumber;
    private String oemNumber;
    private Integer warrantyPeriod;
    private Product.ProductStatus status = Product.ProductStatus.ACTIVE;
    private Integer minStockLevel = 0;
    private Integer reorderPoint = 10;
    private Boolean isCombo = false;
    private Boolean isSubstitutable = false;
    private List<String> imageUrls;
    private List<String> tags;
    private List<Long> compatibleVehicleIds;
} 