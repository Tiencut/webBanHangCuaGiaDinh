package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.DiscountRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DiscountRuleRepository - Repository cho quản lý quy tắc giảm giá
 * 
 * Cung cấp các query methods để:
 * - Tìm quy tắc giảm giá phù hợp
 * - Quản lý approval workflow
 * - Phân tích hiệu quả discount
 * - Role-based permissions
 */
@Repository
public interface DiscountRuleRepository extends JpaRepository<DiscountRule, Long> {
    
    /**
     * Tìm tất cả quy tắc đang hoạt động
     */
    List<DiscountRule> findByIsActiveTrueOrderByPriorityOrderAsc();
    
    /**
     * Tìm quy tắc theo loại
     */
    List<DiscountRule> findByTypeAndIsActiveTrueOrderByPriorityOrderAsc(DiscountRule.DiscountType type);
    
    /**
     * Tìm quy tắc theo loại giá trị giảm giá
     */
    List<DiscountRule> findByDiscountTypeAndIsActiveTrueOrderByPriorityOrderAsc(DiscountRule.DiscountValueType discountType);
    
    /**
     * Tìm quy tắc cần approval
     */
    List<DiscountRule> findByRequiresApprovalTrueAndIsActiveTrueOrderByPriorityOrderAsc();
    
    /**
     * Tìm quy tắc không cần approval
     */
    List<DiscountRule> findByRequiresApprovalFalseAndIsActiveTrueOrderByPriorityOrderAsc();
    
    /**
     * Tìm quy tắc theo role
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.isActive = true " +
           "AND (dr.applicableRoles IS NULL OR :role MEMBER OF dr.applicableRoles) " +
           "ORDER BY dr.priorityOrder ASC")
    List<DiscountRule> findByRole(@Param("role") String role);
    
    /**
     * Tìm quy tắc theo customer type
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.isActive = true " +
           "AND (dr.applicableCustomerTypes IS NULL OR :customerType MEMBER OF dr.applicableCustomerTypes) " +
           "ORDER BY dr.priorityOrder ASC")
    List<DiscountRule> findByCustomerType(@Param("customerType") String customerType);
    
    /**
     * Tìm quy tắc theo category
     */
    @Query("SELECT dr FROM DiscountRule dr JOIN dr.applicableCategories ac " +
           "WHERE ac.id = :categoryId AND dr.isActive = true " +
           "ORDER BY dr.priorityOrder ASC")
    List<DiscountRule> findByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * Tìm quy tắc theo product
     */
    @Query("SELECT dr FROM DiscountRule dr JOIN dr.applicableProducts ap " +
           "WHERE ap.id = :productId AND dr.isActive = true " +
           "ORDER BY dr.priorityOrder ASC")
    List<DiscountRule> findByProductId(@Param("productId") Long productId);
    
    /**
     * Tìm quy tắc theo khoảng giá trị giảm giá
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.maxDiscountValue BETWEEN :minValue AND :maxValue " +
           "AND dr.isActive = true ORDER BY dr.maxDiscountValue ASC")
    List<DiscountRule> findByDiscountValueRange(@Param("minValue") BigDecimal minValue, 
                                               @Param("maxValue") BigDecimal maxValue);
    
    /**
     * Tìm quy tắc theo profit margin tối thiểu
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.minProfitMargin >= :minMargin " +
           "AND dr.isActive = true ORDER BY dr.minProfitMargin ASC")
    List<DiscountRule> findByMinProfitMarginGreaterThan(@Param("minMargin") BigDecimal minMargin);
    
    /**
     * Tìm quy tắc theo số lượng tối thiểu
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.minQuantity IS NULL OR dr.minQuantity <= :quantity " +
           "AND dr.isActive = true ORDER BY dr.priorityOrder ASC")
    List<DiscountRule> findByMinQuantityLessThanOrNull(@Param("quantity") Integer quantity);
    
    /**
     * Tìm quy tắc theo giá trị đơn hàng tối thiểu
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.minOrderValue IS NULL OR dr.minOrderValue <= :orderValue " +
           "AND dr.isActive = true ORDER BY dr.priorityOrder ASC")
    List<DiscountRule> findByMinOrderValueLessThanOrNull(@Param("orderValue") BigDecimal orderValue);
    
    /**
     * Tìm quy tắc có hiệu lực trong khoảng thời gian
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.isActive = true " +
           "AND (dr.effectiveFrom IS NULL OR dr.effectiveFrom <= :currentTime) " +
           "AND (dr.effectiveTo IS NULL OR dr.effectiveTo >= :currentTime) " +
           "ORDER BY dr.priorityOrder ASC")
    List<DiscountRule> findEffectiveRules(@Param("currentTime") LocalDateTime currentTime);
    
    /**
     * Tìm quy tắc sắp hết hiệu lực
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.effectiveTo IS NOT NULL " +
           "AND dr.effectiveTo BETWEEN :fromDate AND :toDate " +
           "AND dr.isActive = true ORDER BY dr.effectiveTo ASC")
    List<DiscountRule> findExpiringRules(@Param("fromDate") LocalDateTime fromDate, 
                                        @Param("toDate") LocalDateTime toDate);
    
    /**
     * Tìm quy tắc đã hết hiệu lực
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.effectiveTo IS NOT NULL " +
           "AND dr.effectiveTo < :currentTime AND dr.isActive = true " +
           "ORDER BY dr.effectiveTo DESC")
    List<DiscountRule> findExpiredRules(@Param("currentTime") LocalDateTime currentTime);
    
    /**
     * Tìm quy tắc theo tên (fuzzy search)
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE LOWER(dr.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "AND dr.isActive = true ORDER BY dr.name")
    List<DiscountRule> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Tìm quy tắc theo mô tả (fuzzy search)
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE LOWER(dr.description) LIKE LOWER(CONCAT('%', :description, '%')) " +
           "AND dr.isActive = true ORDER BY dr.name")
    List<DiscountRule> findByDescriptionContainingIgnoreCase(@Param("description") String description);
    
    /**
     * Tìm quy tắc theo priority order
     */
    List<DiscountRule> findByPriorityOrderLessThanEqualAndIsActiveTrueOrderByPriorityOrderAsc(Integer priorityOrder);
    
