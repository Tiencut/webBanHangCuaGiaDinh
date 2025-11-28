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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

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
        log.info("Order cancelled successfully with id: {}", updatedOrder.getId());
        return orderMapper.toDto(updatedOrder);
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
