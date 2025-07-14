package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VoiceCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * VoiceCommandRepository - Repository cho log lệnh giọng nói
 * 
 * Cung cấp các query methods để:
 * - Phân tích hiệu quả voice recognition
 * - Debug và cải thiện độ chính xác
 * - Theo dõi pattern sử dụng
 * - Analytics cho voice features
 */
@Repository
public interface VoiceCommandRepository extends JpaRepository<VoiceCommand, Long> {
    
    /**
     * Tìm lệnh giọng nói của một user
     */
    List<VoiceCommand> findByUserIdOrderByExecutedAtDesc(Long userId);
    
    /**
     * Tìm lệnh giọng nói theo intent
     */
    List<VoiceCommand> findByIntentOrderByExecutedAtDesc(VoiceCommand.VoiceIntent intent);
    
    /**
     * Tìm lệnh giọng nói thành công
     */
    List<VoiceCommand> findBySuccessTrueOrderByExecutedAtDesc();
    
    /**
     * Tìm lệnh giọng nói thất bại
     */
    List<VoiceCommand> findBySuccessFalseOrderByExecutedAtDesc();
    
    /**
     * Tìm lệnh giọng nói theo độ tin cậy
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.confidence >= :minConfidence " +
           "ORDER BY vc.confidence DESC")
    List<VoiceCommand> findByConfidenceGreaterThan(@Param("minConfidence") Double minConfidence);
    
    /**
     * Tìm lệnh giọng nói có độ tin cậy thấp
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.confidence < :maxConfidence " +
           "ORDER BY vc.confidence ASC")
    List<VoiceCommand> findByConfidenceLessThan(@Param("maxConfidence") Double maxConfidence);
    
    /**
     * Tìm lệnh giọng nói theo khoảng độ tin cậy
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.confidence BETWEEN :minConfidence AND :maxConfidence " +
           "ORDER BY vc.confidence DESC")
    List<VoiceCommand> findByConfidenceBetween(@Param("minConfidence") Double minConfidence, 
                                              @Param("maxConfidence") Double maxConfidence);
    
    /**
     * Tìm lệnh giọng nói theo speech engine
     */
    List<VoiceCommand> findBySpeechEngineOrderByExecutedAtDesc(VoiceCommand.SpeechEngine speechEngine);
    
    /**
     * Tìm lệnh giọng nói theo ngôn ngữ
     */
    List<VoiceCommand> findByLanguageOrderByExecutedAtDesc(String language);
    
    /**
     * Tìm lệnh giọng nói theo device type
     */
    List<VoiceCommand> findByDeviceTypeOrderByExecutedAtDesc(String deviceType);
    
    /**
     * Tìm lệnh giọng nói theo khoảng thời gian
     */
    List<VoiceCommand> findByExecutedAtBetweenOrderByExecutedAtDesc(LocalDateTime fromDate, LocalDateTime toDate);
    
    /**
     * Tìm lệnh giọng nói theo thời gian xử lý
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.processingTimeMs <= :maxProcessingTime " +
           "ORDER BY vc.processingTimeMs ASC")
    List<VoiceCommand> findByProcessingTimeLessThan(@Param("maxProcessingTime") Long maxProcessingTime);
    
    /**
     * Tìm lệnh giọng nói có thời gian xử lý chậm
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.processingTimeMs > :minProcessingTime " +
           "ORDER BY vc.processingTimeMs DESC")
    List<VoiceCommand> findByProcessingTimeGreaterThan(@Param("minProcessingTime") Long minProcessingTime);
    
    /**
     * Tìm lệnh giọng nói theo session
     */
    List<VoiceCommand> findBySessionIdOrderByExecutedAtDesc(String sessionId);
    
    /**
     * Tìm lệnh giọng nói theo IP address
     */
    List<VoiceCommand> findByIpAddressOrderByExecutedAtDesc(String ipAddress);
    
