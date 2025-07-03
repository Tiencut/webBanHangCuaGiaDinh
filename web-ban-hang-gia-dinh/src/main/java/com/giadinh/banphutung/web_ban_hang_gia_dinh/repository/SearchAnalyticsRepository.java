package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.SearchAnalytics;

@Repository
public interface SearchAnalyticsRepository extends JpaRepository<SearchAnalytics, Long> {
    
    List<SearchAnalytics> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT sa FROM SearchAnalytics sa WHERE sa.user.id = :userId AND sa.createdAt >= :fromDate")
    List<SearchAnalytics> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT AVG(sa.timeToFind) FROM SearchAnalytics sa WHERE sa.user.id = :userId AND sa.timeToFind IS NOT NULL")
    Double getAverageSearchTimeByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(sa) FROM SearchAnalytics sa WHERE sa.user.id = :userId AND sa.askedForHelp = true")
    Long countHelpRequestsByUserId(@Param("userId") Long userId);
}
