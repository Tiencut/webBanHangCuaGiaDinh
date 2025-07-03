package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"parent", "children"})
public class Category extends BaseEntity {
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "name")
    private String name;
    
    @Size(max = 500)
    @Column(name = "description")
    private String description;
    
    @Column(name = "code", unique = true)
    private String code;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(name = "level")
    private Integer level = 0;
    
    @Column(name = "path")
    private String path; // Đường dẫn: /1/2/3
    
    // Self-reference cho cây phân loại
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    private List<Category> children = new ArrayList<>();
    
    // Business methods
    public boolean isRoot() {
        return parent == null;
    }
    
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }
    
    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
        child.setLevel(this.level + 1);
        child.updatePath();
    }
    
    public void updatePath() {
        if (parent == null) {
            this.path = "/" + this.getId();
        } else {
            this.path = parent.getPath() + "/" + this.getId();
        }
    }
}