    /**
     * Tìm quy tắc có auto approval limit cao nhất
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.autoApprovalLimit > 0 " +
           "ORDER BY dr.autoApprovalLimit DESC")
    Page<DiscountRule> findHighestAutoApprovalRules(Pageable pageable);
    
    /**
     * Tìm quy tắc có max discount value cao nhất
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.maxDiscountValue > 0 " +
           "ORDER BY dr.maxDiscountValue DESC")
    Page<DiscountRule> findHighestDiscountRules(Pageable pageable);
    
    /**
     * Tìm quy tắc theo ghi chú (fuzzy search)
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE LOWER(dr.notes) LIKE LOWER(CONCAT('%', :notes, '%')) " +
           "AND dr.isActive = true ORDER BY dr.name")
    List<DiscountRule> findByNotesContainingIgnoreCase(@Param("notes") String notes);
    
    /**
     * Tìm quy tắc theo ngày tạo
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.createdAt >= :fromDate " +
           "ORDER BY dr.createdAt DESC")
    List<DiscountRule> findByCreatedAtAfter(@Param("fromDate") LocalDateTime fromDate);
    
    /**
     * Tìm quy tắc theo ngày cập nhật
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.updatedAt >= :fromDate " +
           "ORDER BY dr.updatedAt DESC")
    List<DiscountRule> findByUpdatedAtAfter(@Param("fromDate") LocalDateTime fromDate);
    
    /**
     * Đếm số quy tắc đang hoạt động
     */
    long countByIsActiveTrue();
    
    /**
     * Đếm số quy tắc theo loại
     */
    long countByTypeAndIsActiveTrue(DiscountRule.DiscountType type);
    
    /**
     * Đếm số quy tắc cần approval
     */
    long countByRequiresApprovalTrueAndIsActiveTrue();
    
    /**
     * Đếm số quy tắc có hiệu lực
     */
    @Query("SELECT COUNT(dr) FROM DiscountRule dr WHERE dr.isActive = true " +
           "AND (dr.effectiveFrom IS NULL OR dr.effectiveFrom <= CURRENT_TIMESTAMP) " +
           "AND (dr.effectiveTo IS NULL OR dr.effectiveTo >= CURRENT_TIMESTAMP)")
    long countEffectiveRules();
    
    /**
     * Đếm số quy tắc sắp hết hiệu lực
     */
    @Query("SELECT COUNT(dr) FROM DiscountRule dr WHERE dr.effectiveTo IS NOT NULL " +
           "AND dr.effectiveTo BETWEEN CURRENT_TIMESTAMP AND :futureDate " +
           "AND dr.isActive = true")
    long countExpiringRules(@Param("futureDate") LocalDateTime futureDate);
    
    /**
     * Tìm quy tắc tối ưu cho một tình huống cụ thể
     */
    @Query("SELECT dr FROM DiscountRule dr WHERE dr.isActive = true " +
           "AND (dr.applicableRoles IS NULL OR :role MEMBER OF dr.applicableRoles) " +
           "AND (dr.applicableCustomerTypes IS NULL OR :customerType MEMBER OF dr.applicableCustomerTypes) " +
           "AND (dr.minQuantity IS NULL OR dr.minQuantity <= :quantity) " +
           "AND (dr.minOrderValue IS NULL OR dr.minOrderValue <= :orderValue) " +
           "AND (dr.effectiveFrom IS NULL OR dr.effectiveFrom <= CURRENT_TIMESTAMP) " +
           "AND (dr.effectiveTo IS NULL OR dr.effectiveTo >= CURRENT_TIMESTAMP) " +
           "ORDER BY dr.priorityOrder ASC")
    List<DiscountRule> findOptimalRules(@Param("role") String role, 
                                       @Param("customerType") String customerType,
                                       @Param("quantity") Integer quantity,
                                       @Param("orderValue") BigDecimal orderValue);
} 