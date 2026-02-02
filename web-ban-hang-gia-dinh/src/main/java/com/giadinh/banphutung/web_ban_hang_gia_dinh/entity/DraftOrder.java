package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "draft_orders")
@Data
public class DraftOrder extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "payload", columnDefinition = "text")
    private String payload; // store JSON

    @Column(name = "last_saved")
    private LocalDateTime lastSaved = LocalDateTime.now();
}
