package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.SubstitutionHistory;
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
 * SubstitutionHistoryRepository - Repository cho lịch sử thay thế linh kiện
 * 
 * Cung cấp các query methods để:
 * - Tìm lịch sử thay thế của xe
 * - Phân tích hiệu quả của linh kiện
 * - Gợi ý linh kiện phù hợp
 * - Theo dõi bảo hành
 */
@Repository
public interface SubstitutionHistoryRepository extends JpaRepository<SubstitutionHistory, Long> {
    
    /**
     * Tìm lịch sử thay thế của một xe
     */
    List<SubstitutionHistory> findByCustomerVehicleIdOrderByReplacedAtDesc(Long customerVehicleId);
    
    /**
     * Tìm lịch sử thay thế của một sản phẩm
     */
    List<SubstitutionHistory> findByProductIdOrderByReplacedAtDesc(Long productId);
    
    /**
     * Tìm lịch sử thay thế của một supplier
     */
    List<SubstitutionHistory> findBySupplierIdOrderByReplacedAtDesc(Long supplierId);
    
    /**
     * Tìm lịch sử thay thế theo khách hàng
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.customerVehicle.customer.id = :customerId " +
           "ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Tìm lịch sử thay thế theo khoảng thời gian
     */
    List<SubstitutionHistory> findByReplacedAtBetweenOrderByReplacedAtDesc(
            LocalDateTime fromDate, LocalDateTime toDate);
    
    /**
     * Tìm lịch sử thay thế theo lý do
     */
    List<SubstitutionHistory> findByReplacementReason(SubstitutionHistory.ReplacementReason reason);
    
    /**
     * Tìm lịch sử thay thế theo đánh giá
     */
    List<SubstitutionHistory> findByRating(Integer rating);
    
    /**
     * Tìm lịch sử thay thế có đánh giá cao (4-5 sao)
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.rating >= 4 " +
           "ORDER BY sh.rating DESC, sh.replacedAt DESC")
    List<SubstitutionHistory> findHighRatedSubstitutions();
    
    /**
     * Tìm lịch sử thay thế có đánh giá thấp (1-2 sao)
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.rating <= 2 " +
           "ORDER BY sh.rating ASC, sh.replacedAt DESC")
    List<SubstitutionHistory> findLowRatedSubstitutions();
    
    /**
     * Tìm lịch sử thay thế theo tình trạng linh kiện cũ
     */
    List<SubstitutionHistory> findByOldPartCondition(SubstitutionHistory.PartCondition condition);
    
    /**
     * Tìm lịch sử thay thế có bảo hành
     */
    List<SubstitutionHistory> findByHasWarrantyTrue();
    
    /**
     * Tìm lịch sử thay thế đang trong thời hạn bảo hành
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.hasWarranty = true " +
           "AND sh.warrantyExpiryDate > CURRENT_TIMESTAMP " +
           "ORDER BY sh.warrantyExpiryDate ASC")
    List<SubstitutionHistory> findActiveWarrantySubstitutions();
    
    /**
     * Tìm lịch sử thay thế đã claim bảo hành
     */
    List<SubstitutionHistory> findByWarrantyClaimedTrue();
    
    /**
     * Tìm lịch sử thay thế theo kết quả claim bảo hành
     */
    List<SubstitutionHistory> findByWarrantyClaimResult(SubstitutionHistory.WarrantyClaimResult result);
    
