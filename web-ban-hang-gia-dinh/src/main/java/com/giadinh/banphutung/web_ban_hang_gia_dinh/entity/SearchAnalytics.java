package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SearchAnalytics - Theo dõi hành vi tìm kiếm và học tập của nhân viên
 */
@Entity
@Table(name = "search_analytics")
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchAnalytics extends BaseEntity {

    /**
     * Nhân viên thực hiện tìm kiếm
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Từ khóa tìm kiếm
     */
    @Column(name = "search_query", length = 500)
    private String searchQuery;

    /**
     * Loại tìm kiếm
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "search_type")
    private SearchType searchType;

    /**
     * Có tìm thấy kết quả không
     */
    @Column(name = "result_found")
    private Boolean resultFound = false;

    /**
     * Số kết quả trả về
     */
    @Column(name = "result_count")
    private Integer resultCount = 0;

    /**
     * Thời gian tìm kiếm (giây)
     */
    @Column(name = "time_to_find")
    private Integer timeToFind;

    /**
     * Có yêu cầu hỗ trợ không
     */
    @Column(name = "asked_for_help")
    private Boolean askedForHelp = false;

    /**
     * Có click vào training content không
     */
    @Column(name = "used_training_content")
    private Boolean usedTrainingContent = false;

    /**
     * Sản phẩm cuối cùng được chọn
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_product_id")
    private Product selectedProduct;

    /**
     * Ghi chú về session tìm kiếm
     */
    @Column(name = "session_notes", columnDefinition = "TEXT")
    private String sessionNotes;

    public enum SearchType {
        PRODUCT_SEARCH("Tìm sản phẩm"),
        TRAINING_SEARCH("Tìm tài liệu học"),
        HELP_SEARCH("Tìm trợ giúp"),
        VEHICLE_SEARCH("Tìm thông tin xe");

        private final String displayName;

        SearchType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
