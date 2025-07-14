package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "products")
@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {
    
    @NotBlank
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    
    @Column(name = "code", unique = true)
    private String code;
    
    @Size(max = 1000)
    @Column(name = "description")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;
    
    @NotNull
    @DecimalMin("0.0")
    @Column(name = "base_price", precision = 19, scale = 2)
    private BigDecimal basePrice;
    
    @Column(name = "selling_price", precision = 19, scale = 2)
    private BigDecimal sellingPrice;
    
    @Column(name = "cost_price", precision = 19, scale = 2)
    private BigDecimal costPrice;
    
    @Column(name = "weight")
    private Double weight;
    
    @Column(name = "dimensions")
    private String dimensions; // "L x W x H"
    
    @Column(name = "brand")
    private String brand;
    
    @Column(name = "model")
    private String model;
    
    @Column(name = "vehicle_type")
    private String vehicleType; // Hino, Isuzu, Hyundai...
    
    @Column(name = "part_number")
    private String partNumber;
    
    @Column(name = "oem_number")
    private String oemNumber;
    
    @Column(name = "warranty_period")
    private Integer warrantyPeriod; // Tháng
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status = ProductStatus.ACTIVE;
    
    @Column(name = "min_stock_level")
    private Integer minStockLevel = 0;
    
    @Column(name = "reorder_point")
    private Integer reorderPoint = 10;
    
    @Column(name = "is_combo")
    private Boolean isCombo = false;
    
    @Column(name = "is_substitutable")
    private Boolean isSubstitutable = false;
    
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();
    
    /**
     * Danh sách mẫu xe tương thích với sản phẩm này
     * Quan hệ Many-to-Many với VehicleModel
     * 
     * Ví dụ: Hộp số 5 cấp có thể tương thích với:
     * - Thaco Ollin 500 (2018-2020)
     * - Hyundai HD72 (2015-2019)
     * - Isuzu QKR (2016-2022)
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_vehicle_compatibility",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "vehicle_model_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "compatibleProducts"})
    private List<VehicleModel> compatibleVehicles = new ArrayList<>();
    
    public enum ProductStatus {
        ACTIVE, INACTIVE, DISCONTINUED, OUT_OF_STOCK
    }
    
    // Business methods
    public BigDecimal getActualSellingPrice() {
        return sellingPrice != null ? sellingPrice : basePrice;
    }
}
