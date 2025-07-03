package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Category;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product.ProductStatus;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Tìm product theo code
    Optional<Product> findByCode(String code);
    
    // Tìm product theo category
    List<Product> findByCategoryOrderByNameAsc(Category category);
    
    // Tìm product theo status
    List<Product> findByStatus(ProductStatus status);
    
    // Tìm product active
    List<Product> findByStatusAndIsActiveTrueOrderByNameAsc(ProductStatus status);
    
    // Tìm product theo brand
    List<Product> findByBrandIgnoreCaseOrderByNameAsc(String brand);
    
    // Tìm product theo vehicle type
    List<Product> findByVehicleTypeIgnoreCaseOrderByNameAsc(String vehicleType);
    
    // Tìm product theo part number
    Optional<Product> findByPartNumber(String partNumber);
    
    // Tìm product theo OEM number
    Optional<Product> findByOemNumber(String oemNumber);
    
    // Tìm product trong khoảng giá
    List<Product> findByBasePriceBetweenOrderByBasePriceAsc(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Tìm product combo
    List<Product> findByIsComboTrue();
    
    // ========== Vehicle Compatibility Methods ==========
    
    // Tìm product tương thích với vehicle model
    @Query("SELECT p FROM Product p JOIN p.compatibleVehicles v WHERE v.id = :vehicleModelId")
    List<Product> findByCompatibleVehicleId(@Param("vehicleModelId") Long vehicleModelId);
    
    // Tìm product tương thích với nhiều vehicle models
    @Query("SELECT DISTINCT p FROM Product p JOIN p.compatibleVehicles v WHERE v.id IN :vehicleModelIds")
    List<Product> findByCompatibleVehicleIds(@Param("vehicleModelIds") List<Long> vehicleModelIds);
    
    // Tìm product theo vehicle brand
    @Query("SELECT DISTINCT p FROM Product p JOIN p.compatibleVehicles v WHERE LOWER(v.brand) = LOWER(:brand)")
    List<Product> findByVehicleBrand(@Param("brand") String brand);
    
    // Tìm product theo vehicle name pattern
    @Query("SELECT DISTINCT p FROM Product p JOIN p.compatibleVehicles v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :vehicleName, '%'))")
    List<Product> findByVehicleNameContaining(@Param("vehicleName") String vehicleName);
    
    // Tìm product có thể thay thế
    List<Product> findByIsSubstitutableTrue();
    
    // Tìm product cần nhập thêm (low stock)
    @Query("SELECT p FROM Product p, Inventory i WHERE p.id = i.product.id " +
           "GROUP BY p.id HAVING SUM(i.quantity) <= p.reorderPoint")
    List<Product> findLowStockProducts();
    
    // Tìm product theo tên (không phân biệt hoa thường)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(p.code) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(p.partNumber) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> searchProducts(@Param("name") String name, Pageable pageable);
    
    // Tìm product theo category và các category con
    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds")
    List<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
    
    // Kiểm tra code đã tồn tại chưa
    boolean existsByCode(String code);
    
    // Kiểm tra part number đã tồn tại chưa
    boolean existsByPartNumber(String partNumber);
    
    // Đếm product theo category
    Long countByCategory(Category category);
}
