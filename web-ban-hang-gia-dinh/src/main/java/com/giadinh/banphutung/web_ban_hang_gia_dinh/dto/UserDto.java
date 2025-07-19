package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private User.Role role;
    private User.UserStatus status;
    private String avatar;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
} 