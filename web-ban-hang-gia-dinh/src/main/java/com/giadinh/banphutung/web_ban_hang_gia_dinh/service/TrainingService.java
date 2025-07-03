package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.LearningProgress;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.SearchAnalytics;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.TrainingContent;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.LearningProgressRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.SearchAnalyticsRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.TrainingContentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingService {
    
    private final TrainingContentRepository trainingContentRepository;
    private final LearningProgressRepository learningProgressRepository;
    private final SearchAnalyticsRepository searchAnalyticsRepository;

    /**
     * Tìm kiếm nội dung đào tạo theo từ khóa
     */
    public List<TrainingContent> searchTrainingContent(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return trainingContentRepository.findByIsActiveTrueOrderByPriorityAscCreatedAtDesc();
        }
        return trainingContentRepository.searchByKeyword(keyword.trim());
    }

    /**
     * Lấy quick help content
     */
    public List<TrainingContent> getQuickHelpContent() {
        return trainingContentRepository.findByShowInQuickHelpTrueAndIsActiveTrueOrderByPriorityAsc();
    }

    /**
     * Lấy nội dung đào tạo liên quan đến sản phẩm
     */
    public List<TrainingContent> getTrainingForProduct(Long productId) {
        return trainingContentRepository.findByRelatedProductId(productId);
    }

    /**
     * Lấy nội dung đào tạo theo category
     */
    public List<TrainingContent> getTrainingByCategory(TrainingContent.TrainingCategory category) {
        return trainingContentRepository.findByCategoryAndIsActiveTrue(category);
    }

    /**
     * Ghi nhận user đã xem training content
     */
    @Transactional
    public void markAsViewed(User user, Long trainingContentId, Integer readTime) {
        Optional<TrainingContent> trainingContent = trainingContentRepository.findById(trainingContentId);
        if (trainingContent.isPresent()) {
            LearningProgress progress = learningProgressRepository
                .findByUserAndTrainingContent(user, trainingContent.get())
                .orElse(new LearningProgress());
            
            progress.setUser(user);
            progress.setTrainingContent(trainingContent.get());
            progress.setViewed(true);
            progress.setViewCount(progress.getViewCount() + 1);
            
            if (readTime != null) {
                progress.setActualReadTime(readTime);
            }
            
            learningProgressRepository.save(progress);
        }
    }

    /**
     * Ghi nhận user đã hiểu nội dung
     */
    @Transactional
    public void markAsUnderstood(User user, Long trainingContentId, String notes) {
        Optional<TrainingContent> trainingContent = trainingContentRepository.findById(trainingContentId);
        if (trainingContent.isPresent()) {
            LearningProgress progress = learningProgressRepository
                .findByUserAndTrainingContent(user, trainingContent.get())
                .orElse(new LearningProgress());
            
            progress.setUser(user);
            progress.setTrainingContent(trainingContent.get());
            progress.setViewed(true);
            progress.setUnderstood(true);
            progress.setNotes(notes);
            
            learningProgressRepository.save(progress);
        }
    }

    /**
     * Ghi nhận thống kê tìm kiếm
     */
    @Transactional
    public void recordSearchAnalytics(User user, String searchQuery, SearchAnalytics.SearchType searchType, 
                                    boolean resultFound, int resultCount, Integer timeToFind) {
        SearchAnalytics analytics = new SearchAnalytics();
        analytics.setUser(user);
        analytics.setSearchQuery(searchQuery);
        analytics.setSearchType(searchType);
        analytics.setResultFound(resultFound);
        analytics.setResultCount(resultCount);
        analytics.setTimeToFind(timeToFind);
        
        searchAnalyticsRepository.save(analytics);
    }

    /**
     * Ghi nhận yêu cầu hỗ trợ
     */
    @Transactional
    public void recordHelpRequest(User user, String searchQuery, String notes) {
        SearchAnalytics analytics = new SearchAnalytics();
        analytics.setUser(user);
        analytics.setSearchQuery(searchQuery);
        analytics.setSearchType(SearchAnalytics.SearchType.HELP_SEARCH);
        analytics.setAskedForHelp(true);
        analytics.setSessionNotes(notes);
        
        searchAnalyticsRepository.save(analytics);
    }

    /**
     * Lấy thống kê học tập của user
     */
    public LearningStats getLearningStats(User user) {
        Long viewedCount = learningProgressRepository.countViewedByUserId(user.getId());
        Long understoodCount = learningProgressRepository.countUnderstoodByUserId(user.getId());
        Double avgSearchTime = searchAnalyticsRepository.getAverageSearchTimeByUserId(user.getId());
        Long helpRequestCount = searchAnalyticsRepository.countHelpRequestsByUserId(user.getId());
        
        return new LearningStats(viewedCount, understoodCount, avgSearchTime, helpRequestCount);
    }

    /**
     * DTO cho thống kê học tập
     */
    public static class LearningStats {
        public final Long viewedContent;
        public final Long understoodContent;
        public final Double averageSearchTime;
        public final Long helpRequests;

        public LearningStats(Long viewedContent, Long understoodContent, 
                           Double averageSearchTime, Long helpRequests) {
            this.viewedContent = viewedContent != null ? viewedContent : 0L;
            this.understoodContent = understoodContent != null ? understoodContent : 0L;
            this.averageSearchTime = averageSearchTime != null ? averageSearchTime : 0.0;
            this.helpRequests = helpRequests != null ? helpRequests : 0L;
        }
    }
}
