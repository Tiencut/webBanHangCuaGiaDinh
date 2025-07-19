package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.OrderStatsResponse;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.OrderDetail;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Order Management", description = "APIs để quản lý đơn hàng")
public class OrderController {
    
    private final OrderService orderService;
    
    @Operation(summary = "Lấy tất cả đơn hàng", 
               description = "Trả về danh sách tất cả đơn hàng với phân trang")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Kích thước trang") @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all orders with pagination: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.findAll(pageable);
        return ResponseEntity.ok(orders);
    }
    
    @Operation(summary = "Lấy đơn hàng theo ID", 
               description = "Trả về thông tin chi tiết của đơn hàng theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy đơn hàng")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
            @Parameter(description = "ID của đơn hàng") @PathVariable Long id) {
        log.info("Getting order with id: {}", id);
        return orderService.findById(id)
            .map(order -> ResponseEntity.ok(order))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Lấy đơn hàng theo mã đơn hàng", 
               description = "Trả về thông tin đơn hàng theo mã đơn hàng")
    @GetMapping("/code/{orderCode}")
    public ResponseEntity<Order> getOrderByCode(
            @Parameter(description = "Mã đơn hàng") @PathVariable String orderCode) {
        log.info("Getting order with code: {}", orderCode);
        return orderService.findByOrderCode(orderCode)
            .map(order -> ResponseEntity.ok(order))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Lấy đơn hàng theo khách hàng", 
               description = "Trả về danh sách đơn hàng của một khách hàng")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(
            @Parameter(description = "ID của khách hàng") @PathVariable Long customerId) {
        log.info("Getting orders by customer id: {}", customerId);
        List<Order> orders = orderService.findByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }
    
    @Operation(summary = "Lấy đơn hàng theo trạng thái", 
               description = "Trả về danh sách đơn hàng theo trạng thái")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(
            @Parameter(description = "Trạng thái đơn hàng") @PathVariable String status) {
        log.info("Getting orders by status: {}", status);
        List<Order> orders = orderService.findByStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(orders);
    }
    
    @Operation(summary = "Lấy đơn hàng theo khoảng thời gian", 
               description = "Trả về danh sách đơn hàng trong khoảng thời gian")
    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(
            @Parameter(description = "Ngày bắt đầu") @RequestParam String startDate,
            @Parameter(description = "Ngày kết thúc") @RequestParam String endDate) {
        log.info("Getting orders by date range: {} - {}", startDate, endDate);
        java.time.LocalDateTime start = java.time.LocalDateTime.parse(startDate);
        java.time.LocalDateTime end = java.time.LocalDateTime.parse(endDate);
        List<Order> orders = orderService.findByDateRange(start, end);
        return ResponseEntity.ok(orders);
    }
    
    @Operation(summary = "Lấy đơn hàng theo khoảng giá", 
               description = "Trả về danh sách đơn hàng trong khoảng giá")
    @GetMapping("/total-range")
    public ResponseEntity<List<Order>> getOrdersByTotalRange(
            @Parameter(description = "Tổng tiền tối thiểu") @RequestParam BigDecimal minTotal,
            @Parameter(description = "Tổng tiền tối đa") @RequestParam BigDecimal maxTotal) {
        log.info("Getting orders by total range: {} - {}", minTotal, maxTotal);
        List<Order> orders = orderService.findByTotalRange(minTotal, maxTotal);
        return ResponseEntity.ok(orders);
    }
    
    @Operation(summary = "Tìm kiếm đơn hàng", 
               description = "Tìm kiếm đơn hàng theo từ khóa với phân trang")
    @GetMapping("/search")
    public ResponseEntity<Page<Order>> searchOrders(
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam String term,
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Kích thước trang") @RequestParam(defaultValue = "10") int size) {
        log.info("Searching orders with term: {}", term);
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.searchOrders(term, pageable);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Lấy thống kê đơn hàng", 
               description = "Trả về thống kê tổng số đơn hàng và doanh thu")
    @GetMapping("/stats")
    public ResponseEntity<OrderStatsResponse> getOrderStats() {
        // Lấy tổng số đơn hàng và tổng doanh thu toàn bộ
        int totalOrders = orderService.getTotalOrderCount();
        java.math.BigDecimal totalRevenue = orderService.getTotalRevenue();
        OrderStatsResponse stats = new OrderStatsResponse();
        stats.setTotalOrders(totalOrders);
        stats.setTotalRevenue(totalRevenue);
        // Có thể bổ sung các trường khác nếu muốn
        return ResponseEntity.ok(stats);
    }
    
    @Operation(summary = "Tạo đơn hàng mới", 
               description = "Tạo một đơn hàng mới trong hệ thống")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tạo thành công"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
        @ApiResponse(responseCode = "404", description = "Khách hàng hoặc sản phẩm không tồn tại"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @Parameter(description = "Thông tin đơn hàng cần tạo") @Valid @RequestBody Order order) {
        try {
            Order savedOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (BusinessException e) {
            log.error("Business error creating order: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found creating order: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cập nhật đơn hàng", 
               description = "Cập nhật thông tin của đơn hàng theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
        @ApiResponse(responseCode = "404", description = "Đơn hàng không tồn tại"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @Parameter(description = "ID của đơn hàng") @PathVariable Long id, 
            @Parameter(description = "Thông tin đơn hàng cần cập nhật") @Valid @RequestBody Order order
    ) {
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (BusinessException e) {
            log.error("Business error updating order: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Order not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cập nhật trạng thái đơn hàng", 
               description = "Cập nhật trạng thái của đơn hàng")
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @Parameter(description = "ID của đơn hàng") @PathVariable Long id,
            @Parameter(description = "Trạng thái mới") @RequestParam String status
    ) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (BusinessException e) {
            log.error("Business error updating order status: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Order not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating order status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Thêm sản phẩm vào đơn hàng", 
               description = "Thêm một sản phẩm vào đơn hàng hiện có")
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderDetail> addOrderItem(
            @Parameter(description = "ID của đơn hàng") @PathVariable Long orderId,
            @Parameter(description = "Thông tin sản phẩm cần thêm") @Valid @RequestBody OrderDetail orderDetail
    ) {
        try {
            OrderDetail savedOrderDetail = orderService.addOrderItem(orderId, orderDetail);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrderDetail);
        } catch (BusinessException e) {
            log.error("Business error adding order item: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Order or product not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error adding order item: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cập nhật sản phẩm trong đơn hàng", 
               description = "Cập nhật thông tin sản phẩm trong đơn hàng")
    @PutMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderDetail> updateOrderItem(
            @Parameter(description = "ID của đơn hàng") @PathVariable Long orderId,
            @Parameter(description = "ID của sản phẩm trong đơn hàng") @PathVariable Long itemId,
            @Parameter(description = "Thông tin sản phẩm cần cập nhật") @Valid @RequestBody OrderDetail orderDetail
    ) {
        try {
            OrderDetail updatedOrderDetail = orderService.updateOrderItem(orderId, itemId, orderDetail);
            return ResponseEntity.ok(updatedOrderDetail);
        } catch (BusinessException e) {
            log.error("Business error updating order item: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Order item not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating order item: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Xóa sản phẩm khỏi đơn hàng", 
               description = "Xóa một sản phẩm khỏi đơn hàng")
    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<Void> removeOrderItem(
            @Parameter(description = "ID của đơn hàng") @PathVariable Long orderId,
            @Parameter(description = "ID của sản phẩm trong đơn hàng") @PathVariable Long itemId) {
        try {
            orderService.removeOrderItem(orderId, itemId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Order item not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error removing order item: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Xóa đơn hàng", 
               description = "Xóa đơn hàng theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Xóa thành công"),
        @ApiResponse(responseCode = "404", description = "Đơn hàng không tồn tại"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "ID của đơn hàng") @PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Order not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
