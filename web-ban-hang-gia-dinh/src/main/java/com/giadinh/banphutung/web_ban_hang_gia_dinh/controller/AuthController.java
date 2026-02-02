package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.AuthRequest;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.AuthResponse;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.RegisterRequest;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.UserDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.UserRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.UserService;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.security.JwtUtil;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetService passwordResetService;
    private final com.giadinh.banphutung.web_ban_hang_gia_dinh.service.RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest req) {
        UserDto dto = new UserDto();
        dto.setUsername(req.getUsername());
        dto.setEmail(req.getEmail());
        dto.setFullName(req.getFullName());
        dto.setPhone(req.getPhone());
        dto.setRole(User.UserRole.CUSTOMER);
        UserDto created = userService.createUser(dto, req.getPassword());
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        boolean valid = userService.validateCredentials(req.getUsername(), req.getPassword());
        if (!valid) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findByUsernameAndIsActiveTrue(req.getUsername()).orElseThrow();
        userService.updateLastLogin(user.getId());

        List<String> roles = List.of(user.getRole().name());
        String token = JwtUtil.generateToken(user.getUsername(), roles);

        // create refresh token
        var refresh = refreshTokenService.createTokenForUser(user);

        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setRefreshToken(refresh.getToken());
        resp.setUser(userService.getUserById(user.getId()));
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) {
        var userOpt = refreshTokenService.validateAndGetUser(refreshToken, userService);
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();
        var user = userOpt.get();

        // rotate refresh token: revoke old and issue new
        refreshTokenService.revokeToken(refreshToken);
        var newRefresh = refreshTokenService.createTokenForUser(user);

        List<String> roles = List.of(user.getRole().name());
        String token = JwtUtil.generateToken(user.getUsername(), roles);

        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setRefreshToken(newRefresh.getToken());
        resp.setUser(userService.getUserById(user.getId()));
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam(required = false) String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.revokeToken(refreshToken);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        if (!JwtUtil.validateToken(token)) return ResponseEntity.status(401).build();

        String username = JwtUtil.getUsername(token);
        User user = userRepository.findByUsernameAndIsActiveTrue(username).orElseThrow();
        return ResponseEntity.ok(userService.getUserById(user.getId()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email, @RequestParam(required = false) String frontendUrl) {
        // frontendUrl used to build reset link; fallback to config
        if (frontendUrl == null || frontendUrl.isBlank()) {
            frontendUrl = System.getenv().getOrDefault("FRONTEND_RESET_URL", "http://localhost:3000/reset-password");
        }
        var prt = passwordResetService.createTokenForEmail(email, frontendUrl);
        if (prt == null) return ResponseEntity.ok().build(); // do not reveal existence
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        var userOpt = passwordResetService.validateToken(token);
        if (userOpt.isEmpty()) return ResponseEntity.status(400).build();
        var user = userOpt.get();
        userService.resetPassword(user.getId(), newPassword);
        passwordResetService.consumeToken(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-reset")
    public ResponseEntity<Void> validateReset(@RequestParam String token) {
        var userOpt = passwordResetService.validateToken(token);
        if (userOpt.isEmpty()) return ResponseEntity.status(400).build();
        return ResponseEntity.ok().build();
    }
}
