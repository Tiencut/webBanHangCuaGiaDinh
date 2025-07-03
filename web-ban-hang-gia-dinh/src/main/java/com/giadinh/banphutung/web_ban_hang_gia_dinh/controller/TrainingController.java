package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.SearchAnalytics;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.TrainingContent;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.TrainingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    /**
     * Tìm kiếm nội dung đào tạo
     */
    @GetMapping("/search")
    public ResponseEntity<List<TrainingContent>> searchTraining(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TrainingContent.TrainingCategory category,
            @AuthenticationPrincipal User currentUser) {
        
        List<TrainingContent> results;
        
        if (category != null) {
            results = trainingService.getTrainingByCategory(category);
        } else {
            results = trainingService.searchTrainingContent(keyword);
        }
        
        // Record search analytics
        if (keyword != null && !keyword.trim().isEmpty()) {
            trainingService.recordSearchAnalytics(
                currentUser, 
                keyword, 
                SearchAnalytics.SearchType.TRAINING_SEARCH,
                !results.isEmpty(),
                results.size(),
                null
            );
        }
        
        return ResponseEntity.ok(results);
    }

    /**
     * Lấy quick help content
     */
    @GetMapping("/quick-help")
    public ResponseEntity<List<TrainingContent>> getQuickHelp() {
        List<TrainingContent> quickHelp = trainingService.getQuickHelpContent();
        return ResponseEntity.ok(quickHelp);
    }

    /**
     * Lấy training content liên quan đến sản phẩm
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<TrainingContent>> getTrainingForProduct(@PathVariable Long productId) {
        List<TrainingContent> content = trainingService.getTrainingForProduct(productId);
        return ResponseEntity.ok(content);
    }

    /**
     * Đánh dấu đã xem
     */
    @PostMapping("/{trainingId}/viewed")
    public ResponseEntity<Void> markAsViewed(
            @PathVariable Long trainingId,
            @RequestParam(required = false) Integer readTime,
            @AuthenticationPrincipal User currentUser) {
        
        trainingService.markAsViewed(currentUser, trainingId, readTime);
        return ResponseEntity.ok().build();
    }

    /**
     * Đánh dấu đã hiểu
     */
    @PostMapping("/{trainingId}/understood")
    public ResponseEntity<Void> markAsUnderstood(
            @PathVariable Long trainingId,
            @RequestParam(required = false) String notes,
            @AuthenticationPrincipal User currentUser) {
        
        trainingService.markAsUnderstood(currentUser, trainingId, notes);
        return ResponseEntity.ok().build();
    }

    /**
     * Yêu cầu hỗ trợ
     */
    @PostMapping("/help-request")
    public ResponseEntity<Void> requestHelp(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal User currentUser) {
        
        String query = request.get("query");
        String notes = request.get("notes");
        
        trainingService.recordHelpRequest(currentUser, query, notes);
        return ResponseEntity.ok().build();
    }

    /**
     * Lấy thống kê học tập
     */
    @GetMapping("/stats")
    public ResponseEntity<TrainingService.LearningStats> getLearningStats(
            @AuthenticationPrincipal User currentUser) {
        
        TrainingService.LearningStats stats = trainingService.getLearningStats(currentUser);
        return ResponseEntity.ok(stats);
    }

    /**
     * Ghi nhận thống kê tìm kiếm sản phẩm (từ màn hình bán hàng)
     */
    @PostMapping("/record-search")
    public ResponseEntity<Void> recordProductSearch(
            @RequestBody Map<String, Object> searchData,
            @AuthenticationPrincipal User currentUser) {
        
        String query = (String) searchData.get("query");
        Boolean found = (Boolean) searchData.get("found");
        Integer count = (Integer) searchData.get("count");
        Integer time = (Integer) searchData.get("timeToFind");
        
        trainingService.recordSearchAnalytics(
            currentUser,
            query,
            SearchAnalytics.SearchType.PRODUCT_SEARCH,
            found != null && found,
            count != null ? count : 0,
            time
        );
        
        return ResponseEntity.ok().build();
    }
}
