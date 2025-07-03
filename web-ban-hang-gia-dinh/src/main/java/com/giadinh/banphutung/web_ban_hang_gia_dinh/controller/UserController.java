package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User.UserRole;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    // GET /api/users - Lấy tất cả user active
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Getting all active users");
        List<User> users = userService.findAllActiveUsers();
        return ResponseEntity.ok(users);
    }
    
    // GET /api/users/{id} - Lấy user theo ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Getting user with id: {}", id);
        return userService.findById(id)
            .map(user -> ResponseEntity.ok(user))
            .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/users/username/{username} - Lấy user theo username
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        log.info("Getting user with username: {}", username);
        return userService.findByUsername(username)
            .map(user -> ResponseEntity.ok(user))
            .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/users/role/{role} - Lấy user theo role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable UserRole role) {
        log.info("Getting users with role: {}", role);
        List<User> users = userService.findByRole(role);
        return ResponseEntity.ok(users);
    }
    
    // GET /api/users/search - Tìm kiếm user theo tên
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        log.info("Searching users with name: {}", name);
        List<User> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
    
    // POST /api/users - Tạo user mới
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Creating new user: {}", user.getUsername());
        try {
            User savedUser = userService.createUser(user);
            // Ẩn password trong response
            savedUser.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (RuntimeException e) {
            log.error("Error creating user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT /api/users/{id} - Cập nhật user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user) {
        log.info("Updating user with id: {}", id);
        try {
            User updatedUser = userService.updateUser(id, user);
            // Ẩn password trong response
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            log.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT /api/users/{id}/password - Đổi password
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody PasswordChangeRequest request) {
        log.info("Changing password for user id: {}", id);
        try {
            userService.changePassword(id, request.oldPassword, request.newPassword);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error changing password: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT /api/users/{id}/toggle-status - Khóa/mở khóa user
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<Void> toggleUserStatus(@PathVariable Long id) {
        log.info("Toggling status for user id: {}", id);
        try {
            userService.toggleUserStatus(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error toggling user status: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DELETE /api/users/{id} - Xóa user (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DTO cho đổi password
    public static class PasswordChangeRequest {
        public String oldPassword;
        public String newPassword;
    }
}
