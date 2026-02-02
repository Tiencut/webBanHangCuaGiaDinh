package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Inventory;

/**
 * InventoryRepository - Repository cho quản lý tồn kho theo supplier
 * 
 * Cung cấp các query methods để:
 * - Quản lý tồn kho chi tiết theo supplier
 * - Cảnh báo tồn kho thấp và cần đặt hàng
 * - Tính toán vòng quay tồn kho
 * - Theo dõi lịch sử nhập/xuất
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    /**
     * Tìm tồn kho theo product và supplier
     */
    Optional<Inventory> findByProductIdAndSupplierId(Long productId, Long supplierId);
    
    /**
     * Tìm tất cả tồn kho của một sản phẩm
     */
    List<Inventory> findByProductIdOrderByCurrentQuantityDesc(Long productId);

       @Lock(LockModeType.PESSIMISTIC_WRITE)
       @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.supplier.id = :supplierId")
       Optional<Inventory> findByProductIdAndSupplierIdForUpdate(@Param("productId") Long productId, @Param("supplierId") Long supplierId);

       @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(i.currentQuantity),0) FROM Inventory i WHERE i.product.id = :productId")
       Integer sumCurrentQuantityByProductId(@Param("productId") Long productId);
    
    /**
     * Tìm tồn kho đang hoạt động của một sản phẩm
     */
    List<Inventory> findByProductIdAndIsActiveTrueOrderByCurrentQuantityDesc(Long productId);
    
    /**
     * Tìm tồn kho của một supplier
     */
    List<Inventory> findBySupplierIdOrderByCurrentQuantityDesc(Long supplierId);
    
    /**
     * Tìm tồn kho đang hoạt động của một supplier
     */
    List<Inventory> findBySupplierIdAndIsActiveTrueOrderByCurrentQuantityDesc(Long supplierId);
    
    /**
     * Tìm tồn kho theo trạng thái
     */
    List<Inventory> findByStockStatusOrderByProductNameAsc(Inventory.StockStatus stockStatus);
    
    /**
     * Tìm tồn kho hết hàng
     */
    @Query("SELECT i FROM Inventory i WHERE i.currentQuantity = 0 AND i.isActive = true " +
           "ORDER BY i.product.name, i.supplier.name")
    List<Inventory> findOutOfStockItems();
    
    /**
     * Tìm tồn kho sắp hết hàng
     */
    @Query("SELECT i FROM Inventory i WHERE i.currentQuantity <= i.minStockLevel " +
           "AND i.currentQuantity > 0 AND i.isActive = true " +
           "ORDER BY i.product.name, i.supplier.name")
    List<Inventory> findLowStockItems();
    
    /**
     * Tìm tồn kho cần đặt hàng lại
     */
    @Query("SELECT i FROM Inventory i WHERE i.currentQuantity <= i.reorderPoint " +
           "AND i.isActive = true ORDER BY i.product.name, i.supplier.name")
    List<Inventory> findItemsNeedingReorder();
    
    /**
     * Tìm tồn kho theo khoảng số lượng
     */
    @Query("SELECT i FROM Inventory i WHERE i.currentQuantity BETWEEN :minQuantity AND :maxQuantity " +
           "AND i.isActive = true ORDER BY i.currentQuantity ASC")
    List<Inventory> findByQuantityRange(@Param("minQuantity") Integer minQuantity, 
                                       @Param("maxQuantity") Integer maxQuantity);
    
    /**
     * Tìm tồn kho theo khoảng giá trị
     */
    @Query("SELECT i FROM Inventory i WHERE i.inventoryValue BETWEEN :minValue AND :maxValue " +
           "AND i.isActive = true ORDER BY i.inventoryValue ASC")
    List<Inventory> findByValueRange(@Param("minValue") BigDecimal minValue, 
                                    @Param("maxValue") BigDecimal maxValue);
    
    /**
     * Tìm tồn kho theo vòng quay
     */
    @Query("SELECT i FROM Inventory i WHERE i.turnoverRate >= :minTurnover " +
           "AND i.isActive = true ORDER BY i.turnoverRate DESC")
    List<Inventory> findByTurnoverRateGreaterThan(@Param("minTurnover") Double minTurnover);
    
    /**
     * Tìm tồn kho theo số ngày tồn kho
     */
    @Query("SELECT i FROM Inventory i WHERE i.averageDaysInStock <= :maxDays " +
           "AND i.isActive = true ORDER BY i.averageDaysInStock ASC")
    List<Inventory> findByAverageDaysInStockLessThan(@Param("maxDays") Integer maxDays);
    
    /**
     * Tìm tồn kho theo category
     */
    @Query("SELECT i FROM Inventory i WHERE i.product.category.id = :categoryId " +
           "AND i.isActive = true ORDER BY i.product.name, i.supplier.name")
    List<Inventory> findByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * Tìm tồn kho theo brand
     */
    @Query("SELECT i FROM Inventory i WHERE i.product.brand = :brand " +
           "AND i.isActive = true ORDER BY i.product.name, i.supplier.name")
    List<Inventory> findByBrand(@Param("brand") String brand);
    
    /**
     * Tìm tồn kho theo vehicle type
     */
    @Query("SELECT i FROM Inventory i WHERE i.product.vehicleType = :vehicleType " +
           "AND i.isActive = true ORDER BY i.product.name, i.supplier.name")
    List<Inventory> findByVehicleType(@Param("vehicleType") String vehicleType);
    
    /**
     * Tìm tồn kho có nhập hàng gần đây
     */
    @Query("SELECT i FROM Inventory i WHERE i.lastReceivedDate IS NOT NULL " +
           "ORDER BY i.lastReceivedDate DESC")
    Page<Inventory> findRecentlyReceivedItems(Pageable pageable);
    
    /**
     * Tìm tồn kho có xuất hàng gần đây
     */
    @Query("SELECT i FROM Inventory i WHERE i.lastShippedDate IS NOT NULL " +
           "ORDER BY i.lastShippedDate DESC")
    Page<Inventory> findRecentlyShippedItems(Pageable pageable);
    
    /**
     * Tìm tồn kho chưa bao giờ nhập hàng
     */
    @Query("SELECT i FROM Inventory i WHERE i.lastReceivedDate IS NULL " +
           "AND i.isActive = true ORDER BY i.product.name, i.supplier.name")
    List<Inventory> findItemsNeverReceived();
    
    /**
     * Tìm tồn kho chưa bao giờ xuất hàng
     */
    @Query("SELECT i FROM Inventory i WHERE i.lastShippedDate IS NULL " +
           "AND i.isActive = true ORDER BY i.product.name, i.supplier.name")
    List<Inventory> findItemsNeverShipped();
    
    /**
     * Tìm tồn kho theo khoảng giá nhập trung bình
     */
    @Query("SELECT i FROM Inventory i WHERE i.averageCost BETWEEN :minCost AND :maxCost " +
           "AND i.isActive = true ORDER BY i.averageCost ASC")
    List<Inventory> findByAverageCostRange(@Param("minCost") BigDecimal minCost, 
                                          @Param("maxCost") BigDecimal maxCost);
    
    /**
     * Tìm tồn kho có vòng quay cao nhất
     */
    @Query("SELECT i FROM Inventory i WHERE i.turnoverRate > 0 " +
           "ORDER BY i.turnoverRate DESC")
    Page<Inventory> findHighestTurnoverItems(Pageable pageable);
    
    /**
     * Tìm tồn kho có vòng quay thấp nhất
     */
    @Query("SELECT i FROM Inventory i WHERE i.turnoverRate > 0 " +
           "ORDER BY i.turnoverRate ASC")
    Page<Inventory> findLowestTurnoverItems(Pageable pageable);
    
    /**
     * Tìm tồn kho có số ngày tồn kho cao nhất
     */
    @Query("SELECT i FROM Inventory i WHERE i.averageDaysInStock > 0 " +
           "ORDER BY i.averageDaysInStock DESC")
    Page<Inventory> findLongestStockItems(Pageable pageable);
    
    /**
     * Tìm tồn kho có số ngày tồn kho thấp nhất
     */
    @Query("SELECT i FROM Inventory i WHERE i.averageDaysInStock > 0 " +
           "ORDER BY i.averageDaysInStock ASC")
    Page<Inventory> findShortestStockItems(Pageable pageable);
    
    /**
     * Tìm tồn kho có giá trị cao nhất
     */
    @Query("SELECT i FROM Inventory i WHERE i.inventoryValue > 0 " +
           "ORDER BY i.inventoryValue DESC")
    Page<Inventory> findHighestValueItems(Pageable pageable);
    
    /**
     * Tìm tồn kho có số lượng cao nhất
     */
    @Query("SELECT i FROM Inventory i WHERE i.currentQuantity > 0 " +
           "ORDER BY i.currentQuantity DESC")
    Page<Inventory> findHighestQuantityItems(Pageable pageable);
    
    /**
     * Tìm tồn kho theo tên sản phẩm (fuzzy search)
     */
    @Query("SELECT i FROM Inventory i WHERE LOWER(i.product.name) LIKE LOWER(CONCAT('%', :productName, '%')) " +
           "AND i.isActive = true ORDER BY i.product.name, i.supplier.name")
    List<Inventory> findByProductNameContainingIgnoreCase(@Param("productName") String productName);
    
    /**
     * Tìm tồn kho theo tên supplier (fuzzy search)
     */
    @Query("SELECT i FROM Inventory i WHERE LOWER(i.supplier.name) LIKE LOWER(CONCAT('%', :supplierName, '%')) " +
           "AND i.isActive = true ORDER BY i.supplier.name, i.product.name")
    List<Inventory> findBySupplierNameContainingIgnoreCase(@Param("supplierName") String supplierName);
    
    /**
     * Tìm tồn kho theo ghi chú (fuzzy search)
     */
    @Query("SELECT i FROM Inventory i WHERE LOWER(i.notes) LIKE LOWER(CONCAT('%', :notes, '%')) " +
           "AND i.isActive = true ORDER BY i.product.name, i.supplier.name")
    List<Inventory> findByNotesContainingIgnoreCase(@Param("notes") String notes);
    
    /**
     * Đếm số tồn kho của một sản phẩm
     */
    long countByProductIdAndIsActiveTrue(Long productId);
    
    /**
     * Đếm số tồn kho của một supplier
     */
    long countBySupplierIdAndIsActiveTrue(Long supplierId);
    
    /**
     * Đếm số tồn kho theo trạng thái
     */
    long countByStockStatusAndIsActiveTrue(Inventory.StockStatus stockStatus);
    
    /**
     * Đếm số tồn kho hết hàng
     */
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.currentQuantity = 0 AND i.isActive = true")
    long countOutOfStockItems();
    
    /**
     * Đếm số tồn kho sắp hết hàng
     */
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.currentQuantity <= i.minStockLevel " +
           "AND i.currentQuantity > 0 AND i.isActive = true")
    long countLowStockItems();
    
    /**
     * Đếm số tồn kho cần đặt hàng lại
     */
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.currentQuantity <= i.reorderPoint " +
           "AND i.isActive = true")
    long countItemsNeedingReorder();
    
    /**
     * Tính tổng giá trị tồn kho
     */
    @Query("SELECT SUM(i.inventoryValue) FROM Inventory i WHERE i.isActive = true")
    BigDecimal getTotalInventoryValue();
    
    /**
     * Tính tổng giá trị tồn kho theo category
     */
    @Query("SELECT SUM(i.inventoryValue) FROM Inventory i WHERE i.product.category.id = :categoryId " +
           "AND i.isActive = true")
    BigDecimal getTotalInventoryValueByCategory(@Param("categoryId") Long categoryId);
    
    /**
     * Tính tổng giá trị tồn kho theo supplier
     */
    @Query("SELECT SUM(i.inventoryValue) FROM Inventory i WHERE i.supplier.id = :supplierId " +
           "AND i.isActive = true")
    BigDecimal getTotalInventoryValueBySupplier(@Param("supplierId") Long supplierId);
    
    /**
     * Tính tổng số lượng tồn kho
     */
    @Query("SELECT SUM(i.currentQuantity) FROM Inventory i WHERE i.isActive = true")
    Long getTotalInventoryQuantity();
    
    /**
     * Tính tổng số lượng tồn kho theo category
     */
    @Query("SELECT SUM(i.currentQuantity) FROM Inventory i WHERE i.product.category.id = :categoryId " +
           "AND i.isActive = true")
    Long getTotalInventoryQuantityByCategory(@Param("categoryId") Long categoryId);
    
    /**
     * Tính tổng số lượng tồn kho theo supplier
     */
    @Query("SELECT SUM(i.currentQuantity) FROM Inventory i WHERE i.supplier.id = :supplierId " +
           "AND i.isActive = true")
    Long getTotalInventoryQuantityBySupplier(@Param("supplierId") Long supplierId);
    
    /**
     * Tính vòng quay tồn kho trung bình
     */
    @Query("SELECT AVG(i.turnoverRate) FROM Inventory i WHERE i.turnoverRate > 0 AND i.isActive = true")
    Double getAverageTurnoverRate();
    
    /**
     * Tính số ngày tồn kho trung bình
     */
    @Query("SELECT AVG(i.averageDaysInStock) FROM Inventory i WHERE i.averageDaysInStock > 0 AND i.isActive = true")
    Double getAverageDaysInStock();
    
    /**
     * Tìm category có tồn kho cao nhất
     */
    @Query("SELECT i.product.category.id, SUM(i.inventoryValue) as totalValue " +
           "FROM Inventory i WHERE i.isActive = true " +
           "GROUP BY i.product.category.id ORDER BY totalValue DESC")
    List<Object[]> findCategoriesWithHighestInventory();
    
    /**
     * Tìm supplier có tồn kho cao nhất
     */
    @Query("SELECT i.supplier.id, SUM(i.inventoryValue) as totalValue " +
           "FROM Inventory i WHERE i.isActive = true " +
           "GROUP BY i.supplier.id ORDER BY totalValue DESC")
    List<Object[]> findSuppliersWithHighestInventory();
    
    /**
     * Tìm sản phẩm có tồn kho cao nhất
     */
    @Query("SELECT i.product.id, SUM(i.inventoryValue) as totalValue " +
           "FROM Inventory i WHERE i.isActive = true " +
           "GROUP BY i.product.id ORDER BY totalValue DESC")
    List<Object[]> findProductsWithHighestInventory();
    
    // === Các method bổ sung cho InventoryService ===
    
    /**
     * Tìm tất cả tồn kho đang active
     */
    List<Inventory> findByIsActiveTrue();
    
    /**
     * Tìm tồn kho đang active với phân trang
     */
    Page<Inventory> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Tìm tồn kho theo id và active
     */
    Optional<Inventory> findByIdAndIsActiveTrue(Long id);
    
    /**
     * Tìm tồn kho theo product, supplier và active
     */
    Optional<Inventory> findByProductIdAndSupplierIdAndIsActiveTrue(Long productId, Long supplierId);
    
    // === Các method bổ sung cho InventoryService ===
    
    /**
     * Tìm tồn kho theo product và active
     */
    List<Inventory> findByProductIdAndIsActiveTrue(Long productId);
    
    /**
     * Tìm tồn kho theo supplier và active
     */
    List<Inventory> findBySupplierIdAndIsActiveTrue(Long supplierId);
    
    /**
     * Tìm tồn kho theo available quantity và active
     */
    List<Inventory> findByAvailableQuantityLessThanEqualAndIsActiveTrue(Integer quantity);
    
    /**
     * Tìm kiếm tồn kho theo keyword
     */
    @Query("SELECT i FROM Inventory i WHERE i.isActive = true AND (LOWER(i.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(i.supplier.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Inventory> searchInventories(@Param("keyword") String keyword);
    
    /**
     * Tìm tồn kho theo location và active
     */
    List<Inventory> findByLocationContainingIgnoreCaseAndIsActiveTrue(String location);
    
    /**
     * Tìm tồn kho sắp hết hạn
     */
    @Query("SELECT i FROM Inventory i WHERE i.isActive = true AND i.expiryDate IS NOT NULL " +
           "AND i.expiryDate <= :threshold ORDER BY i.expiryDate ASC")
    List<Inventory> findExpiringInventories(@Param("threshold") LocalDateTime threshold);
} 