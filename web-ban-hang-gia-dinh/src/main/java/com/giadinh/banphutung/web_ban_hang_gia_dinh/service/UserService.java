package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.UserDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper.UserMapper;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findByIsActiveTrue();
        return userMapper.toDtoList(users);
    }

    public Page<UserDto> getUsersWithPagination(Pageable pageable) {
        log.info("Fetching users with pagination: {}", pageable);
        Page<User> users = userRepository.findByIsActiveTrue(pageable);
        return users.map(userMapper::toDto);
    }

    public UserDto getUserById(Long id) {
        log.info("Fetching user by id: {}", id);
        User user = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    public UserDto getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        User user = userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }

    public UserDto getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }

    public UserDto createUser(UserDto userDto, String password) {
        log.info("Creating new user: {}", userDto.getUsername());
        
        // Check if username already exists
        Optional<User> existingUser = userRepository.findByUsernameAndIsActiveTrue(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new BusinessException("Username already exists: " + userDto.getUsername());
        }

        // Check if email already exists
        if (userDto.getEmail() != null && !userDto.getEmail().trim().isEmpty()) {
            Optional<User> existingEmail = userRepository.findByEmailAndIsActiveTrue(userDto.getEmail());
            if (existingEmail.isPresent()) {
                throw new BusinessException("Email already exists: " + userDto.getEmail());
            }
        }

        User user = new User();
        userMapper.updateEntityFromDto(userDto, user);
        user.setPassword(passwordEncoder.encode(password));
        user.setIsActive(true);
        user.setStatus(User.UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        log.info("Updating user with id: {}", id);
        User user = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check if new username conflicts with existing user
        if (userDto.getUsername() != null && !userDto.getUsername().equals(user.getUsername())) {
            Optional<User> existingUser = userRepository.findByUsernameAndIsActiveTrue(userDto.getUsername());
            if (existingUser.isPresent()) {
                throw new BusinessException("Username already exists: " + userDto.getUsername());
            }
        }

        // Check if new email conflicts with existing user
        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            Optional<User> existingEmail = userRepository.findByEmailAndIsActiveTrue(userDto.getEmail());
            if (existingEmail.isPresent()) {
                throw new BusinessException("Email already exists: " + userDto.getEmail());
            }
        }

        userMapper.updateEntityFromDto(userDto, user);
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with id: {}", updatedUser.getId());
        return userMapper.toDto(updatedUser);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        User user = userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        user.setIsActive(false);
        userRepository.save(user);
        log.info("User deleted successfully with id: {}", id);
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        log.info("Changing password for user id: {}", userId);
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("Old password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed successfully for user id: {}", userId);
    }

    public void resetPassword(Long userId, String newPassword) {
        log.info("Resetting password for user id: {}", userId);
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password reset successfully for user id: {}", userId);
    }

    public void updateLastLogin(Long userId) {
        log.info("Updating last login for user id: {}", userId);
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("Last login updated successfully for user id: {}", userId);
    }

    public List<UserDto> getUsersByRole(User.Role role) {
        log.info("Fetching users by role: {}", role);
        List<User> users = userRepository.findByRoleAndIsActiveTrue(role);
        return userMapper.toDtoList(users);
    }

    public List<UserDto> getUsersByStatus(User.UserStatus status) {
        log.info("Fetching users by status: {}", status);
        List<User> users = userRepository.findByStatusAndIsActiveTrue(status);
        return userMapper.toDtoList(users);
    }

    public List<UserDto> searchUsers(String keyword) {
        log.info("Searching users with keyword: {}", keyword);
        List<User> users = userRepository.searchUsers(keyword);
        return userMapper.toDtoList(users);
    }

    public void activateUser(Long userId) {
        log.info("Activating user with id: {}", userId);
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        user.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(user);
        log.info("User activated successfully with id: {}", userId);
    }

    public void deactivateUser(Long userId) {
        log.info("Deactivating user with id: {}", userId);
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        user.setStatus(User.UserStatus.INACTIVE);
        userRepository.save(user);
        log.info("User deactivated successfully with id: {}", userId);
    }

    public boolean validateCredentials(String username, String password) {
        log.info("Validating credentials for username: {}", username);
        Optional<User> user = userRepository.findByUsernameAndIsActiveTrue(username);
        if (user.isPresent()) {
            boolean isValid = passwordEncoder.matches(password, user.get().getPassword());
            log.info("Credential validation result: {}", isValid);
            return isValid;
        }
        log.info("User not found for username: {}", username);
        return false;
    }
}
