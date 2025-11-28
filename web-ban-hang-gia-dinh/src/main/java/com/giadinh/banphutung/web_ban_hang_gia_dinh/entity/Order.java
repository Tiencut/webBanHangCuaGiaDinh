package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {
    
    @Column(name = "order_number", unique = true)
    private String orderNumber;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private User staff;
    
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    
    @Column(name = "required_date")
    private LocalDateTime requiredDate;
    
    @Column(name = "shipped_date")
    private LocalDateTime shippedDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(name = "subtotal", precision = 19, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    @Column(name = "discount_amount", precision = 19, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "tax_amount", precision = 19, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Column(name = "shipping_cost", precision = 19, scale = 2)
    private BigDecimal shippingCost = BigDecimal.ZERO;
    
    @Column(name = "total_amount", precision = 19, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "paid_amount", precision = 19, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod = PaymentMethod.CASH;
    
    @Column(name = "shipping_address")
    private String shippingAddress;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;
    
    /**
     * Đơn hàng có được tạo bằng giọng nói không
     */
    @Column(name = "voice_created")
    private Boolean voiceCreated = false;
    
    /**
     * Phương thức giao hàng
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method")
    private DeliveryMethod deliveryMethod = DeliveryMethod.SELF_PICKUP;
    
    // Thêm các field cần thiết cho OrderService
    @Column(name = "order_code", unique = true)
    private String orderCode;
    
    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;
    
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;
    
    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;
    
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
    
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;
    
    @Column(name = "cancel_reason")
    private String cancelReason;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    
    // Flag cho biết đã áp giá (price applied) cho hoá đơn chưa
    @Column(name = "is_price_applied")
    private Boolean isPriceApplied = false;

    // Thời điểm đã áp giá
    @Column(name = "price_applied_at")
    private LocalDateTime priceAppliedAt;

    // Người dùng đã áp giá (user id)
    @Column(name = "price_applied_by")
    private Long priceAppliedBy;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();
    
    public enum OrderStatus {
        PENDING,        // Chờ xử lý
        CONFIRMED,      // Đã xác nhận
        PROCESSING,     // Đang xử lý
        SHIPPED,        // Đã giao
        DELIVERED,      // Đã nhận
        CANCELLED,      // Đã hủy
        RETURNED        // Đã trả hàng
    }
    
    public enum PaymentStatus {
        PENDING,        // Chưa thanh toán
        PARTIAL,        // Thanh toán một phần
        PAID,           // Đã thanh toán
        REFUNDED,       // Đã hoàn tiền
        OVERDUE         // Quá hạn
    }
    
    public enum PaymentMethod {
        CASH,           // Tiền mặt
        COD,            // Thu hộ (Cash on Delivery)
        BANK_TRANSFER,  // Chuyển khoản
        CREDIT_CARD,    // Thẻ tín dụng
        VNPAY,          // VNPay
        MOMO,           // MoMo
        CREDIT          // Công nợ
    }
    
    public enum DeliveryMethod {
        SELF_PICKUP,    // Khách tự đến lấy
        MOTORBIKE,      // Xe ôm
        BUS,            // Xe khách
        TRUCK,          // Xe tải riêng
        EXPRESS         // Giao hàng nhanh
    }
    
    // Business methods
    public BigDecimal getRemainingAmount() {
        return totalAmount.subtract(paidAmount);
    }
    
    public boolean isPaid() {
        return paymentStatus == PaymentStatus.PAID;
    }
    
    public boolean isOverdue() {
        return paymentStatus == PaymentStatus.OVERDUE;
    }
    
    public void calculateTotalAmount() {
        BigDecimal orderTotal = subtotal.subtract(discountAmount).add(taxAmount).add(shippingCost);
        this.totalAmount = orderTotal;
    }
}
