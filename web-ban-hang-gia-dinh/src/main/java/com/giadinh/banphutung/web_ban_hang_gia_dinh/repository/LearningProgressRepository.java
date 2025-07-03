package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.LearningProgress;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.TrainingContent;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;

@Repository
public interface LearningProgressRepository extends JpaRepository<LearningProgress, Long> {
    
    Optional<LearningProgress> findByUserAndTrainingContent(User user, TrainingContent trainingContent);
    
    List<LearningProgress> findByUserOrderByUpdatedAtDesc(User user);
    
    @Query("SELECT lp FROM LearningProgress lp WHERE lp.user.id = :userId AND lp.understood = true")
    List<LearningProgress> findUnderstoodContentByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(lp) FROM LearningProgress lp WHERE lp.user.id = :userId AND lp.viewed = true")
    Long countViewedByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(lp) FROM LearningProgress lp WHERE lp.user.id = :userId AND lp.understood = true")
    Long countUnderstoodByUserId(@Param("userId") Long userId);
}
