package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Tìm order theo customer
    List<Order> findByCustomerId(Long customerId);
    
    // Tìm order theo customer với phân trang
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
    
    // Tìm order theo status
    List<Order> findByStatus(OrderStatus status);
    
    // Tìm order theo ngày tạo
    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Tìm order theo tháng/năm
    @Query("SELECT o FROM Order o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month")
    List<Order> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
    
    // Tìm order theo ngày cụ thể
    @Query("SELECT o FROM Order o WHERE o.orderDate = :date")
    List<Order> findByOrderDate(@Param("date") LocalDate date);
    
    // Tìm order có tổng tiền >= số tiền
    List<Order> findByTotalAmountGreaterThanEqual(BigDecimal minAmount);
    
    // Tìm order theo mã đơn hàng
    Optional<Order> findByOrderCode(String orderCode);
    
    // Tìm order active (chưa bị xóa)
    @Query("SELECT o FROM Order o WHERE o.isDeleted = false ORDER BY o.orderDate DESC")
    List<Order> findAllActive();
    
    // Tìm order active với phân trang
    @Query("SELECT o FROM Order o WHERE o.isDeleted = false ORDER BY o.orderDate DESC")
    Page<Order> findAllActive(Pageable pageable);
    
    // Đếm order theo status
    Long countByStatus(OrderStatus status);
    
    // Đếm order theo ngày
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate = :date AND o.isDeleted = false")
    Long countByOrderDate(@Param("date") LocalDate date);
    
    // Tính tổng doanh thu theo ngày
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate = :date AND o.status != 'CANCELLED' AND o.isDeleted = false")
    BigDecimal sumTotalAmountByOrderDate(@Param("date") LocalDate date);
    
    // Tính tổng doanh thu theo tháng
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month AND o.status != 'CANCELLED' AND o.isDeleted = false")
    BigDecimal sumTotalAmountByYearAndMonth(@Param("year") int year, @Param("month") int month);
    
    // Tính tổng doanh thu theo khoảng thời gian
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.status != 'CANCELLED' AND o.isDeleted = false")
    BigDecimal sumTotalAmountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Top customer theo số đơn hàng
    @Query("SELECT o.customer.id, COUNT(o) FROM Order o WHERE o.isDeleted = false GROUP BY o.customer.id ORDER BY COUNT(o) DESC")
    List<Object[]> findTopCustomersByOrderCount(Pageable pageable);
    
    // Top customer theo tổng tiền
    @Query("SELECT o.customer.id, SUM(o.totalAmount) FROM Order o WHERE o.isDeleted = false AND o.status != 'CANCELLED' GROUP BY o.customer.id ORDER BY SUM(o.totalAmount) DESC")
    List<Object[]> findTopCustomersByTotalAmount(Pageable pageable);
    
    // Order pending cần xử lý (quá 24h chưa confirm)
    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.createdAt < :cutoffTime AND o.isDeleted = false")
    List<Order> findPendingOrdersNeedingAttention(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    // Order đã giao nhưng chưa thanh toán
    @Query("SELECT o FROM Order o WHERE o.status = 'DELIVERED' AND o.paymentStatus != 'PAID' AND o.isDeleted = false")
    List<Order> findDeliveredButUnpaidOrders();
    
    // Thống kê order theo status trong ngày
    @Query("SELECT o.status, COUNT(o) FROM Order o WHERE o.orderDate = :date AND o.isDeleted = false GROUP BY o.status")
    List<Object[]> countOrdersByStatusAndDate(@Param("date") LocalDate date);
    
    // Thống kê order theo status trong tháng
    @Query("SELECT o.status, COUNT(o) FROM Order o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month AND o.isDeleted = false GROUP BY o.status")
    List<Object[]> countOrdersByStatusAndYearMonth(@Param("year") int year, @Param("month") int month);
    
    // Tìm order có delivery date hôm nay (cần giao hàng)
    @Query("SELECT o FROM Order o WHERE o.expectedDeliveryDate = :date AND o.status IN ('CONFIRMED', 'SHIPPED') AND o.isDeleted = false")
    List<Order> findOrdersForDeliveryToday(@Param("date") LocalDate date);
    
    // Tìm order quá hạn giao hàng
    @Query("SELECT o FROM Order o WHERE o.expectedDeliveryDate < :today AND o.status IN ('CONFIRMED', 'SHIPPED') AND o.isDeleted = false")
    List<Order> findOverdueOrders(@Param("today") LocalDate today);

    // Tìm order theo khoảng thời gian (LocalDateTime)
    @Query("SELECT o FROM Order o WHERE o.orderDate >= :start AND o.orderDate <= :end")
    List<Order> findByOrderDateBetween(@Param("start") java.time.LocalDateTime start, @Param("end") java.time.LocalDateTime end);

    // Tìm order theo tổng tiền trong khoảng
    List<Order> findByTotalAmountBetween(BigDecimal minTotal, BigDecimal maxTotal);

    // Tìm kiếm đơn hàng theo từ khóa (orderCode, customer name, v.v.)
    @Query("SELECT o FROM Order o WHERE o.orderCode LIKE %:term% OR o.customer.name LIKE %:term%")
    Page<Order> searchOrders(@Param("term") String term, Pageable pageable);
}
