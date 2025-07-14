package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * VoiceCommand - Entity log lệnh giọng nói
 * 
 * Lưu trữ thông tin về các lệnh giọng nói được thực hiện
 * Dùng để:
 * - Phân tích hiệu quả của voice recognition
 * - Cải thiện độ chính xác
 * - Theo dõi pattern sử dụng
 * - Debug khi có lỗi
 * 
 * Ví dụ:
 * - "Tạo đơn hàng cho garage Minh Tâm, má phanh Hino 2 cái"
 * - "Kiểm tra tồn kho má phanh"
 * - "Gọi khách hàng A"
 */
@Entity
@Table(name = "voice_commands")
@Data
@EqualsAndHashCode(callSuper = true)
public class VoiceCommand extends BaseEntity {
    
    /**
     * Người dùng thực hiện lệnh
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    /**
     * Nội dung giọng nói gốc (transcript)
     */
    @Column(name = "transcript", columnDefinition = "TEXT")
    private String transcript;
    
    /**
     * Độ tin cậy của speech recognition (0.0 - 1.0)
     */
    @Column(name = "confidence")
    private Double confidence;
    
    /**
     * Ý định được nhận diện
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "intent")
    private VoiceIntent intent;
    
    /**
     * Các entity được trích xuất (JSON)
     */
    @Column(name = "entities", columnDefinition = "TEXT")
    private String entities;
    
    /**
     * Lệnh có thành công không
     */
    @Column(name = "success")
    private Boolean success = false;
    
    /**
     * Thời gian xử lý (milliseconds)
     */
    @Column(name = "processing_time_ms")
    private Long processingTimeMs;
    
    /**
     * Engine được sử dụng
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "speech_engine")
    private SpeechEngine speechEngine;
    
    /**
     * Ngôn ngữ được sử dụng
     */
    @Column(name = "language")
    private String language = "vi-VN";
    
    /**
     * Thiết bị sử dụng
     */
    @Column(name = "device_type")
    private String deviceType; // "mobile", "web", "tablet"
    
    /**
     * Lỗi nếu có
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * Kết quả thực hiện (JSON)
     */
    @Column(name = "result", columnDefinition = "TEXT")
    private String result;
    
    /**
     * Thời gian thực hiện
     */
    @Column(name = "executed_at")
    private LocalDateTime executedAt;
    
    /**
     * IP address của người dùng
     */
    @Column(name = "ip_address")
    private String ipAddress;
    
    /**
     * User agent
     */
    @Column(name = "user_agent")
    private String userAgent;
    
    /**
     * Session ID
     */
    @Column(name = "session_id")
    private String sessionId;
    
    /**
     * Ghi chú
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Enum ý định giọng nói
     */
    public enum VoiceIntent {
        CREATE_ORDER("Tạo đơn hàng"),
        CHECK_STOCK("Kiểm tra tồn kho"),
        SEARCH_PRODUCT("Tìm kiếm sản phẩm"),
        CALL_CUSTOMER("Gọi khách hàng"),
        CHECK_PRICE("Kiểm tra giá"),
        ADD_PRODUCT("Thêm sản phẩm"),
        REMOVE_PRODUCT("Xóa sản phẩm"),
        UPDATE_QUANTITY("Cập nhật số lượng"),
        APPLY_DISCOUNT("Áp dụng giảm giá"),
        CONFIRM_ORDER("Xác nhận đơn hàng"),
        CANCEL_ORDER("Hủy đơn hàng"),
        NAVIGATE("Điều hướng"),
        HELP("Trợ giúp"),
        UNKNOWN("Không xác định");

        private final String displayName;

        VoiceIntent(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Enum engine speech recognition
     */
    public enum SpeechEngine {
        GOOGLE_CLOUD("Google Cloud Speech-to-Text"),
        FPT_AI("FPT.AI Speech"),
        AZURE("Azure Speech Services"),
        AWS_TRANSCRIBE("AWS Transcribe"),
        LOCAL("Local Speech Recognition");

        private final String displayName;

        SpeechEngine(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Kiểm tra độ tin cậy có đủ cao không
     */
    public boolean isConfident() {
        return confidence != null && confidence >= 0.8;
    }
    
    /**
     * Kiểm tra có cần xác nhận không
     */
    public boolean needsConfirmation() {
        return confidence != null && confidence >= 0.6 && confidence < 0.8;
    }
    
    /**
     * Kiểm tra có cần nhập lại không
     */
    public boolean needsRepetition() {
        return confidence == null || confidence < 0.6;
    }
    
    /**
     * Tính thời gian xử lý trung bình
     */
    public boolean isSlowProcessing() {
        return processingTimeMs != null && processingTimeMs > 3000; // > 3 giây
    }
    
    /**
     * Kiểm tra có phải lệnh tạo đơn hàng không
     */
    public boolean isOrderCreation() {
        return intent == VoiceIntent.CREATE_ORDER;
    }
    
    /**
     * Kiểm tra có phải lệnh kiểm tra tồn kho không
     */
    public boolean isStockCheck() {
        return intent == VoiceIntent.CHECK_STOCK;
    }
    
    /**
     * Tạo log entry cho voice command
     */
    public static VoiceCommand createLog(User user, String transcript, Double confidence, 
                                       VoiceIntent intent, String entities, Boolean success) {
        VoiceCommand command = new VoiceCommand();
        command.setUser(user);
        command.setTranscript(transcript);
        command.setConfidence(confidence);
        command.setIntent(intent);
        command.setEntities(entities);
        command.setSuccess(success);
        command.setExecutedAt(LocalDateTime.now());
        return command;
    }
    
    /**
     * Cập nhật kết quả xử lý
     */
    public void updateResult(String result, Long processingTimeMs, String errorMessage) {
        this.result = result;
        this.processingTimeMs = processingTimeMs;
        this.errorMessage = errorMessage;
        this.success = errorMessage == null || errorMessage.isEmpty();
    }
} 