    /**
     * Tìm lệnh giọng nói có lỗi
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.errorMessage IS NOT NULL " +
           "ORDER BY vc.executedAt DESC")
    List<VoiceCommand> findCommandsWithErrors();
    
    /**
     * Tìm lệnh giọng nói theo loại lỗi
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.errorMessage LIKE CONCAT('%', :errorType, '%') " +
           "ORDER BY vc.executedAt DESC")
    List<VoiceCommand> findByErrorType(@Param("errorType") String errorType);
    
    /**
     * Tìm lệnh giọng nói theo transcript (fuzzy search)
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE LOWER(vc.transcript) LIKE LOWER(CONCAT('%', :transcript, '%')) " +
           "ORDER BY vc.executedAt DESC")
    List<VoiceCommand> findByTranscriptContainingIgnoreCase(@Param("transcript") String transcript);
    
    /**
     * Tìm lệnh giọng nói theo entities (fuzzy search)
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE LOWER(vc.entities) LIKE LOWER(CONCAT('%', :entities, '%')) " +
           "ORDER BY vc.executedAt DESC")
    List<VoiceCommand> findByEntitiesContainingIgnoreCase(@Param("entities") String entities);
    
    /**
     * Tìm lệnh giọng nói theo result (fuzzy search)
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE LOWER(vc.result) LIKE LOWER(CONCAT('%', :result, '%')) " +
           "ORDER BY vc.executedAt DESC")
    List<VoiceCommand> findByResultContainingIgnoreCase(@Param("result") String result);
    
    /**
     * Tìm lệnh giọng nói theo user agent
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE LOWER(vc.userAgent) LIKE LOWER(CONCAT('%', :userAgent, '%')) " +
           "ORDER BY vc.executedAt DESC")
    List<VoiceCommand> findByUserAgentContainingIgnoreCase(@Param("userAgent") String userAgent);
    
    /**
     * Tìm lệnh giọng nói gần đây
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.executedAt >= :fromDate " +
           "ORDER BY vc.executedAt DESC")
    List<VoiceCommand> findRecentCommands(@Param("fromDate") LocalDateTime fromDate);
    
    /**
     * Tìm lệnh giọng nói có thời gian xử lý nhanh nhất
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.processingTimeMs > 0 " +
           "ORDER BY vc.processingTimeMs ASC")
    Page<VoiceCommand> findFastestCommands(Pageable pageable);
    
    /**
     * Tìm lệnh giọng nói có thời gian xử lý chậm nhất
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.processingTimeMs > 0 " +
           "ORDER BY vc.processingTimeMs DESC")
    Page<VoiceCommand> findSlowestCommands(Pageable pageable);
    
    /**
     * Tìm lệnh giọng nói có độ tin cậy cao nhất
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.confidence > 0 " +
           "ORDER BY vc.confidence DESC")
    Page<VoiceCommand> findMostConfidentCommands(Pageable pageable);
    
    /**
     * Tìm lệnh giọng nói có độ tin cậy thấp nhất
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE vc.confidence > 0 " +
           "ORDER BY vc.confidence ASC")
    Page<VoiceCommand> findLeastConfidentCommands(Pageable pageable);
    
    /**
     * Tìm lệnh giọng nói theo ghi chú (fuzzy search)
     */
    @Query("SELECT vc FROM VoiceCommand vc WHERE LOWER(vc.notes) LIKE LOWER(CONCAT('%', :notes, '%')) " +
           "ORDER BY vc.executedAt DESC")
    List<VoiceCommand> findByNotesContainingIgnoreCase(@Param("notes") String notes);
    
    /**
     * Đếm số lệnh giọng nói của một user
     */
    long countByUserId(Long userId);
    
    /**
     * Đếm số lệnh giọng nói theo intent
     */
    long countByIntent(VoiceCommand.VoiceIntent intent);
    
    /**
     * Đếm số lệnh giọng nói thành công
     */
    long countBySuccessTrue();
    
    /**
     * Đếm số lệnh giọng nói thất bại
     */
    long countBySuccessFalse();
    
