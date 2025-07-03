package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    
    private String token;
    private String username;
    private String fullName;
    private String email;
    private String role;
}
