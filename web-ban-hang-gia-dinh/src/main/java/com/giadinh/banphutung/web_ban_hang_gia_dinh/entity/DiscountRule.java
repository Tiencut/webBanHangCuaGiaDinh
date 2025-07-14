package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * DiscountRule - Entity quản lý quy tắc giảm giá
 * 
 * Định nghĩa các quy tắc giảm giá khác nhau:
 * - Thương lượng với khách hàng
 * - Giảm giá theo số lượng
 * - Giảm giá cho khách VIP
 * - Giảm giá thanh lý hàng tồn kho
 * 
 * Ví dụ:
 * - Quy tắc thương lượng: Nhân viên được giảm tối đa 10%, Manager 20%
 * - Quy tắc VIP: Khách VIP được giảm 5% tự động
 * - Quy tắc số lượng: Mua từ 10 cái trở lên giảm 3%
 */
@Entity
@Table(name = "discount_rules")
@Data
@EqualsAndHashCode(callSuper = true)
public class DiscountRule extends BaseEntity {
    
    @NotNull
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    /**
     * Loại quy tắc giảm giá
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DiscountType type;
    
    /**
     * Loại giảm giá (phần trăm hoặc số tiền cố định)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountValueType discountType = DiscountValueType.PERCENTAGE;
    
    /**
     * Giá trị giảm giá tối đa (% hoặc số tiền)
     */
    @DecimalMin("0.0")
    @Column(name = "max_discount_value", precision = 19, scale = 2)
    private BigDecimal maxDiscountValue;
    
    /**
     * Giá trị giảm giá tối thiểu (% hoặc số tiền)
     */
    @DecimalMin("0.0")
    @Column(name = "min_discount_value", precision = 19, scale = 2)
    private BigDecimal minDiscountValue = BigDecimal.ZERO;
    
    /**
     * Mức lợi nhuận tối thiểu sau khi giảm giá (%)
     */
    @DecimalMin("0.0")
    @Column(name = "min_profit_margin", precision = 5, scale = 2)
    private BigDecimal minProfitMargin = BigDecimal.valueOf(10.0);
    
    /**
     * Có cần approval không
     */
    @Column(name = "requires_approval")
    private Boolean requiresApproval = false;
    
    /**
     * Giá trị tối đa có thể giảm mà không cần approval
     */
    @DecimalMin("0.0")
    @Column(name = "auto_approval_limit", precision = 19, scale = 2)
    private BigDecimal autoApprovalLimit = BigDecimal.ZERO;
    
    /**
     * Các role được áp dụng quy tắc này
     */
    @ElementCollection
    @CollectionTable(name = "discount_rule_roles", joinColumns = @JoinColumn(name = "discount_rule_id"))
    @Column(name = "role")
    private List<String> applicableRoles;
    
    /**
     * Các loại khách hàng được áp dụng
     */
    @ElementCollection
    @CollectionTable(name = "discount_rule_customer_types", joinColumns = @JoinColumn(name = "discount_rule_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type")
    private List<Customer.CustomerType> applicableCustomerTypes;
    
    /**
     * Các danh mục sản phẩm được áp dụng
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "discount_rule_categories",
        joinColumns = @JoinColumn(name = "discount_rule_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> applicableCategories;
    
    /**
     * Các sản phẩm cụ thể được áp dụng
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "discount_rule_products",
        joinColumns = @JoinColumn(name = "discount_rule_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> applicableProducts;
    
    /**
     * Số lượng tối thiểu để áp dụng (cho volume discount)
     */
    @Column(name = "min_quantity")
    private Integer minQuantity;
    
    /**
     * Giá trị đơn hàng tối thiểu để áp dụng
     */
    @DecimalMin("0.0")
    @Column(name = "min_order_value", precision = 19, scale = 2)
    private BigDecimal minOrderValue = BigDecimal.ZERO;
    
    /**
     * Ngày bắt đầu hiệu lực
     */
    @Column(name = "effective_from")
    private java.time.LocalDateTime effectiveFrom;
    
    /**
     * Ngày kết thúc hiệu lực
     */
    @Column(name = "effective_to")
    private java.time.LocalDateTime effectiveTo;
    
    /**
     * Có đang hoạt động không
     */
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    /**
     * Ưu tiên khi có nhiều quy tắc cùng lúc (số càng nhỏ càng ưu tiên)
     */
    @Column(name = "priority_order")
    private Integer priorityOrder = 999;
    
    /**
     * Ghi chú
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Enum loại quy tắc giảm giá
     */
    public enum DiscountType {
        NEGOTIATION("Thương lượng"),
        VOLUME("Theo số lượng"),
        LOYALTY("Khách VIP"),
        CLEARANCE("Thanh lý"),
        DAMAGE("Hàng lỗi"),
        COMPETITION("Cạnh tranh"),
        SEASONAL("Theo mùa"),
        PROMOTION("Khuyến mãi");

        private final String displayName;

        DiscountType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Enum loại giá trị giảm giá
     */
    public enum DiscountValueType {
        PERCENTAGE("Phần trăm"),
        FIXED_AMOUNT("Số tiền cố định");

        private final String displayName;

        DiscountValueType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Kiểm tra quy tắc có hiệu lực không
     */
    public boolean isEffective() {
        if (!isActive) {
            return false;
        }
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        
        if (effectiveFrom != null && now.isBefore(effectiveFrom)) {
            return false;
        }
        
        if (effectiveTo != null && now.isAfter(effectiveTo)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Kiểm tra role có được áp dụng không
     */
    public boolean isApplicableForRole(String role) {
        return applicableRoles == null || applicableRoles.isEmpty() || 
               applicableRoles.contains(role);
    }
    
    /**
     * Kiểm tra loại khách hàng có được áp dụng không
     */
    public boolean isApplicableForCustomerType(Customer.CustomerType customerType) {
        return applicableCustomerTypes == null || applicableCustomerTypes.isEmpty() || 
               applicableCustomerTypes.contains(customerType);
    }
    
    /**
     * Kiểm tra sản phẩm có được áp dụng không
     */
    public boolean isApplicableForProduct(Product product) {
        // Kiểm tra sản phẩm cụ thể
        if (applicableProducts != null && !applicableProducts.isEmpty()) {
            boolean foundInProducts = applicableProducts.stream()
                .anyMatch(p -> p.getId().equals(product.getId()));
            if (!foundInProducts) {
                return false;
            }
        }
        
        // Kiểm tra danh mục
        if (applicableCategories != null && !applicableCategories.isEmpty()) {
            if (product.getCategory() == null) {
                return false;
            }
            boolean foundInCategories = applicableCategories.stream()
                .anyMatch(cat -> cat.getId().equals(product.getCategory().getId()));
            if (!foundInCategories) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Kiểm tra đơn hàng có đủ điều kiện không
     */
    public boolean isApplicableForOrder(Integer quantity, BigDecimal orderValue) {
        if (minQuantity != null && quantity < minQuantity) {
            return false;
        }
        
        if (minOrderValue != null && orderValue.compareTo(minOrderValue) < 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Tính giá trị giảm giá tối đa có thể áp dụng
     */
    public BigDecimal calculateMaxDiscount(BigDecimal originalPrice) {
        if (discountType == DiscountValueType.PERCENTAGE) {
            return originalPrice.multiply(maxDiscountValue)
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return maxDiscountValue;
        }
    }
    
    /**
     * Kiểm tra có cần approval cho giá trị giảm giá này không
     */
    public boolean needsApproval(BigDecimal discountValue) {
        if (!requiresApproval) {
            return false;
        }
        
        return discountValue.compareTo(autoApprovalLimit) > 0;
    }
} 