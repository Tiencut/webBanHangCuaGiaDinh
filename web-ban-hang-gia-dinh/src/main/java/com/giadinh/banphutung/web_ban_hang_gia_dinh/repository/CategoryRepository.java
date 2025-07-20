package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Tìm category theo code
    Optional<Category> findByCode(String code);
    
    // Tìm category root (không có parent)
    List<Category> findByParentIsNull();
    
    // Tìm category con theo parent
    List<Category> findByParentIdOrderBySortOrderAsc(Long parentId);
    
    // Tìm category theo level
    List<Category> findByLevel(Integer level);
    
    // Tìm category đang active
    List<Category> findByIsActiveTrueOrderBySortOrderAsc();
    
    // Tìm category theo tên (không phân biệt hoa thường)
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Category> findByNameContainingIgnoreCase(@Param("name") String name);
    
    // Tìm tất cả category con của một category (bao gồm cả con của con)
    @Query("SELECT c FROM Category c WHERE c.path LIKE CONCAT(:path, '%')")
    List<Category> findAllChildrenByPath(@Param("path") String path);
    
    // Tìm category theo parent và active
    List<Category> findByParentAndIsActiveTrueOrderBySortOrderAsc(Category parent);
    
    // Kiểm tra code đã tồn tại chưa
    boolean existsByCode(String code);
    
    // Đếm số lượng category con
    @Query("SELECT COUNT(c) FROM Category c WHERE c.parent.id = :parentId")
    Long countByParentId(@Param("parentId") Long parentId);
    
    // === Các method bổ sung cho CategoryService ===
    
    // Tìm tất cả category đang active
    List<Category> findByIsActiveTrue();
    
    // Tìm category theo id và active
    Optional<Category> findByIdAndIsActiveTrue(Long id);
    
    // Tìm category theo code và active
    Optional<Category> findByCodeAndIsActiveTrue(String code);
    
    // Tìm category con theo parent id và active
    List<Category> findByParentIdAndIsActiveTrue(Long parentId);
    
    // Tìm category root (parent null) và active
    List<Category> findByParentIdIsNullAndIsActiveTrue();
    
    // Tìm category theo level và active
    List<Category> findByLevelAndIsActiveTrue(Integer level);
    
    // Tìm kiếm category theo keyword
    @Query("SELECT c FROM Category c WHERE c.isActive = true AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Category> searchCategories(@Param("keyword") String keyword);
    
    // Tìm kiếm nâng cao category
    @Query("""
        SELECT c FROM Category c 
        WHERE c.isActive = true
        AND (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) 
             OR LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:parentId IS NULL OR c.parent.id = :parentId)
        AND (:level IS NULL OR c.level = :level)
        AND (:isActive IS NULL OR c.isActive = :isActive)
        ORDER BY 
        CASE WHEN :sortBy = 'name' THEN c.name END ASC,
        CASE WHEN :sortBy = 'name' AND :sortDirection = 'desc' THEN c.name END DESC,
        CASE WHEN :sortBy = 'code' THEN c.code END ASC,
        CASE WHEN :sortBy = 'code' AND :sortDirection = 'desc' THEN c.code END DESC,
        CASE WHEN :sortBy = 'level' THEN c.level END ASC,
        CASE WHEN :sortBy = 'level' AND :sortDirection = 'desc' THEN c.level END DESC,
        CASE WHEN :sortBy = 'sortOrder' THEN c.sortOrder END ASC,
        CASE WHEN :sortBy = 'sortOrder' AND :sortDirection = 'desc' THEN c.sortOrder END DESC
        """)
    List<Category> advancedSearch(
        @Param("keyword") String keyword,
        @Param("parentId") Long parentId,
        @Param("level") Integer level,
        @Param("isActive") Boolean isActive,
        @Param("productCountRange") String productCountRange,
        @Param("sortBy") String sortBy,
        @Param("sortDirection") String sortDirection
    );
}
