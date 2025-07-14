package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Inventory - Entity quản lý tồn kho theo supplier
 * 
 * Theo dõi chi tiết tồn kho của từng sản phẩm theo từng nhà cung cấp
 * Bao gồm:
 * - Số lượng tồn kho
 * - Giá trị tồn kho
 * - Lịch sử nhập/xuất
 * - Cảnh báo tồn kho
 * 
 * Ví dụ:
 * - Má phanh Hino J05E:
 *   + NCC A: 20 cái, giá trị 9,000,000đ
 *   + NCC B: 15 cái, giá trị 6,300,000đ
 *   + NCC C: 8 cái, giá trị 3,840,000đ
 *   + Tổng: 43 cái, giá trị 19,140,000đ
 */
@Entity
@Table(name = "inventory")
@Data
@EqualsAndHashCode(callSuper = true)
public class Inventory extends BaseEntity {
    
    /**
     * Sản phẩm
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    /**
     * Nhà cung cấp
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    
    /**
     * Số lượng tồn kho hiện tại
     */
    @Column(name = "current_quantity")
    private Integer currentQuantity = 0;
    
    /**
     * Số lượng đã đặt hàng (chưa nhập)
     */
    @Column(name = "ordered_quantity")
    private Integer orderedQuantity = 0;
    
    /**
     * Số lượng đã cam kết (đã bán nhưng chưa xuất)
     */
    @Column(name = "committed_quantity")
    private Integer committedQuantity = 0;
    
    /**
     * Số lượng có sẵn để bán
     */
    @Column(name = "available_quantity")
    private Integer availableQuantity = 0;
    
    /**
     * Giá nhập trung bình
     */
    @Column(name = "average_cost", precision = 19, scale = 2)
    private BigDecimal averageCost = BigDecimal.ZERO;
    
    /**
     * Giá trị tồn kho
     */
    @Column(name = "inventory_value", precision = 19, scale = 2)
    private BigDecimal inventoryValue = BigDecimal.ZERO;
    
    /**
     * Mức tồn kho tối thiểu
     */
    @Column(name = "min_stock_level")
    private Integer minStockLevel = 5;
    
    /**
     * Điểm đặt hàng lại
     */
    @Column(name = "reorder_point")
    private Integer reorderPoint = 10;
    
    /**
     * Số lượng đặt hàng tối ưu
     */
    @Column(name = "optimal_order_quantity")
    private Integer optimalOrderQuantity = 50;
    
    /**
     * Ngày nhập hàng cuối cùng
     */
    @Column(name = "last_received_date")
    private LocalDateTime lastReceivedDate;
    
    /**
     * Ngày xuất hàng cuối cùng
     */
    @Column(name = "last_shipped_date")
    private LocalDateTime lastShippedDate;
    
    /**
     * Số lần nhập hàng trong tháng
     */
    @Column(name = "receipts_this_month")
    private Integer receiptsThisMonth = 0;
    
    /**
     * Số lần xuất hàng trong tháng
     */
    @Column(name = "shipments_this_month")
    private Integer shipmentsThisMonth = 0;
    
    /**
     * Tổng số lượng nhập trong tháng
     */
    @Column(name = "total_received_this_month")
    private Integer totalReceivedThisMonth = 0;
    
    /**
     * Tổng số lượng xuất trong tháng
     */
    @Column(name = "total_shipped_this_month")
    private Integer totalShippedThisMonth = 0;
    
    /**
     * Vòng quay tồn kho (tháng)
     */
    @Column(name = "turnover_rate")
    private Double turnoverRate = 0.0;
    
    /**
     * Số ngày tồn kho trung bình
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
     * Ghi chú
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Enum trạng thái tồn kho
     */
    public enum StockStatus {
        OUT_OF_STOCK("Hết hàng"),
        LOW_STOCK("Sắp hết"),
        NORMAL("Bình thường"),
        HIGH_STOCK("Tồn kho cao"),
        OVERSTOCK("Tồn kho quá nhiều");

        private final String displayName;

        StockStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Cập nhật số lượng có sẵn
     */
    public void updateAvailableQuantity() {
        this.availableQuantity = Math.max(0, currentQuantity - committedQuantity);
    }
    
