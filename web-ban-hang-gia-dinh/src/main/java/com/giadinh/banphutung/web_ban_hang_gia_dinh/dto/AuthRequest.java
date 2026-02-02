package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class AuthRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
