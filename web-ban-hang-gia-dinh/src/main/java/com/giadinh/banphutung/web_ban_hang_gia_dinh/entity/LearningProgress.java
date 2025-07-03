package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * LearningProgress - Theo dõi tiến độ học tập của nhân viên
 */
@Entity
@Table(name = "learning_progress")
@Data
@EqualsAndHashCode(callSuper = true)
public class LearningProgress extends BaseEntity {

    /**
     * Nhân viên
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Nội dung đào tạo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_content_id", nullable = false)
    private TrainingContent trainingContent;

    /**
     * Đã xem chưa
     */
    @Column(name = "viewed")
    private Boolean viewed = false;

    /**
     * Đã hiểu chưa (user click "Đã hiểu")
     */
    @Column(name = "understood")
    private Boolean understood = false;

    /**
     * Đánh dấu là hữu ích
     */
    @Column(name = "marked_helpful")
    private Boolean markedHelpful = false;

    /**
     * Thời gian đọc thực tế (giây)
     */
    @Column(name = "actual_read_time")
    private Integer actualReadTime;

    /**
     * Ghi chú của nhân viên
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Số lần xem
     */
    @Column(name = "view_count")
    private Integer viewCount = 0;
}