    /**
     * Cập nhật giá trị tồn kho
     */
    public void updateInventoryValue() {
        this.inventoryValue = averageCost.multiply(BigDecimal.valueOf(currentQuantity));
    }
    
    /**
     * Cập nhật trạng thái tồn kho
     */
    public void updateStockStatus() {
        if (currentQuantity <= 0) {
            this.stockStatus = StockStatus.OUT_OF_STOCK;
        } else if (currentQuantity <= minStockLevel) {
            this.stockStatus = StockStatus.LOW_STOCK;
        } else if (currentQuantity <= reorderPoint) {
            this.stockStatus = StockStatus.NORMAL;
        } else if (currentQuantity <= reorderPoint * 2) {
            this.stockStatus = StockStatus.HIGH_STOCK;
        } else {
            this.stockStatus = StockStatus.OVERSTOCK;
        }
    }
    
    /**
     * Kiểm tra có cần đặt hàng không
     */
    public boolean needsReorder() {
        return currentQuantity <= reorderPoint;
    }
    
    /**
     * Kiểm tra có hết hàng không
     */
    public boolean isOutOfStock() {
        return currentQuantity <= 0;
    }
    
    /**
     * Kiểm tra có sắp hết hàng không
     */
    public boolean isLowStock() {
        return currentQuantity <= minStockLevel;
    }
    
    /**
     * Tính số lượng cần đặt hàng
     */
    public Integer calculateReorderQuantity() {
        if (currentQuantity >= reorderPoint) {
            return 0;
        }
        return Math.max(optimalOrderQuantity, reorderPoint - currentQuantity);
    }
    
    /**
     * Cập nhật sau khi nhập hàng
     */
    public void updateAfterReceipt(Integer quantity, BigDecimal cost) {
        // Cập nhật số lượng
        this.currentQuantity += quantity;
        this.orderedQuantity = Math.max(0, this.orderedQuantity - quantity);
        
        // Cập nhật giá trung bình
        if (currentQuantity > 0) {
            BigDecimal totalValue = averageCost.multiply(BigDecimal.valueOf(currentQuantity - quantity))
                    .add(cost.multiply(BigDecimal.valueOf(quantity)));
            this.averageCost = totalValue.divide(BigDecimal.valueOf(currentQuantity), 2, BigDecimal.ROUND_HALF_UP);
        }
        
        // Cập nhật thống kê
        this.receiptsThisMonth++;
        this.totalReceivedThisMonth += quantity;
        this.lastReceivedDate = LocalDateTime.now();
        
        // Cập nhật các giá trị khác
        updateAvailableQuantity();
        updateInventoryValue();
        updateStockStatus();
    }
    
    /**
     * Cập nhật sau khi xuất hàng
     */
    public void updateAfterShipment(Integer quantity) {
        // Cập nhật số lượng
        this.currentQuantity = Math.max(0, this.currentQuantity - quantity);
        this.committedQuantity = Math.max(0, this.committedQuantity - quantity);
        
        // Cập nhật thống kê
        this.shipmentsThisMonth++;
        this.totalShippedThisMonth += quantity;
        this.lastShippedDate = LocalDateTime.now();
        
        // Cập nhật các giá trị khác
        updateAvailableQuantity();
        updateInventoryValue();
        updateStockStatus();
    }
    
    /**
     * Cập nhật sau khi cam kết bán
     */
    public void updateAfterCommit(Integer quantity) {
        this.committedQuantity += quantity;
        updateAvailableQuantity();
    }
    
    /**
     * Tính vòng quay tồn kho
     */
    public void calculateTurnoverRate() {
        if (currentQuantity > 0 && totalShippedThisMonth > 0) {
            this.turnoverRate = (double) totalShippedThisMonth / currentQuantity;
            this.averageDaysInStock = (int) (30.0 / turnoverRate);
        }
    }
    
    /**
     * Reset thống kê tháng
     */
    public void resetMonthlyStats() {
        this.receiptsThisMonth = 0;
        this.shipmentsThisMonth = 0;
        this.totalReceivedThisMonth = 0;
        this.totalShippedThisMonth = 0;
    }
}
