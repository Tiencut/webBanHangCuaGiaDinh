package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ProductSupplierRepository - Repository cho quan hệ Product-Supplier
 * 
 * Cung cấp các query methods để:
 * - Tìm supplier cho một sản phẩm
 * - Tìm supplier tối ưu dựa trên giá, chất lượng, tồn kho
 * - Quản lý tồn kho theo supplier
 * - Phân tích hiệu quả của từng supplier
 */
@Repository
public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, Long> {
    
    /**
     * Tìm tất cả supplier của một sản phẩm
     */
    List<ProductSupplier> findByProductIdOrderByPriorityOrderAsc(Long productId);
    
    /**
     * Tìm supplier đang hoạt động của một sản phẩm
     */
    List<ProductSupplier> findByProductIdAndIsActiveTrueOrderByPriorityOrderAsc(Long productId);
    
    /**
     * Tìm supplier có tồn kho > 0 của một sản phẩm
     */
    List<ProductSupplier> findByProductIdAndStockQuantityGreaterThanAndIsActiveTrueOrderByPriorityOrderAsc(
            Long productId, Integer minQuantity);
    
    /**
     * Tìm supplier theo product và supplier
     */
    Optional<ProductSupplier> findByProductIdAndSupplierId(Long productId, Long supplierId);
    
    /**
     * Tìm supplier có giá bán thấp nhất cho một sản phẩm
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.product.id = :productId " +
           "AND ps.isActive = true AND ps.stockQuantity > 0 " +
           "ORDER BY ps.sellingPrice ASC")
    List<ProductSupplier> findLowestPriceSuppliers(@Param("productId") Long productId);
    
    /**
     * Tìm supplier có lợi nhuận cao nhất cho một sản phẩm
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.product.id = :productId " +
           "AND ps.isActive = true AND ps.stockQuantity > 0 " +
           "ORDER BY (ps.sellingPrice - ps.costPrice) DESC")
    List<ProductSupplier> findHighestProfitSuppliers(@Param("productId") Long productId);
    
    /**
     * Tìm supplier có chất lượng cao nhất cho một sản phẩm
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.product.id = :productId " +
           "AND ps.isActive = true AND ps.stockQuantity > 0 " +
           "ORDER BY ps.qualityRating DESC")
    List<ProductSupplier> findHighestQualitySuppliers(@Param("productId") Long productId);
    
    /**
     * Tìm supplier cần đặt hàng lại (tồn kho <= reorder point)
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.stockQuantity <= ps.reorderPoint " +
           "AND ps.isActive = true ORDER BY ps.product.name, ps.supplier.name")
    List<ProductSupplier> findSuppliersNeedingReorder();
    
    /**
     * Tìm supplier hết hàng
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.stockQuantity = 0 " +
           "AND ps.isActive = true ORDER BY ps.product.name, ps.supplier.name")
    List<ProductSupplier> findOutOfStockSuppliers();
    
    /**
     * Tìm supplier sắp hết hàng
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.stockQuantity <= ps.minStockLevel " +
           "AND ps.stockQuantity > 0 AND ps.isActive = true " +
           "ORDER BY ps.product.name, ps.supplier.name")
    List<ProductSupplier> findLowStockSuppliers();
    
    /**
     * Tìm supplier theo khoảng giá
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.product.id = :productId " +
           "AND ps.sellingPrice BETWEEN :minPrice AND :maxPrice " +
           "AND ps.isActive = true ORDER BY ps.sellingPrice ASC")
    List<ProductSupplier> findByPriceRange(@Param("productId") Long productId, 
                                          @Param("minPrice") BigDecimal minPrice, 
                                          @Param("maxPrice") BigDecimal maxPrice);
    
    /**
     * Tìm supplier theo đánh giá chất lượng
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.product.id = :productId " +
           "AND ps.qualityRating >= :minRating AND ps.isActive = true " +
           "ORDER BY ps.qualityRating DESC")
    List<ProductSupplier> findByQualityRating(@Param("productId") Long productId, 
                                             @Param("minRating") Double minRating);
    
    /**
     * Tìm supplier theo thời gian giao hàng
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.product.id = :productId " +
           "AND ps.deliveryTimeDays <= :maxDeliveryDays AND ps.isActive = true " +
           "ORDER BY ps.deliveryTimeDays ASC")
    List<ProductSupplier> findByDeliveryTime(@Param("productId") Long productId, 
                                            @Param("maxDeliveryDays") Integer maxDeliveryDays);
    
    /**
     * Tìm supplier tối ưu (kết hợp giá, chất lượng, tồn kho)
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.product.id = :productId " +
           "AND ps.isActive = true AND ps.stockQuantity > 0 " +
           "ORDER BY (ps.qualityRating * 0.4 + (1.0 / ps.sellingPrice) * 0.3 + " +
           "ps.stockQuantity * 0.3) DESC")
    List<ProductSupplier> findOptimalSuppliers(@Param("productId") Long productId);
    
    /**
     * Đếm số supplier của một sản phẩm
     */
    long countByProductIdAndIsActiveTrue(Long productId);
    
    /**
     * Đếm số sản phẩm của một supplier
     */
    long countBySupplierIdAndIsActiveTrue(Long supplierId);
    
    /**
     * Tìm supplier có doanh số cao nhất
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.soldQuantity > 0 " +
           "ORDER BY ps.soldQuantity DESC")
    Page<ProductSupplier> findTopSellingSuppliers(Pageable pageable);
    
    /**
     * Tìm supplier có đánh giá cao nhất
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.qualityRating > 0 " +
           "ORDER BY ps.qualityRating DESC")
    Page<ProductSupplier> findTopRatedSuppliers(Pageable pageable);
    
    /**
     * Tìm supplier theo category
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.product.category.id = :categoryId " +
           "AND ps.isActive = true ORDER BY ps.product.name, ps.supplier.name")
    List<ProductSupplier> findByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * Tìm supplier theo brand
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.product.brand = :brand " +
           "AND ps.isActive = true ORDER BY ps.product.name, ps.supplier.name")
    List<ProductSupplier> findByBrand(@Param("brand") String brand);
    
    /**
     * Tìm supplier theo vehicle type
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.product.vehicleType = :vehicleType " +
           "AND ps.isActive = true ORDER BY ps.product.name, ps.supplier.name")
    List<ProductSupplier> findByVehicleType(@Param("vehicleType") String vehicleType);
    
    /**
     * Tìm supplier có giá thay đổi gần đây
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.lastPurchaseDate IS NOT NULL " +
           "ORDER BY ps.lastPurchaseDate DESC")
    Page<ProductSupplier> findRecentlyUpdatedSuppliers(Pageable pageable);
    
    /**
     * Tìm supplier theo khoảng giá nhập
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.costPrice BETWEEN :minCost AND :maxCost " +
           "AND ps.isActive = true ORDER BY ps.costPrice ASC")
    List<ProductSupplier> findByCostRange(@Param("minCost") BigDecimal minCost, 
                                         @Param("maxCost") BigDecimal maxCost);
    
    /**
     * Tìm supplier có lợi nhuận margin cao nhất
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.isActive = true " +
           "ORDER BY ((ps.sellingPrice - ps.costPrice) / ps.sellingPrice) DESC")
    Page<ProductSupplier> findHighestMarginSuppliers(Pageable pageable);
    
    /**
     * Tìm supplier theo reliability rating
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.reliabilityRating >= :minReliability " +
           "AND ps.isActive = true ORDER BY ps.reliabilityRating DESC")
    List<ProductSupplier> findByReliabilityRating(@Param("minReliability") Double minReliability);
    
    /**
     * Tìm supplier có tồn kho cao nhất
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE ps.stockQuantity > 0 " +
           "ORDER BY ps.stockQuantity DESC")
    Page<ProductSupplier> findHighestStockSuppliers(Pageable pageable);
    
    /**
     * Tìm supplier theo priority order
     */
    List<ProductSupplier> findByProductIdAndIsActiveTrueOrderByPriorityOrderAscSellingPriceAsc(Long productId);
    
    /**
     * Tìm supplier theo tên sản phẩm (fuzzy search)
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE LOWER(ps.product.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
           "AND ps.isActive = true ORDER BY ps.product.name, ps.supplier.name")
    List<ProductSupplier> findByProductNameContainingIgnoreCase(@Param("productName") String productName);
    
    /**
     * Tìm supplier theo tên supplier (fuzzy search)
     */
    @Query("SELECT ps FROM ProductSupplier ps WHERE LOWER(ps.supplier.name) LIKE LOWER(CONCAT('%', :supplierName, '%')) " +
           "AND ps.isActive = true ORDER BY ps.supplier.name, ps.product.name")
    List<ProductSupplier> findBySupplierNameContainingIgnoreCase(@Param("supplierName") String supplierName);

    // Tìm theo supplier và sắp xếp theo số lượng tồn kho giảm dần
    List<ProductSupplier> findBySupplierIdAndIsActiveTrueOrderByCurrentQuantityDesc(Long supplierId);

    /**
     * Tìm tất cả sản phẩm của một supplier, sắp xếp theo số lượng tồn kho giảm dần
     */
    List<ProductSupplier> findBySupplierIdAndIsActiveTrueOrderByStockQuantityDesc(Long supplierId);
} 