package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String orderNumber;
    private String orderCode;
    private Long customerId;
    private String customerName;
    private Long staffId;
    private String staffName;
    private LocalDateTime orderDate;
    private LocalDateTime requiredDate;
    private LocalDateTime shippedDate;
    private Order.OrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingCost;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private Order.PaymentStatus paymentStatus;
    private Order.PaymentMethod paymentMethod;
    private Order.DeliveryMethod deliveryMethod;
    private String shippingAddress;
    private String notes;
    private String internalNotes;
    private String cancelReason;
    private LocalDateTime cancelledAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime expectedDeliveryDate;
    private Boolean isPriceApplied;
    private LocalDateTime priceAppliedAt;
    private Long priceAppliedBy;
    private Boolean voiceCreated;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private List<OrderDetailDto> orderDetails;
} 