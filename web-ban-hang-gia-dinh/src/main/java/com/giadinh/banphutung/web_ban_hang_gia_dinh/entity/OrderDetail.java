package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "order_details")
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderDetail extends BaseEntity {
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "unit_price", precision = 19, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "discount_amount", precision = 19, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "line_total", precision = 19, scale = 2)
    private BigDecimal lineTotal;
    
    @Column(name = "notes")
    private String notes;
    
    // Business methods
    public void calculateLineTotal() {
        BigDecimal total = unitPrice.multiply(new BigDecimal(quantity));
        this.lineTotal = total.subtract(discountAmount);
    }
    
    public BigDecimal getOriginalLineTotal() {
        return unitPrice.multiply(new BigDecimal(quantity));
    }
}
