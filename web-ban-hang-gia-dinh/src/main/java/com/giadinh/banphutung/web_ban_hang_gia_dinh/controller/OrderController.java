package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.OrderService;

import jakarta.validation.Valid;

/**
 * OrderController - API quản lý đơn hàng
 * 
 * Đây là controller xử lý các API liên quan đến đơn hàng:
 * - CRUD đơn hàng (Create, Read, Update, Delete)
 * - Tìm kiếm đơn hàng theo khách hàng, ngày, trạng thái
 * - Phân trang danh sách đơn hàng
 * - Thống kê đơn hàng, doanh thu
 * - Xử lý trạng thái đơn hàng (pending, confirmed, shipped, delivered, cancelled)
 * 
 * Các endpoint chính:
 * - GET /api/orders - Lấy danh sách đơn hàng
 * - GET /api/orders/{id} - Lấy thông tin 1 đơn hàng
 * - POST /api/orders - Tạo đơn hàng mới
 * - PUT /api/orders/{id} - Cập nhật đơn hàng
 * - DELETE /api/orders/{id} - Xóa đơn hàng
 * - GET /api/orders/search - Tìm kiếm đơn hàng
 * - PUT /api/orders/{id}/status - Cập nhật trạng thái đơn hàng
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Lấy danh sách đơn hàng với phân trang
     * GET /api/orders?page=0&size=10&sort=orderDate
     */
    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        try {
            Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            Page<Order> orders = orderService.findAll(pageable);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy thông tin 1 đơn hàng theo ID
     * GET /api/orders/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        try {
            Optional<Order> order = orderService.findById(id);
            return order.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tạo đơn hàng mới
     * POST /api/orders
     * Body: JSON của Order
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        try {
            Order savedOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Cập nhật đơn hàng
     * PUT /api/orders/1
     * Body: JSON của Order
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long id, 
            @Valid @RequestBody Order order
    ) {
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Xóa đơn hàng (soft delete)
     * DELETE /api/orders/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tìm kiếm đơn hàng theo khách hàng
     * GET /api/orders/customer/1
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable Long customerId) {
        try {
            List<Order> orders = orderService.findByCustomerId(customerId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tìm kiếm đơn hàng theo trạng thái
     * GET /api/orders/status/PENDING
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        try {
            List<Order> orders = orderService.findByStatus(status);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tìm kiếm đơn hàng theo khoảng ngày
     * GET /api/orders/date-range?from=2024-01-01&to=2024-01-31
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(
            @RequestParam String from,
            @RequestParam String to
    ) {
        try {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);
            List<Order> orders = orderService.findByDateRange(fromDate, toDate);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Cập nhật trạng thái đơn hàng
     * PUT /api/orders/1/status
     * Body: {"status": "CONFIRMED"}
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request
    ) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, request.getStatus());
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Hủy đơn hàng
     * PUT /api/orders/1/cancel
     * Body: {"reason": "Khách hàng thay đổi ý định"}
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long id,
            @RequestBody CancelRequest request
    ) {
        try {
            Order cancelledOrder = orderService.cancelOrder(id, request.getReason());
            return ResponseEntity.ok(cancelledOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Lấy thống kê đơn hàng theo ngày
     * GET /api/orders/stats/daily?date=2024-01-15
     */
    @GetMapping("/stats/daily")
    public ResponseEntity<OrderStatsResponse> getDailyStats(
            @RequestParam(required = false) String date
    ) {
        try {
            LocalDate targetDate = date != null ? LocalDate.parse(date) : LocalDate.now();
            OrderStatsResponse stats = orderService.getDailyStats(targetDate);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy thống kê đơn hàng theo tháng
     * GET /api/orders/stats/monthly?year=2024&month=1
     */
    @GetMapping("/stats/monthly")
    public ResponseEntity<OrderStatsResponse> getMonthlyStats(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        try {
            LocalDate now = LocalDate.now();
            int targetYear = year != null ? year : now.getYear();
            int targetMonth = month != null ? month : now.getMonthValue();
            OrderStatsResponse stats = orderService.getMonthlyStats(targetYear, targetMonth);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Inner classes cho request/response
     */
    public static class StatusUpdateRequest {
        private String status;
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class CancelRequest {
        private String reason;
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    public static class OrderStatsResponse {
        private int totalOrders;
        private BigDecimal totalRevenue;
        private int pendingOrders;
        private int confirmedOrders;
        private int shippedOrders;
        private int deliveredOrders;
        private int cancelledOrders;

        // Constructors
        public OrderStatsResponse() {}

        public OrderStatsResponse(int totalOrders, BigDecimal totalRevenue, 
                int pendingOrders, int confirmedOrders, int shippedOrders, 
                int deliveredOrders, int cancelledOrders) {
            this.totalOrders = totalOrders;
            this.totalRevenue = totalRevenue;
            this.pendingOrders = pendingOrders;
            this.confirmedOrders = confirmedOrders;
            this.shippedOrders = shippedOrders;
            this.deliveredOrders = deliveredOrders;
            this.cancelledOrders = cancelledOrders;
        }

        // Getters and setters
        public int getTotalOrders() { return totalOrders; }
        public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

        public int getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(int pendingOrders) { this.pendingOrders = pendingOrders; }

        public int getConfirmedOrders() { return confirmedOrders; }
        public void setConfirmedOrders(int confirmedOrders) { this.confirmedOrders = confirmedOrders; }

        public int getShippedOrders() { return shippedOrders; }
        public void setShippedOrders(int shippedOrders) { this.shippedOrders = shippedOrders; }

        public int getDeliveredOrders() { return deliveredOrders; }
        public void setDeliveredOrders(int deliveredOrders) { this.deliveredOrders = deliveredOrders; }

        public int getCancelledOrders() { return cancelledOrders; }
        public void setCancelledOrders(int cancelledOrders) { this.cancelledOrders = cancelledOrders; }
    }
}
