package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.LoginRequest;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.LoginResponse;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.UserService;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.util.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Authentication Controller - Xử lý đăng nhập, đăng xuất
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Đăng nhập
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Get user details
            User user = userService.getUserByUsername(request.getUsername());
            if (user == null || !user.getIsActive()) {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString());

            // Create response
            LoginResponse response = LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Login failed: " + e.getMessage());
        }
    }

    /**
     * Lấy thông tin user hiện tại
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Unauthorized");
            }

            User user = userService.getUserByUsername(authentication.getName());
            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }

            LoginResponse response = LoginResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to get user info: " + e.getMessage());
        }
    }

    /**
     * Đăng xuất (chỉ là endpoint placeholder vì JWT stateless)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }
}
