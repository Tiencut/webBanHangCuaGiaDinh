package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.OrderStatsResponse;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order.OrderStatus;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.OrderDetail;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.OrderRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OrderService - Service xử lý logic nghiệp vụ đơn hàng
 * 
 * Đây là service layer xử lý tất cả logic nghiệp vụ liên quan đến đơn hàng:
 * - Tạo đơn hàng mới (tự động tạo mã, tính tổng tiền, validate)
 * - Cập nhật trạng thái đơn hàng (pending -> confirmed -> shipped -> delivered)
 * - Hủy đơn hàng (với lý do)
 * - Tính toán doanh thu, thống kê
 * - Xử lý giao hàng, thanh toán
 * 
 * Service này giao tiếp với OrderRepository để thao tác database
 * và có thể gọi các service khác như CustomerService, ProductService, InventoryService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;

    // ========== CRUD Methods cơ bản ==========
    
    // Lấy tất cả orders với phân trang
    @Transactional(readOnly = true)
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAllActive(pageable);
    }
    
    // Tìm order theo ID
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    // Tạo đơn hàng mới
    public Order createOrder(Order order) {
        log.info("Creating new order for customer: {}", order.getCustomer().getId());
        
        // Validate order
        validateOrder(order);
        
        // Tự động tạo order code nếu chưa có
        if (order.getOrderCode() == null || order.getOrderCode().trim().isEmpty()) {
            order.setOrderCode(generateOrderCode());
        }
        
        // Set trạng thái mặc định
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.PENDING);
        }
        
        // Tính tổng tiền từ order details
        calculateTotalAmount(order);
        
        // Set ngày tạo
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        
        // Set expected delivery date (mặc định + 3 ngày)
        if (order.getExpectedDeliveryDate() == null) {
            order.setExpectedDeliveryDate(order.getOrderDate().plusDays(3));
        }
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with code: {}", savedOrder.getOrderCode());
        
        return savedOrder;
    }
    
    // Cập nhật đơn hàng
    public Order updateOrder(Long id, Order orderUpdate) {
        log.info("Updating order with id: {}", id);
        
        Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        // Không cho phép cập nhật order đã delivered hoặc cancelled
        if (existingOrder.getStatus() == OrderStatus.DELIVERED || 
            existingOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Không thể cập nhật đơn hàng đã giao hoặc đã hủy");
        }
        
        // Cập nhật thông tin
        existingOrder.setShippingAddress(orderUpdate.getShippingAddress());
        existingOrder.setNotes(orderUpdate.getNotes());
        existingOrder.setExpectedDeliveryDate(orderUpdate.getExpectedDeliveryDate());
        
        // Tính lại tổng tiền nếu có thay đổi order details
        if (orderUpdate.getOrderDetails() != null) {
            existingOrder.setOrderDetails(orderUpdate.getOrderDetails());
            calculateTotalAmount(existingOrder);
        }
        
        return orderRepository.save(existingOrder);
    }
    
    // Xóa đơn hàng (soft delete)
    public void deleteOrder(Long id) {
        log.info("Deleting order with id: {}", id);
        
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        // Chỉ cho phép xóa order pending
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Chỉ có thể xóa đơn hàng ở trạng thái PENDING");
        }
        
        order.setIsDeleted(true);
        orderRepository.save(order);
        
        log.info("Order {} has been soft deleted", order.getOrderCode());
    }
    
    // ========== Status Management ==========
    
    // Cập nhật trạng thái đơn hàng
    public Order updateOrderStatus(Long id, String status) {
        log.info("Updating order {} status to: {}", id, status);
        
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        
        // Validate chuyển đổi trạng thái
        validateStatusTransition(order.getStatus(), newStatus);
        
        order.setStatus(newStatus);
        
        // Cập nhật ngày tương ứng
        LocalDateTime now = LocalDateTime.now();
        switch (newStatus) {
            case CONFIRMED:
                order.setConfirmedAt(now);
                break;
            case PROCESSING:
                // Processing không cần update timestamp riêng
                break;
            case SHIPPED:
                order.setShippedAt(now);
                break;
            case DELIVERED:
                order.setDeliveredAt(now);
                // Auto update payment status if COD
                if (order.getPaymentMethod() == Order.PaymentMethod.COD) {
                    order.setPaymentStatus(Order.PaymentStatus.PAID);
                }
                break;
            case CANCELLED:
                order.setCancelledAt(now);
                break;
            case RETURNED:
                // Return không cần update timestamp riêng
                break;
            case PENDING:
                // Pending không cần update timestamp riêng
                break;
        }
        
        return orderRepository.save(order);
    }
    
    // Hủy đơn hàng
    public Order cancelOrder(Long id, String reason) {
        log.info("Cancelling order {} with reason: {}", id, reason);
        
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        // Không cho phép hủy đơn đã giao
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BusinessException("Không thể hủy đơn hàng đã được giao");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancelReason(reason);
        
        return orderRepository.save(order);
    }
    
    // ========== Search Methods ==========
    
    // Tìm đơn hàng theo customer
    @Transactional(readOnly = true)
    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    // Tìm đơn hàng theo status
    @Transactional(readOnly = true)
    public List<Order> findByStatus(String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        return orderRepository.findByStatus(orderStatus);
    }
    
    // Tìm đơn hàng theo khoảng ngày
    @Transactional(readOnly = true)
    public List<Order> findByDateRange(LocalDate fromDate, LocalDate toDate) {
        return orderRepository.findByOrderDateBetween(fromDate, toDate);
    }
    
    // Tìm đơn hàng theo mã đơn hàng
    @Transactional(readOnly = true)
    public Optional<Order> findByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    // Tìm đơn hàng theo khách hàng
    @Transactional(readOnly = true)
    public List<Order> findByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // Tìm đơn hàng theo trạng thái
    @Transactional(readOnly = true)
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    // Tìm đơn hàng theo khoảng thời gian (LocalDateTime)
    @Transactional(readOnly = true)
    public List<Order> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByOrderDateBetween(start, end);
    }

    // Tìm đơn hàng theo khoảng tổng tiền
    @Transactional(readOnly = true)
    public List<Order> findByTotalRange(BigDecimal minTotal, BigDecimal maxTotal) {
        return orderRepository.findByTotalAmountBetween(minTotal, maxTotal);
    }

    // Tìm kiếm đơn hàng theo từ khóa (orderCode, customer name, v.v.)
    @Transactional(readOnly = true)
    public Page<Order> searchOrders(String term, Pageable pageable) {
        return orderRepository.searchOrders(term, pageable);
    }

    // Thêm sản phẩm vào đơn hàng
    public OrderDetail addOrderItem(Long orderId, OrderDetail orderDetail) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.getOrderDetails().add(orderDetail);
        calculateTotalAmount(order);
        orderRepository.save(order);
        return orderDetail;
    }

    // Cập nhật sản phẩm trong đơn hàng
    public OrderDetail updateOrderItem(Long orderId, Long itemId, OrderDetail orderDetail) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        OrderDetail existing = order.getOrderDetails().stream()
            .filter(od -> od.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Order item not found: " + itemId));
        existing.setProduct(orderDetail.getProduct());
        existing.setQuantity(orderDetail.getQuantity());
        existing.setUnitPrice(orderDetail.getUnitPrice());
        calculateTotalAmount(order);
        orderRepository.save(order);
        return existing;
    }

    // Xóa sản phẩm khỏi đơn hàng
    public void removeOrderItem(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.getOrderDetails().removeIf(od -> od.getId().equals(itemId));
        calculateTotalAmount(order);
        orderRepository.save(order);
    }
    
    // ========== Statistics Methods ==========
    
    // Thống kê đơn hàng theo ngày
    @Transactional(readOnly = true)
    public OrderStatsResponse getDailyStats(LocalDate date) {
        log.info("Getting daily stats for date: {}", date);
        // Đếm tổng đơn hàng
        Long totalOrders = orderRepository.countByOrderDate(date);
        // Tính tổng doanh thu
        BigDecimal totalRevenue = orderRepository.sumTotalAmountByOrderDate(date);
        // Đếm theo từng status
        List<Object[]> statusCounts = orderRepository.countOrdersByStatusAndDate(date);
        int pendingOrders = 0, confirmedOrders = 0, processingOrders = 0, shippedOrders = 0, deliveredOrders = 0, cancelledOrders = 0, returnedOrders = 0;
        for (Object[] row : statusCounts) {
            OrderStatus status = (OrderStatus) row[0];
            Long count = (Long) row[1];
            switch (status) {
                case PENDING: pendingOrders = count.intValue(); break;
                case CONFIRMED: confirmedOrders = count.intValue(); break;
                case PROCESSING: processingOrders = count.intValue(); break;
                case SHIPPED: shippedOrders = count.intValue(); break;
                case DELIVERED: deliveredOrders = count.intValue(); break;
                case CANCELLED: cancelledOrders = count.intValue(); break;
                case RETURNED: returnedOrders = count.intValue(); break;
            }
        }
        OrderStatsResponse stats = new OrderStatsResponse();
        stats.setDate(date);
        stats.setTotalOrders(totalOrders.intValue());
        stats.setPendingOrders(pendingOrders);
        stats.setConfirmedOrders(confirmedOrders);
        stats.setProcessingOrders(processingOrders);
        stats.setShippedOrders(shippedOrders);
        stats.setDeliveredOrders(deliveredOrders);
        stats.setCancelledOrders(cancelledOrders);
        stats.setReturnedOrders(returnedOrders);
        stats.setTotalRevenue(totalRevenue);
        return stats;
    }

    // Thống kê đơn hàng theo tháng
    @Transactional(readOnly = true)
    public OrderStatsResponse getMonthlyStats(int year, int month) {
        log.info("Getting monthly stats for {}/{}", month, year);
        // Tính tổng doanh thu
        BigDecimal totalRevenue = orderRepository.sumTotalAmountByYearAndMonth(year, month);
        // Đếm theo từng status
        List<Object[]> statusCounts = orderRepository.countOrdersByStatusAndYearMonth(year, month);
        int totalOrders = 0, pendingOrders = 0, confirmedOrders = 0, processingOrders = 0, shippedOrders = 0, deliveredOrders = 0, cancelledOrders = 0, returnedOrders = 0;
        for (Object[] row : statusCounts) {
            OrderStatus status = (OrderStatus) row[0];
            Long count = (Long) row[1];
            totalOrders += count.intValue();
            switch (status) {
                case PENDING: pendingOrders = count.intValue(); break;
                case CONFIRMED: confirmedOrders = count.intValue(); break;
                case PROCESSING: processingOrders = count.intValue(); break;
                case SHIPPED: shippedOrders = count.intValue(); break;
                case DELIVERED: deliveredOrders = count.intValue(); break;
                case CANCELLED: cancelledOrders = count.intValue(); break;
                case RETURNED: returnedOrders = count.intValue(); break;
            }
        }
        OrderStatsResponse stats = new OrderStatsResponse();
        stats.setDate(LocalDate.of(year, month, 1));
        stats.setTotalOrders(totalOrders);
        stats.setPendingOrders(pendingOrders);
        stats.setConfirmedOrders(confirmedOrders);
        stats.setProcessingOrders(processingOrders);
        stats.setShippedOrders(shippedOrders);
        stats.setDeliveredOrders(deliveredOrders);
        stats.setCancelledOrders(cancelledOrders);
        stats.setReturnedOrders(returnedOrders);
        stats.setTotalRevenue(totalRevenue);
        return stats;
    }
    
    // ========== Helper Methods ==========
    
    // Validate đơn hàng
    private void validateOrder(Order order) {
        if (order.getCustomer() == null) {
            throw new BusinessException("Order phải có customer");
        }
        
        if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            throw new BusinessException("Order phải có ít nhất 1 sản phẩm");
        }
        
        for (OrderDetail detail : order.getOrderDetails()) {
            if (detail.getProduct() == null) {
                throw new BusinessException("Order detail phải có product");
            }
            if (detail.getQuantity() <= 0) {
                throw new BusinessException("Số lượng phải lớn hơn 0");
            }
            if (detail.getUnitPrice() == null || detail.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Giá phải lớn hơn 0");
            }
        }
    }
    
    // Tính tổng tiền
    private void calculateTotalAmount(Order order) {
        BigDecimal total = order.getOrderDetails().stream()
            .map(detail -> detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Cộng phí giao hàng
        if (order.getShippingCost() != null) {
            total = total.add(order.getShippingCost());
        }
        
        // Trừ discount
        if (order.getDiscountAmount() != null) {
            total = total.subtract(order.getDiscountAmount());
        }
        
        order.setTotalAmount(total);
    }
    
    // Validate chuyển đổi trạng thái
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Định nghĩa workflow hợp lệ
        switch (currentStatus) {
            case PENDING:
                if (newStatus != OrderStatus.CONFIRMED && newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.CANCELLED) {
                    throw new BusinessException("PENDING chỉ có thể chuyển thành CONFIRMED, PROCESSING hoặc CANCELLED");
                }
                break;
            case CONFIRMED:
                if (newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED) {
                    throw new BusinessException("CONFIRMED chỉ có thể chuyển thành PROCESSING, SHIPPED hoặc CANCELLED");
                }
                break;
            case PROCESSING:
                if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED) {
                    throw new BusinessException("PROCESSING chỉ có thể chuyển thành SHIPPED hoặc CANCELLED");
                }
                break;
            case SHIPPED:
                if (newStatus != OrderStatus.DELIVERED && newStatus != OrderStatus.CANCELLED) {
                    throw new BusinessException("SHIPPED chỉ có thể chuyển thành DELIVERED hoặc CANCELLED");
                }
                break;
            case DELIVERED:
                if (newStatus != OrderStatus.RETURNED) {
                    throw new BusinessException("DELIVERED chỉ có thể chuyển thành RETURNED");
                }
                break;
            case CANCELLED:
                throw new BusinessException("Không thể thay đổi trạng thái đơn hàng đã bị hủy");
            case RETURNED:
                throw new BusinessException("Không thể thay đổi trạng thái đơn hàng đã được trả");
        }
    }
    
    // Tạo mã đơn hàng
    private String generateOrderCode() {
        LocalDate today = LocalDate.now();
        String prefix = String.format("DH%04d%02d%02d", 
            today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        
        // Đếm số đơn hàng trong ngày
        Long orderCount = orderRepository.countByOrderDate(today);
        
        return prefix + String.format("%03d", orderCount + 1);
    }
}
