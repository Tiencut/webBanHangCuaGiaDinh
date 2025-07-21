package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "product_attributes")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductAttribute extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AttributeType type;

    @Column(name = "unit")
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public enum AttributeType {
        TEXT, NUMBER, SELECT, BOOL
    }
} 