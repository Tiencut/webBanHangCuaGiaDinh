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
}
