package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.TrainingContent;

@Repository
public interface TrainingContentRepository extends JpaRepository<TrainingContent, Long> {
    
    List<TrainingContent> findByIsActiveTrueOrderByPriorityAscCreatedAtDesc();
    
    List<TrainingContent> findByCategoryAndIsActiveTrue(TrainingContent.TrainingCategory category);
    
    List<TrainingContent> findByShowInQuickHelpTrueAndIsActiveTrueOrderByPriorityAsc();
    
    @Query("SELECT tc FROM TrainingContent tc WHERE tc.isActive = true AND " +
           "(LOWER(tc.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(tc.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(tc.tags) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<TrainingContent> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT tc FROM TrainingContent tc JOIN tc.relatedProducts p WHERE p.id = :productId AND tc.isActive = true")
    List<TrainingContent> findByRelatedProductId(@Param("productId") Long productId);
    
    @Query("SELECT tc FROM TrainingContent tc JOIN tc.relatedVehicles v WHERE v.id = :vehicleId AND tc.isActive = true")
    List<TrainingContent> findByRelatedVehicleId(@Param("vehicleId") Long vehicleId);
}
