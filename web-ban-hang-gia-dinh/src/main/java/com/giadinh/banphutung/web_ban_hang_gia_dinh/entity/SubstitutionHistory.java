package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SubstitutionHistory - Entity theo dõi lịch sử thay thế linh kiện
 * 
 * Lưu trữ thông tin về việc thay thế linh kiện trên xe của khách hàng
 * Dùng để:
 * - Gợi ý linh kiện phù hợp dựa trên lịch sử
 * - Đánh giá chất lượng linh kiện
 * - Dự báo nhu cầu thay thế
 * - Phân tích hiệu quả của từng loại linh kiện
 * 
 * Ví dụ:
 * - Xe 51A-12345 đã thay:
 *   + Má phanh Bendix (2023-01-15) - Đánh giá 5⭐ - Dùng được 18 tháng
 *   + Dầu phanh Shell (2023-06-20) - Đánh giá 4⭐ - Dùng được 12 tháng
 *   + Gasket nắp quy lát (2024-01-10) - Đánh giá 3⭐ - Dùng được 8 tháng
 */
@Entity
@Table(name = "substitution_history")
@Data
@EqualsAndHashCode(callSuper = true)
public class SubstitutionHistory extends BaseEntity {
    
    /**
     * Xe của khách hàng
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_vehicle_id")
    private CustomerVehicle customerVehicle;
    
    /**
     * Sản phẩm đã thay thế
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    /**
     * Nhà cung cấp của sản phẩm
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    
    /**
     * Ngày thay thế
     */
    @Column(name = "replaced_at")
    private LocalDateTime replacedAt;
    
    /**
     * Số km xe khi thay thế
     */
    @Column(name = "mileage_at_replacement")
    private Integer mileageAtReplacement;
    
    /**
     * Số km xe hiện tại (để tính thời gian sử dụng)
     */
    @Column(name = "current_mileage")
    private Integer currentMileage;
    
    /**
     * Đánh giá chất lượng (1-5 sao)
     */
    @Column(name = "rating")
    private Integer rating;
    
    /**
     * Phản hồi của khách hàng
     */
    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;
    
    /**
     * Thời gian sử dụng (tháng)
     */
    @Column(name = "usage_duration_months")
    private Integer usageDurationMonths;
    
    /**
     * Số km đã chạy với linh kiện này
     */
    @Column(name = "usage_mileage")
    private Integer usageMileage;
    
    /**
     * Giá mua linh kiện
     */
    @Column(name = "purchase_price", precision = 19, scale = 2)
    private BigDecimal purchasePrice;
    
    /**
     * Chi phí thay thế (bao gồm nhân công)
     */
    @Column(name = "replacement_cost", precision = 19, scale = 2)
    private BigDecimal replacementCost;
    
    /**
     * Lý do thay thế
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "replacement_reason")
    private ReplacementReason replacementReason;
    
    /**
     * Trạng thái linh kiện cũ
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "old_part_condition")
    private PartCondition oldPartCondition;
    
    /**
     * Ghi chú kỹ thuật
     */
    @Column(name = "technical_notes", columnDefinition = "TEXT")
    private String technicalNotes;
    
    /**
     * Có bảo hành không
     */
    @Column(name = "has_warranty")
    private Boolean hasWarranty = false;
    
    /**
     * Ngày hết hạn bảo hành
     */
    @Column(name = "warranty_expiry_date")
    private LocalDateTime warrantyExpiryDate;
    
    /**
     * Có claim bảo hành không
     */
    @Column(name = "warranty_claimed")
    private Boolean warrantyClaimed = false;
    
    /**
     * Ngày claim bảo hành
     */
    @Column(name = "warranty_claim_date")
    private LocalDateTime warrantyClaimDate;
    
    /**
     * Lý do claim bảo hành
     */
    @Column(name = "warranty_claim_reason")
    private String warrantyClaimReason;
    
    /**
     * Kết quả claim bảo hành
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "warranty_claim_result")
    private WarrantyClaimResult warrantyClaimResult;
    
    /**
     * Enum lý do thay thế
     */
    public enum ReplacementReason {
        MAINTENANCE("Bảo dưỡng định kỳ"),
        WEAR_OUT("Mòn hỏng"),
        DAMAGE("Hư hỏng"),
        UPGRADE("Nâng cấp"),
        PREVENTIVE("Thay thế phòng ngừa"),
        WARRANTY("Bảo hành"),
        ACCIDENT("Tai nạn"),
        OTHER("Khác");

        private final String displayName;

        ReplacementReason(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Enum tình trạng linh kiện cũ
     */
    public enum PartCondition {
        EXCELLENT("Xuất sắc"),
        GOOD("Tốt"),
        FAIR("Khá"),
        POOR("Kém"),
        DAMAGED("Hư hỏng"),
        WORN_OUT("Mòn hỏng");

        private final String displayName;

        PartCondition(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Enum kết quả claim bảo hành
     */
    public enum WarrantyClaimResult {
        APPROVED("Được chấp nhận"),
        REJECTED("Từ chối"),
        PARTIAL("Chấp nhận một phần"),
        PENDING("Đang xử lý");

        private final String displayName;

        WarrantyClaimResult(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Tính thời gian sử dụng (tháng)
     */
    public Integer calculateUsageDuration() {
        if (replacedAt == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        long months = java.time.temporal.ChronoUnit.MONTHS.between(replacedAt, now);
        return (int) months;
    }
    
    /**
     * Tính số km đã sử dụng
     */
    public Integer calculateUsageMileage() {
        if (mileageAtReplacement == null || currentMileage == null) {
            return null;
        }
        return Math.max(0, currentMileage - mileageAtReplacement);
    }
    
    /**
     * Kiểm tra có đang bảo hành không
     */
    public boolean isUnderWarranty() {
        return hasWarranty && warrantyExpiryDate != null && 
               warrantyExpiryDate.isAfter(LocalDateTime.now());
    }
    
    /**
     * Tính chi phí/km sử dụng
     */
    public BigDecimal getCostPerMileage() {
        if (usageMileage == null || usageMileage == 0 || replacementCost == null) {
            return BigDecimal.ZERO;
        }
        return replacementCost.divide(BigDecimal.valueOf(usageMileage), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Đánh giá hiệu quả kinh tế (1-5 sao)
     */
    public Integer getEconomicRating() {
        if (rating == null || usageDurationMonths == null || usageDurationMonths == 0) {
            return null;
        }
        
        // Tính điểm dựa trên thời gian sử dụng và đánh giá
        double score = (rating * usageDurationMonths) / 12.0; // Chuẩn hóa về năm
        
        if (score >= 4.0) return 5;
        else if (score >= 3.0) return 4;
        else if (score >= 2.0) return 3;
        else if (score >= 1.0) return 2;
        else return 1;
    }
    
    /**
     * Cập nhật thông tin sử dụng
     */
    public void updateUsageInfo(Integer newMileage) {
        this.currentMileage = newMileage;
        this.usageMileage = calculateUsageMileage();
        this.usageDurationMonths = calculateUsageDuration();
    }
} 