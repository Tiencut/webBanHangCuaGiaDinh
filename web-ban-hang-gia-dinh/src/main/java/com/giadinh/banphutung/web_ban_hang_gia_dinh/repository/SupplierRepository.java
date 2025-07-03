package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier.SupplierStatus;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    // Tìm supplier theo code
    Optional<Supplier> findByCode(String code);
    
    // Tìm supplier theo phone
    Optional<Supplier> findByPhone(String phone);
    
    // Tìm supplier theo email
    Optional<Supplier> findByEmail(String email);
    
    // Tìm supplier theo status
    List<Supplier> findByStatus(SupplierStatus status);
    
    // Tìm supplier active
    List<Supplier> findByStatusOrderByNameAsc(SupplierStatus status);
    
    // Tìm supplier theo rating
    @Query("SELECT s FROM Supplier s WHERE s.rating >= :minRating ORDER BY s.rating DESC")
    List<Supplier> findByRatingGreaterThanEqual(@Param("minRating") Double minRating);
    
    // Tìm supplier theo tên (không phân biệt hoa thường)
    @Query("SELECT s FROM Supplier s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(s.code) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Supplier> findByNameOrCodeContainingIgnoreCase(@Param("name") String name);
    
    // Tìm supplier theo thời gian giao hàng
    List<Supplier> findByDeliveryTimeDaysLessThanEqualOrderByDeliveryTimeDaysAsc(Integer maxDays);
    
    // Tìm supplier theo địa chỉ
    @Query("SELECT s FROM Supplier s WHERE LOWER(s.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<Supplier> findByAddressContainingIgnoreCase(@Param("address") String address);
    
    // Kiểm tra code đã tồn tại chưa
    boolean existsByCode(String code);
    
    // Kiểm tra phone đã tồn tại chưa
    boolean existsByPhone(String phone);
    
    // Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);
    
    // Tìm supplier theo tên (đơn giản)
    List<Supplier> findByNameContainingIgnoreCase(String name);
    
    // Tìm supplier theo code (đơn giản)
    List<Supplier> findByCodeContainingIgnoreCase(String code);
    

    
    // Tìm top suppliers
    @Query("SELECT s FROM Supplier s WHERE s.status = 'ACTIVE' ORDER BY s.rating DESC, s.name ASC")
    List<Supplier> findTopSuppliers(org.springframework.data.domain.Pageable pageable);
    
    // Đếm supplier theo status
    Long countByStatus(SupplierStatus status);
    
    // Tìm top supplier theo rating
    @Query("SELECT s FROM Supplier s WHERE s.status = 'ACTIVE' ORDER BY s.rating DESC")
    List<Supplier> findTopSuppliersByRating(org.springframework.data.domain.Pageable pageable);
}
