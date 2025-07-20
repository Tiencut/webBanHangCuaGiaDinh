package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions")
@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryTransaction extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "document_code")
    private String documentCode;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "partner_name")
    private String partnerName;

    @Column(name = "trans_date")
    private LocalDateTime transDate;

    @Column(name = "trans_price", precision = 19, scale = 2)
    private BigDecimal transPrice;

    @Column(name = "cost", precision = 19, scale = 2)
    private BigDecimal cost;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "ending_stocks")
    private Integer endingStocks;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    public enum TransactionType {
        IMPORT, EXPORT, ADJUST, UPDATE_COST, OTHER
    }
} 