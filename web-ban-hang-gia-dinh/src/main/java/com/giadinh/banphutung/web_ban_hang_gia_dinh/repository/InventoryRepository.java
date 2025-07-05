package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Inventory;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    // Tìm inventory theo product
    List<Inventory> findByProduct(Product product);
    
    // Tìm inventory theo product ID
    List<Inventory> findByProductId(Long productId);
    
    // Tìm inventory theo product và supplier
    Optional<Inventory> findByProductAndSupplier(Product product, Supplier supplier);
    
    // Tìm inventory có sẵn (available status và quantity > 0)
    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.status = 'AVAILABLE' AND i.quantity > 0")
    List<Inventory> findAvailableInventoryByProductId(@Param("productId") Long productId);
    
    // Tính tổng số lượng có sẵn của một sản phẩm từ tất cả nguồn
    @Query("SELECT COALESCE(SUM(i.quantity - COALESCE(i.reservedQuantity, 0)), 0) FROM Inventory i WHERE i.product.id = :productId AND i.status = 'AVAILABLE'")
    Integer getTotalAvailableQuantityByProductId(@Param("productId") Long productId);
    
    // Tính tổng số lượng trong kho (bao gồm cả reserved) của một sản phẩm
    @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM Inventory i WHERE i.product.id = :productId AND i.status = 'AVAILABLE'")
    Integer getTotalQuantityByProductId(@Param("productId") Long productId);
    
    // Tính tổng số lượng đã đặt trước
    @Query("SELECT COALESCE(SUM(i.reservedQuantity), 0) FROM Inventory i WHERE i.product.id = :productId AND i.status = 'AVAILABLE'")
    Integer getTotalReservedQuantityByProductId(@Param("productId") Long productId);
    
    // Tìm inventory theo batch number
    Optional<Inventory> findByBatchNumber(String batchNumber);
    
    // Tìm inventory theo location
    List<Inventory> findByLocationContainingIgnoreCase(String location);
    
    // Tìm inventory với thông tin chi tiết (join với supplier)
    @Query("SELECT i FROM Inventory i LEFT JOIN FETCH i.supplier WHERE i.product.id = :productId")
    List<Inventory> findByProductIdWithSupplier(@Param("productId") Long productId);
    
    // Tìm inventory theo supplier
    List<Inventory> findBySupplier(Supplier supplier);
    
    // Tìm inventory theo supplier ID
    List<Inventory> findBySupplierId(Long supplierId);
    
    // Tìm inventory có quantity > 0
    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.quantity > 0 AND i.status = 'AVAILABLE'")
    List<Inventory> findByProductIdWithStock(@Param("productId") Long productId);
    
    // Đếm số lượng inventory entries cho một product
    Long countByProductId(Long productId);
    
    // Đếm số lượng supplier khác nhau cho một product
    @Query("SELECT COUNT(DISTINCT i.supplier.id) FROM Inventory i WHERE i.product.id = :productId AND i.status = 'AVAILABLE'")
    Long countDistinctSuppliersByProductId(@Param("productId") Long productId);
}
