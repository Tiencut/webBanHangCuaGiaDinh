package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String refreshToken;
    private UserDto user;
}
