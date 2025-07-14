package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User.UserRole;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User.UserStatus;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.UserRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    // Tạo user mới
    public User createUser(User user) {
        log.info("Creating new user: {}", user.getUsername());
        
        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException("Username đã tồn tại: " + user.getUsername());
        }
        
        // Kiểm tra email đã tồn tại
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("Email đã tồn tại: " + user.getEmail());
        }
        
        // Mã hóa password
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Set default values
        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }
        
        return userRepository.save(user);
    }
    
    // Tìm user theo ID
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    // Tìm user theo username
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    // Tìm user theo email
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Tìm tất cả user active
    @Transactional(readOnly = true)
    public List<User> findAllActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
    
    // Tìm user theo role
    @Transactional(readOnly = true)
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRoleAndIsActiveTrue(role);
    }
    
    // Cập nhật user
    public User updateUser(Long id, User userUpdate) {
        log.info("Updating user with id: {}", id);
        
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        // Cập nhật thông tin cơ bản
        existingUser.setFullName(userUpdate.getFullName());
        existingUser.setEmail(userUpdate.getEmail());
        existingUser.setPhone(userUpdate.getPhone());
        existingUser.setRole(userUpdate.getRole());
        existingUser.setStatus(userUpdate.getStatus());
        
        // Cập nhật email nếu khác
        if (userUpdate.getEmail() != null && 
            !userUpdate.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(userUpdate.getEmail())) {
                throw new BusinessException("Email đã tồn tại: " + userUpdate.getEmail());
            }
            existingUser.setEmail(userUpdate.getEmail());
        }
        
        return userRepository.save(existingUser);
    }
    
    // Đổi password
    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Kiểm tra password cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("Password cũ không đúng");
        }
        
        // Cập nhật password mới
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("Password changed for user: {}", user.getUsername());
    }
    
    // Reset password
    public void resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("Password reset for user: {}", user.getUsername());
    }

    // Khóa/mở khóa user
    public void toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (user.getStatus() == UserStatus.ACTIVE) {
            user.setStatus(UserStatus.INACTIVE);
        } else if (user.getStatus() == UserStatus.INACTIVE) {
            user.setStatus(UserStatus.ACTIVE);
        }
        
        userRepository.save(user);
        log.info("User {} status changed to: {}", user.getUsername(), user.getStatus());
    }
    
    // Cập nhật lần login cuối
    public void updateLastLogin(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            user.setFailedLoginAttempts(0); // Reset failed attempts
            userRepository.save(user);
        }
    }
    
    // Tăng số lần login thất bại
    public void incrementFailedLoginAttempts(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            
            // Khóa tài khoản nếu thất bại quá 5 lần
            if (user.getFailedLoginAttempts() >= 5) {
                user.setAccountLockedUntil(LocalDateTime.now().plusHours(1));
                log.warn("User {} locked due to too many failed login attempts", username);
            }
            
            userRepository.save(user);
        }
    }
    
    // Kiểm tra user có bị khóa không
    @Transactional(readOnly = true)
    public boolean isUserLocked(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getAccountLockedUntil() != null && 
                   user.getAccountLockedUntil().isAfter(LocalDateTime.now());
        }
        return false;
    }
    
    // Tìm kiếm user theo tên
    @Transactional(readOnly = true)
    public List<User> searchUsersByName(String name) {
        return userRepository.findByFullNameContainingIgnoreCase(name);
    }
    
    // Xóa user (soft delete)
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setIsActive(false);
        userRepository.save(user);
        
        log.info("User {} has been deactivated", user.getUsername());
    }
}