    /**
     * Tìm lịch sử thay thế theo khoảng giá
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.purchasePrice BETWEEN :minPrice AND :maxPrice " +
           "ORDER BY sh.purchasePrice ASC")
    List<SubstitutionHistory> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                              @Param("maxPrice") BigDecimal maxPrice);
    
    /**
     * Tìm lịch sử thay thế theo thời gian sử dụng
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.usageDurationMonths >= :minMonths " +
           "ORDER BY sh.usageDurationMonths DESC")
    List<SubstitutionHistory> findByUsageDurationGreaterThan(@Param("minMonths") Integer minMonths);
    
    /**
     * Tìm lịch sử thay thế theo số km sử dụng
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.usageMileage >= :minMileage " +
           "ORDER BY sh.usageMileage DESC")
    List<SubstitutionHistory> findByUsageMileageGreaterThan(@Param("minMileage") Integer minMileage);
    
    /**
     * Tìm lịch sử thay thế theo mẫu xe
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.customerVehicle.vehicleModel.id = :vehicleModelId " +
           "ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findByVehicleModelId(@Param("vehicleModelId") Long vehicleModelId);
    
    /**
     * Tìm lịch sử thay thế theo hãng xe
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.customerVehicle.vehicleModel.brand = :brand " +
           "ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findByVehicleBrand(@Param("brand") String brand);
    
    /**
     * Tìm lịch sử thay thế theo loại sử dụng xe
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.customerVehicle.usageType = :usageType " +
           "ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findByVehicleUsageType(@Param("usageType") String usageType);
    
    /**
     * Tìm lịch sử thay thế theo category sản phẩm
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.product.category.id = :categoryId " +
           "ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findByProductCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * Tìm lịch sử thay thế theo brand sản phẩm
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.product.brand = :brand " +
           "ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findByProductBrand(@Param("brand") String brand);
    
    /**
     * Tìm lịch sử thay thế theo vehicle type sản phẩm
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.product.vehicleType = :vehicleType " +
           "ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findByProductVehicleType(@Param("vehicleType") String vehicleType);
    
    /**
     * Tìm lịch sử thay thế gần đây
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.replacedAt >= :fromDate " +
           "ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findRecentSubstitutions(@Param("fromDate") LocalDateTime fromDate);
    
    /**
     * Tìm lịch sử thay thế có chi phí cao nhất
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.replacementCost > 0 " +
           "ORDER BY sh.replacementCost DESC")
    Page<SubstitutionHistory> findHighestCostSubstitutions(Pageable pageable);
    
    /**
     * Tìm lịch sử thay thế có thời gian sử dụng dài nhất
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.usageDurationMonths > 0 " +
           "ORDER BY sh.usageDurationMonths DESC")
    Page<SubstitutionHistory> findLongestUsageSubstitutions(Pageable pageable);
    
    /**
     * Tìm lịch sử thay thế có km sử dụng cao nhất
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.usageMileage > 0 " +
           "ORDER BY sh.usageMileage DESC")
    Page<SubstitutionHistory> findHighestMileageSubstitutions(Pageable pageable);
    
    /**
     * Tìm lịch sử thay thế theo tên khách hàng (fuzzy search)
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE LOWER(sh.customerVehicle.customer.name) " +
           "LIKE LOWER(CONCAT('%', :customerName, '%')) ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findByCustomerNameContainingIgnoreCase(@Param("customerName") String customerName);
    
    /**
     * Tìm lịch sử thay thế theo tên sản phẩm (fuzzy search)
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE LOWER(sh.product.name) " +
           "LIKE LOWER(CONCAT('%', :productName, '%')) ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findByProductNameContainingIgnoreCase(@Param("productName") String productName);
    
    /**
     * Tìm lịch sử thay thế theo tên supplier (fuzzy search)
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE sh.supplier IS NOT NULL " +
           "AND LOWER(sh.supplier.name) LIKE LOWER(CONCAT('%', :supplierName, '%')) " +
           "ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findBySupplierNameContainingIgnoreCase(@Param("supplierName") String supplierName);
    
    /**
     * Tìm lịch sử thay thế theo biển số xe (fuzzy search)
     */
    @Query("SELECT sh FROM SubstitutionHistory sh WHERE LOWER(sh.customerVehicle.licensePlate) " +
           "LIKE LOWER(CONCAT('%', :licensePlate, '%')) ORDER BY sh.replacedAt DESC")
    List<SubstitutionHistory> findByLicensePlateContainingIgnoreCase(@Param("licensePlate") String licensePlate);
    
    /**
     * Đếm số lần thay thế của một xe
     */
    long countByCustomerVehicleId(Long customerVehicleId);
    
    /**
     * Đếm số lần thay thế của một sản phẩm
     */
    long countByProductId(Long productId);
    
    /**
     * Đếm số lần thay thế của một supplier
     */
    long countBySupplierId(Long supplierId);
    
    /**
     * Đếm số lần thay thế theo lý do
     */
    long countByReplacementReason(SubstitutionHistory.ReplacementReason reason);
    
    /**
     * Đếm số lần thay thế có bảo hành
     */
    long countByHasWarrantyTrue();
    
    /**
     * Đếm số lần thay thế đã claim bảo hành
     */
    long countByWarrantyClaimedTrue();
    
    /**
     * Tính đánh giá trung bình của một sản phẩm
     */
    @Query("SELECT AVG(sh.rating) FROM SubstitutionHistory sh WHERE sh.product.id = :productId " +
           "AND sh.rating IS NOT NULL")
    Double getAverageRatingByProductId(@Param("productId") Long productId);
    
    /**
     * Tính đánh giá trung bình của một supplier
     */
    @Query("SELECT AVG(sh.rating) FROM SubstitutionHistory sh WHERE sh.supplier.id = :supplierId " +
           "AND sh.rating IS NOT NULL")
    Double getAverageRatingBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * Tính thời gian sử dụng trung bình của một sản phẩm
     */
    @Query("SELECT AVG(sh.usageDurationMonths) FROM SubstitutionHistory sh WHERE sh.product.id = :productId " +
           "AND sh.usageDurationMonths IS NOT NULL")
    Double getAverageUsageDurationByProductId(@Param("productId") Long productId);
    
    /**
     * Tính km sử dụng trung bình của một sản phẩm
     */
    @Query("SELECT AVG(sh.usageMileage) FROM SubstitutionHistory sh WHERE sh.product.id = :productId " +
           "AND sh.usageMileage IS NOT NULL")
    Double getAverageUsageMileageByProductId(@Param("productId") Long productId);
} 