    /**
     * Đếm số lệnh giọng nói theo speech engine
     */
    long countBySpeechEngine(VoiceCommand.SpeechEngine speechEngine);
    
    /**
     * Đếm số lệnh giọng nói theo device type
     */
    long countByDeviceType(String deviceType);
    
    /**
     * Đếm số lệnh giọng nói có lỗi
     */
    @Query("SELECT COUNT(vc) FROM VoiceCommand vc WHERE vc.errorMessage IS NOT NULL")
    long countCommandsWithErrors();
    
    /**
     * Tính độ tin cậy trung bình
     */
    @Query("SELECT AVG(vc.confidence) FROM VoiceCommand vc WHERE vc.confidence IS NOT NULL")
    Double getAverageConfidence();
    
    /**
     * Tính thời gian xử lý trung bình
     */
    @Query("SELECT AVG(vc.processingTimeMs) FROM VoiceCommand vc WHERE vc.processingTimeMs IS NOT NULL")
    Double getAverageProcessingTime();
    
    /**
     * Tính tỷ lệ thành công
     */
    @Query("SELECT (COUNT(CASE WHEN vc.success = true THEN 1 END) * 100.0 / COUNT(*)) " +
           "FROM VoiceCommand vc")
    Double getSuccessRate();
    
    /**
     * Tính tỷ lệ thành công theo intent
     */
    @Query("SELECT (COUNT(CASE WHEN vc.success = true THEN 1 END) * 100.0 / COUNT(*)) " +
           "FROM VoiceCommand vc WHERE vc.intent = :intent")
    Double getSuccessRateByIntent(@Param("intent") VoiceCommand.VoiceIntent intent);
    
    /**
     * Tính tỷ lệ thành công theo user
     */
    @Query("SELECT (COUNT(CASE WHEN vc.success = true THEN 1 END) * 100.0 / COUNT(*)) " +
           "FROM VoiceCommand vc WHERE vc.user.id = :userId")
    Double getSuccessRateByUser(@Param("userId") Long userId);
    
    /**
     * Tính tỷ lệ thành công theo speech engine
     */
    @Query("SELECT (COUNT(CASE WHEN vc.success = true THEN 1 END) * 100.0 / COUNT(*)) " +
           "FROM VoiceCommand vc WHERE vc.speechEngine = :speechEngine")
    Double getSuccessRateBySpeechEngine(@Param("speechEngine") VoiceCommand.SpeechEngine speechEngine);
    
    /**
     * Tìm intent phổ biến nhất
     */
    @Query("SELECT vc.intent, COUNT(vc) as count FROM VoiceCommand vc " +
           "GROUP BY vc.intent ORDER BY count DESC")
    List<Object[]> findMostPopularIntents();
    
    /**
     * Tìm user sử dụng voice nhiều nhất
     */
    @Query("SELECT vc.user.id, COUNT(vc) as count FROM VoiceCommand vc " +
           "GROUP BY vc.user.id ORDER BY count DESC")
    List<Object[]> findMostActiveVoiceUsers();
    
    /**
     * Tìm device type phổ biến nhất
     */
    @Query("SELECT vc.deviceType, COUNT(vc) as count FROM VoiceCommand vc " +
           "WHERE vc.deviceType IS NOT NULL GROUP BY vc.deviceType ORDER BY count DESC")
    List<Object[]> findMostPopularDeviceTypes();
    
    /**
     * Tìm speech engine hiệu quả nhất
     */
    @Query("SELECT vc.speechEngine, AVG(vc.confidence) as avgConfidence, " +
           "(COUNT(CASE WHEN vc.success = true THEN 1 END) * 100.0 / COUNT(*)) as successRate " +
           "FROM VoiceCommand vc WHERE vc.speechEngine IS NOT NULL " +
           "GROUP BY vc.speechEngine ORDER BY successRate DESC, avgConfidence DESC")
    List<Object[]> findMostEffectiveSpeechEngines();
} 