package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ProductSupplier - Entity quản lý quan hệ Product-Supplier
 * 
 * Một sản phẩm có thể được cung cấp bởi nhiều nhà cung cấp khác nhau
 * Mỗi nguồn cung có giá nhập, giá bán và tồn kho riêng biệt
 * 
 * Ví dụ:
 * - Má phanh Hino J05E có thể mua từ:
 *   + NCC A: Giá nhập 300k, giá bán 450k, tồn kho 20 cái
 *   + NCC B: Giá nhập 280k, giá bán 420k, tồn kho 15 cái
 *   + NCC C: Giá nhập 320k, giá bán 480k, tồn kho 8 cái
 */
@Entity
@Table(name = "product_suppliers")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSupplier extends BaseEntity {
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    
    /**
     * Giá nhập từ nhà cung cấp này
     */
    @NotNull
    @DecimalMin("0.0")
    @Column(name = "cost_price", precision = 19, scale = 2)
    private BigDecimal costPrice;
    
    /**
     * Giá bán từ nguồn cung này
     */
    @NotNull
    @DecimalMin("0.0")
    @Column(name = "selling_price", precision = 19, scale = 2)
    private BigDecimal sellingPrice;
    
    /**
     * Tồn kho từ nguồn cung này
     */
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;
    
    /**
     * Tồn kho hiện tại (alias cho stockQuantity)
     */
    @Column(name = "current_quantity")
    private Integer currentQuantity = 0;
    
    /**
     * Mức tồn kho tối thiểu cho nguồn cung này
     */
    @Column(name = "min_stock_level")
    private Integer minStockLevel = 5;
    
    /**
     * Mức tồn kho tối đa cho nguồn cung này
     */
    @Column(name = "max_stock_level")
    private Integer maxStockLevel = 100;
    
    /**
     * Điểm đặt hàng lại cho nguồn cung này
     */
    @Column(name = "reorder_point")
    private Integer reorderPoint = 10;
    
    /**
     * Thời gian giao hàng trung bình (ngày)
     */
    @Column(name = "delivery_time_days")
    private Integer deliveryTimeDays = 7;
    
    /**
     * Chất lượng sản phẩm từ nguồn cung này (1-5 sao)
     */
    @Column(name = "quality_rating")
    private Double qualityRating = 0.0;
    
    /**
     * Độ tin cậy của nguồn cung này (1-5 sao)
     */
    @Column(name = "reliability_rating")
    private Double reliabilityRating = 0.0;
    
    /**
     * Ưu tiên khi chọn nguồn cung (số càng nhỏ càng ưu tiên)
     */
    @Column(name = "priority_order")
    private Integer priorityOrder = 999;
    
    /**
     * Tỷ lệ luân chuyển hàng tồn kho
     */
    @Column(name = "turnover_rate")
    private Double turnoverRate = 0.0;
    
    /**
     * Số ngày trung bình hàng tồn trong kho
     */
    @Column(name = "average_days_in_stock")
    private Integer averageDaysInStock = 0;
    
    /**
     * Trạng thái tồn kho
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status")
    private StockStatus stockStatus = StockStatus.NORMAL;
    
    /**
     * Có đang hoạt động không
     */
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    /**
     * Ngày nhập hàng cuối cùng
     */
    @Column(name = "last_purchase_date")
    private LocalDateTime lastPurchaseDate;
    
    /**
     * Số lượng đã bán từ nguồn cung này
     */
    @Column(name = "sold_quantity")
    private Integer soldQuantity = 0;
    
    /**
     * Ghi chú về nguồn cung này
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    public enum StockStatus {
        NORMAL, LOW, OUT_OF_STOCK, OVERSTOCKED
    }
    
    /**
     * Tính lợi nhuận từ nguồn cung này
     */
    public BigDecimal getProfitAmount() {
        return sellingPrice.subtract(costPrice);
    }
    
    /**
     * Tính % lợi nhuận
     */
    public BigDecimal getProfitMargin() {
        if (sellingPrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return getProfitAmount().divide(sellingPrice, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
    
    /**
     * Kiểm tra có cần nhập thêm hàng không
     */
    public boolean needsReorder() {
        return stockQuantity <= reorderPoint;
    }
    
    /**
     * Kiểm tra có hết hàng không
     */
    public boolean isOutOfStock() {
        return stockQuantity <= 0;
    }
    
    /**
     * Kiểm tra có sắp hết hàng không
     */
    public boolean isLowStock() {
        return stockQuantity <= minStockLevel;
    }
    
    /**
     * Cập nhật tồn kho sau khi bán
     */
    public void updateStockAfterSale(Integer quantity) {
        this.stockQuantity = Math.max(0, this.stockQuantity - quantity);
        this.currentQuantity = this.stockQuantity;
        this.soldQuantity += quantity;
    }
    
    /**
     * Cập nhật tồn kho sau khi nhập
     */
    public void updateStockAfterPurchase(Integer quantity) {
        this.stockQuantity += quantity;
        this.currentQuantity = this.stockQuantity;
        this.lastPurchaseDate = LocalDateTime.now();
    }
}
