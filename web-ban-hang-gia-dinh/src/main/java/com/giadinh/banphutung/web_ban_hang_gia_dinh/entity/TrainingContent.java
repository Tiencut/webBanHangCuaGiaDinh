package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TrainingContent - Nội dung đào tạo cho nhân viên
 * 
 * Chứa kiến thức về nhận diện phụ tùng, cách phân biệt hàng thật/giả,
 * tips bán hàng, thông tin kỹ thuật...
 */
@Entity
@Table(name = "training_content")
@Data
@EqualsAndHashCode(callSuper = true)
public class TrainingContent extends BaseEntity {

    /**
     * Tiêu đề bài học
     */
    @NotBlank
    @Column(name = "title", length = 300)
    private String title;

    /**
     * Loại nội dung đào tạo
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private TrainingCategory category;

    /**
     * Nội dung chính (HTML/Markdown)
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * Mô tả ngắn gọn
     */
    @Column(name = "summary", length = 500)
    private String summary;

    /**
     * Hình ảnh minh họa (JSON array)
     */
    @Column(name = "images", columnDefinition = "TEXT")
    private String images; // JSON: ["url1", "url2", ...]

    /**
     * Video hướng dẫn
     */
    @Column(name = "video_url")
    private String videoUrl;

    /**
     * Tags để tìm kiếm
     */
    @Column(name = "tags", length = 1000)
    private String tags; // "hyundai,brake,hd65,recognition"

    /**
     * Mức độ ưu tiên
     */
    @NotNull
    @Column(name = "priority")
    private Integer priority = 3; // 1=Critical, 2=Important, 3=Nice-to-know

    /**
     * Thời gian đọc ước tính (phút)
     */
    @Column(name = "estimated_read_time")
    private Integer estimatedReadTime;

    /**
     * Có hiển thị trong quick help không
     */
    @Column(name = "show_in_quick_help")
    private Boolean showInQuickHelp = false;

    /**
     * Sản phẩm liên quan
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "training_content_products",
        joinColumns = @JoinColumn(name = "training_content_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> relatedProducts = new ArrayList<>();

    /**
     * Mẫu xe liên quan
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "training_content_vehicles",
        joinColumns = @JoinColumn(name = "training_content_id"),
        inverseJoinColumns = @JoinColumn(name = "vehicle_model_id")
    )
    private List<VehicleModel> relatedVehicles = new ArrayList<>();

    /**
     * Trạng thái active
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * Enum loại nội dung đào tạo
     */
    public enum TrainingCategory {
        PRODUCT_IDENTIFICATION("Nhận diện sản phẩm"),
        VEHICLE_KNOWLEDGE("Kiến thức về xe"),
        SALES_TIPS("Kỹ thuật bán hàng"),
        TECHNICAL_SPECS("Thông số kỹ thuật"),
        COMMON_MISTAKES("Lỗi thường gặp"),
        QUALITY_CHECK("Kiểm tra chất lượng"),
        CUSTOMER_SERVICE("Phục vụ khách hàng"),
        PRICING_GUIDE("Hướng dẫn báo giá");

        private final String displayName;

        TrainingCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Utility methods
    public List<String> getImageList() {
        if (images == null || images.isEmpty()) {
            return new ArrayList<>();
        }
        // Parse JSON array (simplified)
        return List.of(images.replaceAll("[\\[\\]\"]", "").split(","));
    }

    public List<String> getTagList() {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        return List.of(tags.split(","));
    }
}
