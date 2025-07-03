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

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer.CustomerStatus;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer.CustomerType;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Tìm customer theo code
    Optional<Customer> findByCode(String code);
    
    // Tìm customer theo phone
    Optional<Customer> findByPhone(String phone);
    
    // Tìm customer theo email
    Optional<Customer> findByEmail(String email);
    
    // Tìm customer theo type
    List<Customer> findByCustomerType(CustomerType customerType);
    
    // Tìm customer theo status
    List<Customer> findByStatus(CustomerStatus status);
    
    // Tìm customer active
    List<Customer> findByStatusOrderByNameAsc(CustomerStatus status);
    
    // Tìm customer VIP
    List<Customer> findByCustomerTypeOrTotalSpentGreaterThanEqual(
        CustomerType customerType, BigDecimal minSpent);
    
    // Tìm customer có công nợ
    @Query("SELECT c FROM Customer c WHERE c.currentDebt > 0 ORDER BY c.currentDebt DESC")
    List<Customer> findCustomersWithDebt();
    
    // Tìm customer theo tên (không phân biệt hoa thường)
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(c.code) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Customer> searchCustomers(@Param("name") String name, Pageable pageable);
    
    // Tìm customer theo địa chỉ
    @Query("SELECT c FROM Customer c WHERE LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<Customer> findByAddressContainingIgnoreCase(@Param("address") String address);
    
    // Tìm customer theo tên (đơn giản)
    List<Customer> findByNameContainingIgnoreCase(String name);
    
    // Tìm customer theo phone (chứa chuỗi)
    List<Customer> findByPhoneContaining(String phone);
    
    // Tìm top customer theo tổng chi tiêu
    @Query("SELECT c FROM Customer c ORDER BY c.totalSpent DESC")
    List<Customer> findTopCustomersBySpending(Pageable pageable);
    
    // Tìm customer chưa mua trong X ngày
    @Query("SELECT c FROM Customer c WHERE c.lastPurchaseDate < :cutoffDate OR c.lastPurchaseDate IS NULL")
    List<Customer> findInactiveCustomers(@Param("cutoffDate") java.time.LocalDate cutoffDate);
    
    // Kiểm tra code đã tồn tại chưa
    boolean existsByCode(String code);
    
    // Kiểm tra phone đã tồn tại chưa
    boolean existsByPhone(String phone);
    
    // Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);
    
    // Đếm customer theo type
    Long countByCustomerType(CustomerType customerType);
    
    // Tổng công nợ của tất cả customer
    @Query("SELECT SUM(c.currentDebt) FROM Customer c")
    BigDecimal getTotalDebt();
}
