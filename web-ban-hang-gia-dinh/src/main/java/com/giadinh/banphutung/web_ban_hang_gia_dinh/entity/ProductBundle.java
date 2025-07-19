package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductBundle - Entity quản lý sản phẩm combo/bundle
 * 
 * Một sản phẩm combo có thể được tạo từ nhiều sản phẩm con khác nhau
 * Ví dụ: Bộ phanh xe Hino = má phanh + đĩa phanh + dầu phanh + ống dẫn
 * 
 * Chức năng:
 * - Tạo combo từ nhiều sản phẩm
 * - Thay thế linh kiện trong combo
 * - Tính giá động khi có thay đổi
 * - Kiểm tra tương thích giữa các linh kiện
 */
@Entity
@Table(name = "product_bundles")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductBundle extends BaseEntity {
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_product_id")
    private Product parentProduct;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_product_id")
    private Product childProduct;
    
    /**
     * Số lượng sản phẩm con trong combo
     */
    @NotNull
    @Column(name = "quantity")
    private Integer quantity = 1;
    
    /**
     * Giá của sản phẩm con trong combo (có thể khác giá gốc)
     */
    @DecimalMin("0.0")
    @Column(name = "bundle_price", precision = 19, scale = 2)
    private BigDecimal bundlePrice;
    
    /**
     * Có thể thay thế sản phẩm con này không
     */
    @Column(name = "is_substitutable")
    private Boolean isSubstitutable = true;
    
    /**
     * Thứ tự hiển thị trong combo
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    /**
     * Ghi chú cho sản phẩm con trong combo
     */
    @Column(name = "notes")
    private String notes;
    
    /**
     * Nhóm tương thích - các sản phẩm có thể thay thế cho nhau
     */
    @Column(name = "compatibility_group")
    private String compatibilityGroup;
    
    /**
     * Sản phẩm thay thế mặc định (nếu có)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_substitute_id")
    private Product defaultSubstitute;
    
    /**
     * Danh sách sản phẩm có thể thay thế
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "bundle_substitute_products",
        joinColumns = @JoinColumn(name = "bundle_id"),
        inverseJoinColumns = @JoinColumn(name = "substitute_product_id")
    )
    private List<Product> substituteProducts = new ArrayList<>();
    
    /**
     * Trạng thái của sản phẩm con trong combo
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BundleStatus status = BundleStatus.ACTIVE;
    
    public enum BundleStatus {
        ACTIVE, INACTIVE, DISCONTINUED
    }
    
    // Business methods
    
    /**
     * Tính giá của sản phẩm con trong combo
     */
    public BigDecimal getEffectivePrice() {
        return bundlePrice != null ? bundlePrice : childProduct.getActualSellingPrice();
    }
    
    /**
     * Tính tổng giá của sản phẩm con trong combo
     */
    public BigDecimal getTotalPrice() {
        return getEffectivePrice().multiply(new BigDecimal(quantity));
    }
    
    /**
     * Kiểm tra xem sản phẩm con có thể thay thế không
     */
    public boolean canSubstitute() {
        return isSubstitutable && status == BundleStatus.ACTIVE;
    }
    
    /**
     * Thêm sản phẩm thay thế
     */
    public void addSubstituteProduct(Product substitute) {
        if (!substituteProducts.contains(substitute)) {
            substituteProducts.add(substitute);
        }
    }
    
    /**
     * Xóa sản phẩm thay thế
     */
    public void removeSubstituteProduct(Product substitute) {
        substituteProducts.remove(substitute);
    }
    
    /**
     * Kiểm tra xem một sản phẩm có thể thay thế cho sản phẩm con này không
     */
    public boolean canBeSubstitutedBy(Product substitute) {
        return substituteProducts.contains(substitute) || 
               (defaultSubstitute != null && defaultSubstitute.equals(substitute));
    }
} 