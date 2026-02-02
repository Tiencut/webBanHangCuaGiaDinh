package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.OrderDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.OrderStatsResponse;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.OrderDetail;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper.OrderMapper;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CustomerRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.OrderRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
// import java.util.Optional; // not used
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private static int safeInt(Integer v) {
        return v == null ? 0 : v;
    }

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.InventoryRepository inventoryRepository;
    private final com.giadinh.banphutung.web_ban_hang_gia_dinh.service.NotificationClient notificationClient;

    @org.springframework.beans.factory.annotation.Value("${notifications.admin.email:admin@example.com}")
    private String notificationsAdminEmail;

    public List<OrderDto> getAllOrders() {
        log.info("Fetching all orders");
        List<Order> orders = orderRepository.findByIsDeletedFalse();
        return orderMapper.toDtoList(orders);
    }

    public Page<OrderDto> getOrdersWithPagination(Pageable pageable) {
        log.info("Fetching orders with pagination: {}", pageable);
        Page<Order> orders = orderRepository.findByIsDeletedFalse(pageable);
        return orders.map(orderMapper::toDto);
    }

    public OrderDto getOrderById(Long id) {
        log.info("Fetching order by id: {}", id);
        Order order = orderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toDto(order);
    }

    public OrderDto getOrderByOrderNumber(String orderNumber) {
        log.info("Fetching order by order number: {}", orderNumber);
        Order order = orderRepository.findByOrderNumberAndIsDeletedFalse(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));
        return orderMapper.toDto(order);
    }

    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Creating new order for customer: {}", orderDto.getCustomerId());
        
        // Validate customer exists
        Customer customer = customerRepository.findByIdAndIsActiveTrue(orderDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDto.getCustomerId()));

        // Validate staff exists if provided
        User staff = null;
        if (orderDto.getStaffId() != null) {
            staff = userRepository.findByIdAndIsActiveTrue(orderDto.getStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + orderDto.getStaffId()));
        }

        Order order = new Order();
        orderMapper.updateEntityFromDto(orderDto, order);
        
        // Set relationships
        order.setCustomer(customer);
        order.setStaff(staff);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);
        order.setIsDeleted(false);
        order.setIsActive(true);

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with id: {}", savedOrder.getId());

        // send order confirmation notification (enqueue)
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("orderId", savedOrder.getId());
            payload.put("customerName", customer.getName());
            payload.put("to", customer.getEmail());
            payload.put("total", savedOrder.getTotalAmount());
            List<Map<String, Object>> items = new ArrayList<>();
            if (savedOrder.getOrderDetails() != null) {
                for (OrderDetail od : savedOrder.getOrderDetails()) {
                    Map<String, Object> it = new HashMap<>();
                    it.put("name", od.getProduct() != null ? od.getProduct().getName() : "#" + (od.getProduct() != null ? od.getProduct().getId() : ""));
                    it.put("quantity", od.getQuantity());
                    it.put("price", od.getUnitPrice());
                    items.add(it);
                }
            }
            payload.put("items", items);
            // enqueue job (best-effort)
            notificationClient.enqueue("order-confirmation", payload);
        } catch (Exception ex) {
            log.warn("Failed to enqueue order confirmation notification: {}", ex.getMessage());
        }

        // Allocate inventory (pessimistic lock) for each order detail
        if (savedOrder.getOrderDetails() != null) {
            for (OrderDetail od : savedOrder.getOrderDetails()) {
                Long productId = od.getProduct() != null ? od.getProduct().getId() : null;
                Long supplierId = od.getSupplier() != null ? od.getSupplier().getId() : null;
                if (productId == null || supplierId == null) continue;
                var invOpt = inventoryRepository.findByProductIdAndSupplierIdForUpdate(productId, supplierId);
                if (invOpt.isEmpty()) {
                    throw new BusinessException("Inventory not found for product " + productId + " supplier " + supplierId);
                }
                var inv = invOpt.get();
                Integer currentQty = inv.getCurrentQuantity();
                if (currentQty == null) currentQty = 0;
                int reqQty = safeInt(od.getQuantity());
                if (currentQty < reqQty) {
                    throw new BusinessException("Insufficient stock for product " + productId);
                }
                inv.setCurrentQuantity(currentQty - reqQty);
                Integer committed = inv.getCommittedQuantity();
                if (committed == null) committed = 0;
                inv.setCommittedQuantity(committed + reqQty);
                inventoryRepository.save(inv);
                // recording inventory transaction left as TODO (inventoryTransactionService)
                // if remaining inventory is low, notify admin/supplier
                try {
                    Integer remaining = inv.getCurrentQuantity();
                    if (remaining == null) remaining = 0;
                    int threshold = 5; // simple threshold
                    if (remaining <= threshold) {
                        Map<String, Object> p = new HashMap<>();
                        p.put("productName", od.getProduct() != null ? od.getProduct().getName() : "product-" + productId);
                        p.put("remaining", remaining);
                        p.put("to", notificationsAdminEmail);
                        notificationClient.enqueue("stock-low", p);
                    }
                } catch (Exception ex) {
                    log.warn("Failed to enqueue stock-low notification: {}", ex.getMessage());
                }
            }
        }

        return orderMapper.toDto(savedOrder);
    }

    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        log.info("Updating order with id: {}", id);
        Order order = orderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        // Validate customer exists if changed
        if (orderDto.getCustomerId() != null && !orderDto.getCustomerId().equals(order.getCustomer().getId())) {
            Customer customer = customerRepository.findByIdAndIsActiveTrue(orderDto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDto.getCustomerId()));
            order.setCustomer(customer);
        }

        // Validate staff exists if changed
        if (orderDto.getStaffId() != null && (order.getStaff() == null || !orderDto.getStaffId().equals(order.getStaff().getId()))) {
            User staff = userRepository.findByIdAndIsActiveTrue(orderDto.getStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + orderDto.getStaffId()));
            order.setStaff(staff);
        }

        orderMapper.updateEntityFromDto(orderDto, order);
        Order updatedOrder = orderRepository.save(order);
        log.info("Order updated successfully with id: {}", updatedOrder.getId());
        return orderMapper.toDto(updatedOrder);
    }

    public void deleteOrder(Long id) {
        log.info("Deleting order with id: {}", id);
        Order order = orderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        order.setIsDeleted(true);
        orderRepository.save(order);
        log.info("Order deleted successfully with id: {}", id);
    }

    public List<OrderDto> getOrdersByCustomer(Long customerId) {
        log.info("Fetching orders for customer: {}", customerId);
        List<Order> orders = orderRepository.findByCustomerIdAndIsDeletedFalse(customerId);
        return orderMapper.toDtoList(orders);
    }

    public List<OrderDto> getOrdersByStatus(Order.OrderStatus status) {
        log.info("Fetching orders by status: {}", status);
        List<Order> orders = orderRepository.findByStatusAndIsDeletedFalse(status);
        return orderMapper.toDtoList(orders);
    }

    public List<OrderDto> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching orders between {} and {}", startDate, endDate);
        List<Order> orders = orderRepository.findByOrderDateBetweenAndIsDeletedFalse(
                startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        return orderMapper.toDtoList(orders);
    }

    public List<OrderDto> getOrdersByPaymentStatus(Order.PaymentStatus paymentStatus) {
        log.info("Fetching orders by payment status: {}", paymentStatus);
        List<Order> orders = orderRepository.findByPaymentStatusAndIsDeletedFalse(paymentStatus);
        return orderMapper.toDtoList(orders);
    }

    public OrderDto confirmOrder(Long id) {
        log.info("Confirming order with id: {}", id);
        Order order = orderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new BusinessException("Order cannot be confirmed. Current status: " + order.getStatus());
        }
        
        order.setStatus(Order.OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);
        log.info("Order confirmed successfully with id: {}", updatedOrder.getId());

        // enqueue lifecycle notification
        try {
            Map<String, Object> p = new HashMap<>();
            p.put("orderId", updatedOrder.getId());
            p.put("to", updatedOrder.getCustomer() != null ? updatedOrder.getCustomer().getEmail() : null);
            p.put("customerName", updatedOrder.getCustomer() != null ? updatedOrder.getCustomer().getName() : null);
            notificationClient.enqueue("order-confirmed", p);
        } catch (Exception ex) {
            log.warn("Failed to enqueue order-confirmed notification: {}", ex.getMessage());
        }
        return orderMapper.toDto(updatedOrder);
    }

    public OrderDto shipOrder(Long id) {
        log.info("Shipping order with id: {}", id);
        Order order = orderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        if (order.getStatus() != Order.OrderStatus.CONFIRMED) {
            throw new BusinessException("Order cannot be shipped. Current status: " + order.getStatus());
        }
        
        order.setStatus(Order.OrderStatus.SHIPPED);
        order.setShippedDate(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);
        log.info("Order shipped successfully with id: {}", updatedOrder.getId());

        try {
            Map<String, Object> p = new HashMap<>();
            p.put("orderId", updatedOrder.getId());
            p.put("to", updatedOrder.getCustomer() != null ? updatedOrder.getCustomer().getEmail() : null);
            p.put("customerName", updatedOrder.getCustomer() != null ? updatedOrder.getCustomer().getName() : null);
            notificationClient.enqueue("order-shipped", p);
        } catch (Exception ex) {
            log.warn("Failed to enqueue order-shipped notification: {}", ex.getMessage());
        }
        return orderMapper.toDto(updatedOrder);
    }

    public OrderDto deliverOrder(Long id) {
        log.info("Delivering order with id: {}", id);
        Order order = orderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        if (order.getStatus() != Order.OrderStatus.SHIPPED) {
            throw new BusinessException("Order cannot be delivered. Current status: " + order.getStatus());
        }
        
        order.setStatus(Order.OrderStatus.DELIVERED);
        order.setDeliveredAt(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);
        log.info("Order delivered successfully with id: {}", updatedOrder.getId());

        try {
            Map<String, Object> p = new HashMap<>();
            p.put("orderId", updatedOrder.getId());
            p.put("to", updatedOrder.getCustomer() != null ? updatedOrder.getCustomer().getEmail() : null);
            p.put("customerName", updatedOrder.getCustomer() != null ? updatedOrder.getCustomer().getName() : null);
            notificationClient.enqueue("order-delivered", p);
        } catch (Exception ex) {
            log.warn("Failed to enqueue order-delivered notification: {}", ex.getMessage());
        }
        return orderMapper.toDto(updatedOrder);
    }

    public OrderDto cancelOrder(Long id, String reason) {
        log.info("Cancelling order with id: {}", id);
        Order order = orderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        if (order.getStatus() == Order.OrderStatus.DELIVERED || order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new BusinessException("Order cannot be cancelled. Current status: " + order.getStatus());
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancelReason(reason);
        order.setCancelledAt(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);
        // restore inventory for each detail
        if (updatedOrder.getOrderDetails() != null) {
            for (OrderDetail od : updatedOrder.getOrderDetails()) {
                Long productId = od.getProduct() != null ? od.getProduct().getId() : null;
                Long supplierId = od.getSupplier() != null ? od.getSupplier().getId() : null;
                if (productId == null || supplierId == null) continue;
                var invOpt = inventoryRepository.findByProductIdAndSupplierId(productId, supplierId);
                if (invOpt.isPresent()) {
                    var inv = invOpt.get();
                    Integer cur = inv.getCurrentQuantity();
                    if (cur == null) cur = 0;
                    int q = safeInt(od.getQuantity());
                    inv.setCurrentQuantity(cur + q);
                    Integer committed = inv.getCommittedQuantity();
                    if (committed == null) committed = 0;
                    inv.setCommittedQuantity(committed - q);
                    inventoryRepository.save(inv);
                }
            }
        }
        log.info("Order cancelled successfully with id: {}", updatedOrder.getId());
        return orderMapper.toDto(updatedOrder);
    }

    // Payment webhook helpers
    public void applyPaymentSuccess(Long orderId, String provider, String providerTxnId) {
        log.info("Applying payment success for order {} via {}", orderId, provider);
        var opt = orderRepository.findByIdAndIsDeletedFalse(orderId);
        if (opt.isEmpty()) {
            log.warn("Order not found for payment success: {}", orderId);
            return;
        }
        var order = opt.get();
        order.setPaymentStatus(Order.PaymentStatus.PAID);
        order.setStatus(Order.OrderStatus.CONFIRMED);
        Order saved = orderRepository.save(order);

        // enqueue order confirmation notification (best-effort)
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("orderId", saved.getId());
            payload.put("customerName", saved.getCustomer() != null ? saved.getCustomer().getName() : "");
            payload.put("to", saved.getCustomer() != null ? saved.getCustomer().getEmail() : null);
            payload.put("total", saved.getTotalAmount());
            payload.put("provider", provider);
            payload.put("providerTxnId", providerTxnId);
            List<Map<String, Object>> items = new ArrayList<>();
            if (saved.getOrderDetails() != null) {
                for (OrderDetail od : saved.getOrderDetails()) {
                    Map<String, Object> it = new HashMap<>();
                    it.put("name", od.getProduct() != null ? od.getProduct().getName() : "");
                    it.put("quantity", od.getQuantity());
                    it.put("price", od.getUnitPrice());
                    items.add(it);
                }
            }
            payload.put("items", items);
            notificationClient.enqueue("order-confirmation", payload);

            // also enqueue SMS if customer phone exists
            try {
                if (saved.getCustomer() != null && saved.getCustomer().getPhone() != null) {
                    Map<String, Object> sms = new HashMap<>();
                    sms.put("phone", saved.getCustomer().getPhone());
                    sms.put("text", "Don hang #" + saved.getId() + " da duoc thanh toan. Cam on!");
                    notificationClient.enqueue("sms", sms);
                }
            } catch (Exception ex) {
                log.warn("Failed to enqueue sms notification: {}", ex.getMessage());
            }
        } catch (Exception ex) {
            log.warn("Failed to enqueue order confirmation notification: {}", ex.getMessage());
        }
    }

    public void applyPaymentFailure(Long orderId, String provider, String providerTxnId) {
        log.info("Applying payment failure for order {} via {}", orderId, provider);
        var opt = orderRepository.findByIdAndIsDeletedFalse(orderId);
        if (opt.isEmpty()) return;
        var order = opt.get();
        order.setPaymentStatus(Order.PaymentStatus.PENDING);
        Order saved = orderRepository.save(order);

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("orderId", saved.getId());
            payload.put("to", saved.getCustomer() != null ? saved.getCustomer().getEmail() : null);
            payload.put("customerName", saved.getCustomer() != null ? saved.getCustomer().getName() : null);
            payload.put("provider", provider);
            payload.put("providerTxnId", providerTxnId);
            notificationClient.enqueue("payment-failure", payload);
        } catch (Exception ex) {
            log.warn("Failed to enqueue payment-failure notification: {}", ex.getMessage());
        }
    }

    public OrderStatsResponse getOrderStats() {
        log.info("Fetching order statistics");
        
        long totalOrders = orderRepository.countByIsDeletedFalse();
        long pendingOrders = orderRepository.countByStatusAndIsDeletedFalse(Order.OrderStatus.PENDING);
        long confirmedOrders = orderRepository.countByStatusAndIsDeletedFalse(Order.OrderStatus.CONFIRMED);
        long processingOrders = 0; // TODO: Add processing status if needed
        long shippedOrders = orderRepository.countByStatusAndIsDeletedFalse(Order.OrderStatus.SHIPPED);
        long deliveredOrders = orderRepository.countByStatusAndIsDeletedFalse(Order.OrderStatus.DELIVERED);
        long cancelledOrders = orderRepository.countByStatusAndIsDeletedFalse(Order.OrderStatus.CANCELLED);
        long returnedOrders = 0; // TODO: Add returned status if needed
        
        BigDecimal totalRevenue = orderRepository.sumTotalAmountByStatusAndIsDeletedFalse(Order.OrderStatus.DELIVERED);
        BigDecimal pendingRevenue = orderRepository.sumTotalAmountByStatusAndIsDeletedFalse(Order.OrderStatus.PENDING);
        BigDecimal confirmedRevenue = orderRepository.sumTotalAmountByStatusAndIsDeletedFalse(Order.OrderStatus.CONFIRMED);
        BigDecimal processingRevenue = BigDecimal.ZERO; // TODO: Add processing status if needed
        BigDecimal shippedRevenue = orderRepository.sumTotalAmountByStatusAndIsDeletedFalse(Order.OrderStatus.SHIPPED);
        BigDecimal deliveredRevenue = orderRepository.sumTotalAmountByStatusAndIsDeletedFalse(Order.OrderStatus.DELIVERED);
        BigDecimal cancelledRevenue = orderRepository.sumTotalAmountByStatusAndIsDeletedFalse(Order.OrderStatus.CANCELLED);
        BigDecimal returnedRevenue = BigDecimal.ZERO; // TODO: Add returned status if needed
        
        return OrderStatsResponse.builder()
                .totalOrders((int) totalOrders)
                .pendingOrders((int) pendingOrders)
                .confirmedOrders((int) confirmedOrders)
                .processingOrders((int) processingOrders)
                .shippedOrders((int) shippedOrders)
                .deliveredOrders((int) deliveredOrders)
                .cancelledOrders((int) cancelledOrders)
                .returnedOrders((int) returnedOrders)
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .pendingRevenue(pendingRevenue != null ? pendingRevenue : BigDecimal.ZERO)
                .confirmedRevenue(confirmedRevenue != null ? confirmedRevenue : BigDecimal.ZERO)
                .processingRevenue(processingRevenue)
                .shippedRevenue(shippedRevenue != null ? shippedRevenue : BigDecimal.ZERO)
                .deliveredRevenue(deliveredRevenue != null ? deliveredRevenue : BigDecimal.ZERO)
                .cancelledRevenue(cancelledRevenue != null ? cancelledRevenue : BigDecimal.ZERO)
                .returnedRevenue(returnedRevenue)
                .build();
    }

    public List<OrderDto> searchOrders(String keyword) {
        log.info("Searching orders with keyword: {}", keyword);
        List<Order> orders = orderRepository.searchOrders(keyword);
        return orderMapper.toDtoList(orders);
    }

    public List<OrderDto> getOrdersByStaff(Long staffId) {
        log.info("Fetching orders by staff: {}", staffId);
        List<Order> orders = orderRepository.findByStaffIdAndIsDeletedFalse(staffId);
        return orderMapper.toDtoList(orders);
    }

    public List<OrderDto> getVoiceCreatedOrders() {
        log.info("Fetching voice created orders");
        List<Order> orders = orderRepository.findByVoiceCreatedTrueAndIsDeletedFalse();
        return orderMapper.toDtoList(orders);
    }

    /**
     * Mark an order as having prices applied.
     * @param id order id
     * @param userId optional user id who applied the prices
     * @return updated OrderDto
     */
    public OrderDto applyPrice(Long id, Long userId) {
        log.info("Applying price for order id: {} by user: {}", id, userId);
        Order order = orderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (Boolean.TRUE.equals(order.getIsPriceApplied())) {
            throw new BusinessException("Prices have already been applied to this order");
        }

        order.setIsPriceApplied(true);
        order.setPriceAppliedAt(LocalDateTime.now());
        order.setPriceAppliedBy(userId);

        Order updated = orderRepository.save(order);
        log.info("Price applied for order id: {}", updated.getId());
        return orderMapper.toDto(updated);
    }
}
