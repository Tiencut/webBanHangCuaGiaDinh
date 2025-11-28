package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.OrderDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.OrderStatsResponse;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        log.info("GET /api/orders - Fetching all orders");
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/page")
    @Operation(summary = "Get orders with pagination", description = "Retrieve orders with pagination support")
    public ResponseEntity<Page<OrderDto>> getOrdersWithPagination(Pageable pageable) {
        log.info("GET /api/orders/page - Fetching orders with pagination: {}", pageable);
        Page<OrderDto> orders = orderService.getOrdersWithPagination(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        log.info("GET /api/orders/{} - Fetching order by id", id);
        OrderDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number", description = "Retrieve a specific order by its order number")
    public ResponseEntity<OrderDto> getOrderByOrderNumber(@PathVariable String orderNumber) {
        log.info("GET /api/orders/number/{} - Fetching order by order number", orderNumber);
        OrderDto order = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    @Operation(summary = "Create new order", description = "Create a new order")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        log.info("POST /api/orders - Creating new order for customer: {}", orderDto.getCustomerId());
        OrderDto order = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order", description = "Update an existing order")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDto orderDto) {
        log.info("PUT /api/orders/{} - Updating order", id);
        OrderDto order = orderService.updateOrder(id, orderDto);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Delete an order (soft delete)")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("DELETE /api/orders/{} - Deleting order", id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get orders by customer", description = "Retrieve all orders for a specific customer")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomer(@PathVariable Long customerId) {
        log.info("GET /api/orders/customer/{} - Fetching orders by customer", customerId);
        List<OrderDto> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieve orders by status")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        log.info("GET /api/orders/status/{} - Fetching orders by status", status);
        List<OrderDto> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get orders by date range", description = "Retrieve orders within a date range")
    public ResponseEntity<List<OrderDto>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /api/orders/date-range - Fetching orders between {} and {}", startDate, endDate);
        List<OrderDto> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/payment-status/{paymentStatus}")
    @Operation(summary = "Get orders by payment status", description = "Retrieve orders by payment status")
    public ResponseEntity<List<OrderDto>> getOrdersByPaymentStatus(@PathVariable Order.PaymentStatus paymentStatus) {
        log.info("GET /api/orders/payment-status/{} - Fetching orders by payment status", paymentStatus);
        List<OrderDto> orders = orderService.getOrdersByPaymentStatus(paymentStatus);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/confirm")
    @Operation(summary = "Confirm order", description = "Confirm a pending order")
    public ResponseEntity<OrderDto> confirmOrder(@PathVariable Long id) {
        log.info("PUT /api/orders/{}/confirm - Confirming order", id);
        OrderDto order = orderService.confirmOrder(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/ship")
    @Operation(summary = "Ship order", description = "Ship a confirmed order")
    public ResponseEntity<OrderDto> shipOrder(@PathVariable Long id) {
        log.info("PUT /api/orders/{}/ship - Shipping order", id);
        OrderDto order = orderService.shipOrder(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/deliver")
    @Operation(summary = "Deliver order", description = "Deliver a shipped order")
    public ResponseEntity<OrderDto> deliverOrder(@PathVariable Long id) {
        log.info("PUT /api/orders/{}/deliver - Delivering order", id);
        OrderDto order = orderService.deliverOrder(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancel an order")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long id, @RequestParam String reason) {
        log.info("PUT /api/orders/{}/cancel - Cancelling order with reason: {}", id, reason);
        OrderDto order = orderService.cancelOrder(id, reason);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get order statistics", description = "Retrieve order statistics")
    public ResponseEntity<OrderStatsResponse> getOrderStats() {
        log.info("GET /api/orders/stats - Fetching order statistics");
        OrderStatsResponse stats = orderService.getOrderStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/search")
    @Operation(summary = "Search orders", description = "Search orders by keyword")
    public ResponseEntity<List<OrderDto>> searchOrders(@RequestParam String keyword) {
        log.info("GET /api/orders/search - Searching orders with keyword: {}", keyword);
        List<OrderDto> orders = orderService.searchOrders(keyword);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/staff/{staffId}")
    @Operation(summary = "Get orders by staff", description = "Retrieve orders created by a specific staff member")
    public ResponseEntity<List<OrderDto>> getOrdersByStaff(@PathVariable Long staffId) {
        log.info("GET /api/orders/staff/{} - Fetching orders by staff", staffId);
        List<OrderDto> orders = orderService.getOrdersByStaff(staffId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/voice-created")
    @Operation(summary = "Get voice created orders", description = "Retrieve orders created via voice commands")
    public ResponseEntity<List<OrderDto>> getVoiceCreatedOrders() {
        log.info("GET /api/orders/voice-created - Fetching voice created orders");
        List<OrderDto> orders = orderService.getVoiceCreatedOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/apply-price")
    @Operation(summary = "Apply price for order", description = "Mark an order as having prices applied")
    public ResponseEntity<OrderDto> applyPrice(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        log.info("PUT /api/orders/{}/apply-price - Applying price by user: {}", id, userId);
        OrderDto order = orderService.applyPrice(id, userId);
        return ResponseEntity.ok(order);
    }
}
