package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.UserDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all active users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("GET /api/users - Fetching all users");
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/page")
    @Operation(summary = "Get users with pagination", description = "Retrieve users with pagination support")
    public ResponseEntity<Page<UserDto>> getUsersWithPagination(Pageable pageable) {
        log.info("GET /api/users/page - Fetching users with pagination: {}", pageable);
        Page<UserDto> users = userService.getUsersWithPagination(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{} - Fetching user by id", id);
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieve a specific user by their username")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        log.info("GET /api/users/username/{} - Fetching user by username", username);
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieve a specific user by their email")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        log.info("GET /api/users/email/{} - Fetching user by email", email);
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(summary = "Create new user", description = "Create a new user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto, @RequestParam String password) {
        log.info("POST /api/users - Creating new user: {}", userDto.getUsername());
        UserDto user = userService.createUser(userDto, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        log.info("PUT /api/users/{} - Updating user", id);
        UserDto user = userService.updateUser(id, userDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user (soft delete)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/users/{} - Deleting user", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/change-password")
    @Operation(summary = "Change password", description = "Change user password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        log.info("PUT /api/users/{}/change-password - Changing password", id);
        userService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reset-password")
    @Operation(summary = "Reset password", description = "Reset user password")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id, @RequestParam String newPassword) {
        log.info("PUT /api/users/{}/reset-password - Resetting password", id);
        userService.resetPassword(id, newPassword);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/update-last-login")
    @Operation(summary = "Update last login", description = "Update user's last login timestamp")
    public ResponseEntity<Void> updateLastLogin(@PathVariable Long id) {
        log.info("PUT /api/users/{}/update-last-login - Updating last login", id);
        userService.updateLastLogin(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Get users by role", description = "Retrieve users by role")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable User.Role role) {
        log.info("GET /api/users/role/{} - Fetching users by role", role);
        List<UserDto> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get users by status", description = "Retrieve users by status")
    public ResponseEntity<List<UserDto>> getUsersByStatus(@PathVariable User.UserStatus status) {
        log.info("GET /api/users/status/{} - Fetching users by status", status);
        List<UserDto> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Search users by keyword")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String keyword) {
        log.info("GET /api/users/search - Searching users with keyword: {}", keyword);
        List<UserDto> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate user", description = "Activate a user")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        log.info("PUT /api/users/{}/activate - Activating user", id);
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivate a user")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        log.info("PUT /api/users/{}/deactivate - Deactivating user", id);
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate-credentials")
    @Operation(summary = "Validate credentials", description = "Validate user credentials")
    public ResponseEntity<Boolean> validateCredentials(@RequestParam String username, @RequestParam String password) {
        log.info("POST /api/users/validate-credentials - Validating credentials for username: {}", username);
        boolean isValid = userService.validateCredentials(username, password);
        return ResponseEntity.ok(isValid);
    }
}
