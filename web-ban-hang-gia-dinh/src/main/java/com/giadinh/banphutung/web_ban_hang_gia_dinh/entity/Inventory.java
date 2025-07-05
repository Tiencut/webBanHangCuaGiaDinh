package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "inventory")
@Data
@EqualsAndHashCode(callSuper = true)
public class Inventory extends BaseEntity {
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    
    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;
    
    @Column(name = "reserved_quantity")
    private Integer reservedQuantity = 0;
    
    @Column(name = "cost_price", precision = 19, scale = 2)
    private BigDecimal costPrice;
    
    @Column(name = "batch_number")
    private String batchNumber;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "notes")
    private String notes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InventoryStatus status = InventoryStatus.AVAILABLE;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    public enum InventoryStatus {
        AVAILABLE, RESERVED, DAMAGED, EXPIRED, RETURNED
    }
    
    // Business methods
    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    public Integer getAvailableQuantity() {
        Integer reserved = this.reservedQuantity != null ? this.reservedQuantity : 0;
        return this.quantity - reserved;
    }
    
    public void addQuantity(int additionalQuantity) {
        this.quantity += additionalQuantity;
        updateTimestamp();
    }
    
    public void removeQuantity(int quantityToRemove) {
        if (quantityToRemove > getAvailableQuantity()) {
            throw new IllegalArgumentException("Không đủ hàng trong kho");
        }
        this.quantity -= quantityToRemove;
        updateTimestamp();
    }
    
    public void reserveQuantity(int quantityToReserve) {
        if (quantityToReserve > getAvailableQuantity()) {
            throw new IllegalArgumentException("Không đủ hàng có sẵn để đặt trước");
        }
        Integer currentReserved = this.reservedQuantity != null ? this.reservedQuantity : 0;
        this.reservedQuantity = currentReserved + quantityToReserve;
        updateTimestamp();
    }
}
