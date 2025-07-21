package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "product_attribute_values")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductAttributeValue extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id")
    private ProductAttribute attribute;

    @Column(name = "value", columnDefinition = "TEXT")
    private String value;
} 