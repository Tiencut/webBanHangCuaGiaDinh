package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Data
@EqualsAndHashCode(callSuper = false)
public class Note extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Column(name = "title", nullable = false, length = 255)
    private String title;


    @Column(name = "completed")
    private Boolean completed = false;

    @Column(name = "due_date")
    private LocalDateTime dueDate;
}
