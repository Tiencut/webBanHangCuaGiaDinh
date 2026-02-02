package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentTransaction extends BaseEntity {

    public enum Status { PENDING, SUCCESS, FAILED }

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "provider")
    private String provider;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "provider_txn_id")
    private String providerTxnId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.PENDING;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "webhook_processed")
    private Boolean webhookProcessed = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
