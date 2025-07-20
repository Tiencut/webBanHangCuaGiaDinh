package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Category;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product.ProductStatus;

@Repository
@RepositoryRestResource(path = "products", collectionResourceRel = "products", itemResourceRel = "product")
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // ===== PROJECTION INTERFACE =====
    interface ProductProjection {
        Long getId();
        String getCode();
        String getName();
        String getPartNumber();
        String getOemNumber();
        String getBrand();
        String getVehicleType();
        BigDecimal getBasePrice();
        BigDecimal getSalePrice();
        ProductStatus getStatus();
        Boolean getIsActive();
        Boolean getIsCombo();
        Boolean getIsSubstitutable();
        Integer getReorderPoint();
        String getDescription();
        String getSpecifications();
        String getWarranty();
        Category getCategory();
        java.time.LocalDateTime getCreatedAt();
        java.time.LocalDateTime getUpdatedAt();
    }
    
    // Tìm product theo code
    @RestResource(path = "by-code", rel = "by-code")
    Optional<Product> findByCode(String code);
    
    // Tìm product theo code với fetch join compatible vehicles
    @RestResource(exported = false) // Không expose endpoint này
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.compatibleVehicles WHERE p.code = :code")
    Optional<Product> findByCodeWithCompatibleVehicles(@Param("code") String code);
    
    // Tìm product theo category
    @RestResource(path = "by-category", rel = "by-category")
    List<Product> findByCategoryOrderByNameAsc(Category category);
    
    // Tìm product theo status
    @RestResource(path = "by-status", rel = "by-status")
    List<Product> findByStatus(ProductStatus status);
    
    // Tìm product active
    @RestResource(path = "active", rel = "active")
    List<Product> findByStatusAndIsActiveTrueOrderByNameAsc(ProductStatus status);
    
    // Tìm product theo brand
    @RestResource(path = "by-brand", rel = "by-brand")
    List<Product> findByBrandIgnoreCaseOrderByNameAsc(String brand);
    
    // Tìm product theo vehicle type
    @RestResource(path = "by-vehicle-type", rel = "by-vehicle-type")
    List<Product> findByVehicleTypeIgnoreCaseOrderByNameAsc(String vehicleType);
    
    // Tìm product theo part number
    @RestResource(path = "by-part-number", rel = "by-part-number")
    Optional<Product> findByPartNumber(String partNumber);
    
    // Tìm product theo OEM number
    @RestResource(path = "by-oem-number", rel = "by-oem-number")
    Optional<Product> findByOemNumber(String oemNumber);
    
    // Tìm product trong khoảng giá
    @RestResource(path = "by-price-range", rel = "by-price-range")
    List<Product> findByBasePriceBetweenOrderByBasePriceAsc(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Tìm product combo
    @RestResource(path = "combo", rel = "combo")
    List<Product> findByIsComboTrue();
    
    // ========== Vehicle Compatibility Methods ==========
    
    // Tìm product tương thích với vehicle model
    @RestResource(path = "by-vehicle-id", rel = "by-vehicle-id")
    @Query("SELECT p FROM Product p JOIN p.compatibleVehicles v WHERE v.id = :vehicleModelId")
    List<Product> findByCompatibleVehicleId(@Param("vehicleModelId") Long vehicleModelId);
    
    // Tìm product tương thích với nhiều vehicle models
    @RestResource(exported = false) // Không expose endpoint này
    @Query("SELECT DISTINCT p FROM Product p JOIN p.compatibleVehicles v WHERE v.id IN :vehicleModelIds")
    List<Product> findByCompatibleVehicleIds(@Param("vehicleModelIds") List<Long> vehicleModelIds);
    
    // Tìm product theo vehicle brand
    @RestResource(path = "by-vehicle-brand", rel = "by-vehicle-brand")
    @Query("SELECT DISTINCT p FROM Product p JOIN p.compatibleVehicles v WHERE LOWER(v.brand) = LOWER(:brand)")
    List<Product> findByVehicleBrand(@Param("brand") String brand);
    
    // Tìm product theo vehicle name pattern
    @RestResource(path = "by-vehicle-name", rel = "by-vehicle-name")
    @Query("SELECT DISTINCT p FROM Product p JOIN p.compatibleVehicles v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :vehicleName, '%'))")
    List<Product> findByVehicleNameContaining(@Param("vehicleName") String vehicleName);
    
    // Tìm product có thể thay thế
    @RestResource(path = "substitutable", rel = "substitutable")
    List<Product> findByIsSubstitutableTrue();
    
    // Tìm product cần nhập thêm (low stock)
    @RestResource(path = "low-stock", rel = "low-stock")
    @Query("SELECT p FROM Product p, Inventory i WHERE p.id = i.product.id " +
           "GROUP BY p.id HAVING SUM(i.currentQuantity) <= p.reorderPoint")
    List<Product> findLowStockProducts();
    
    // Tìm product theo tên (không phân biệt hoa thường)
    @RestResource(path = "search", rel = "search")
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(p.code) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(p.partNumber) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> searchProducts(@Param("name") String name, Pageable pageable);
    
    // Tìm product theo category và các category con
    @RestResource(exported = false) // Không expose endpoint này
    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds")
    List<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
    
    // Kiểm tra code đã tồn tại chưa
    @RestResource(exported = false) // Không expose endpoint này
    boolean existsByCode(String code);
    
    // Kiểm tra part number đã tồn tại chưa
    @RestResource(exported = false) // Không expose endpoint này
    boolean existsByPartNumber(String partNumber);
    
    // Đếm product theo category
    @RestResource(exported = false) // Không expose endpoint này
    Long countByCategory(Category category);
    
    // === Các method bổ sung cho ProductService ===
    
    // Tìm product theo id và active
    @RestResource(exported = false) // Không expose endpoint này
    Optional<Product> findByIdAndIsActiveTrue(Long id);
}
