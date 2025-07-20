package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User.UserRole;

@Repository
@RepositoryRestResource(path = "users", collectionResourceRel = "users", itemResourceRel = "user")
public interface UserRepository extends JpaRepository<User, Long> {
    
    // ===== PROJECTION INTERFACE =====
    // Để customize response và ẩn password
    interface UserProjection {
        Long getId();
        String getUsername();
        String getFullName();
        String getEmail();
        String getPhone();
        UserRole getRole();
        User.UserStatus getStatus();
        Boolean getIsActive();
        java.time.LocalDateTime getLastLogin();
        java.time.LocalDateTime getCreatedAt();
        java.time.LocalDateTime getUpdatedAt();
    }
    
    // Tìm user theo username
    @RestResource(path = "by-username", rel = "by-username")
    Optional<User> findByUsername(String username);
    
    // Tìm user theo email
    @RestResource(path = "by-email", rel = "by-email")
    Optional<User> findByEmail(String email);
    
    // Tìm user theo username hoặc email
    @RestResource(exported = false) // Không expose endpoint này
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    // Tìm user theo role
    @RestResource(path = "by-role", rel = "by-role")
    List<User> findByRole(UserRole role);
    
    // Tìm user đang active
    @RestResource(path = "active", rel = "active")
    List<User> findByIsActiveTrue();
    
    // Tìm user theo role và active
    @RestResource(path = "by-role-active", rel = "by-role-active")
    List<User> findByRoleAndIsActiveTrue(UserRole role);
    
    // Kiểm tra username đã tồn tại chưa
    @RestResource(exported = false) // Không expose endpoint này
    boolean existsByUsername(String username);
    
    // Kiểm tra email đã tồn tại chưa
    @RestResource(exported = false) // Không expose endpoint này
    boolean existsByEmail(String email);
    
    // Tìm user theo phone
    @RestResource(path = "by-phone", rel = "by-phone")
    Optional<User> findByPhone(String phone);
    
    // Custom query - tìm user theo tên đầy đủ (không phân biệt hoa thường)
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    @RestResource(path = "by-name", rel = "by-name")
    List<User> findByFullNameContainingIgnoreCase(@Param("name") String name);
    
    // === Các method bổ sung cho UserService ===
    
    // Tìm user đang active với phân trang
    @RestResource(path = "active-page", rel = "active-page")
    Page<User> findByIsActiveTrue(Pageable pageable);
    
    // Tìm user theo id và active
    @RestResource(exported = false) // Không expose endpoint này
    Optional<User> findByIdAndIsActiveTrue(Long id);
    
    // Tìm user theo username và active
    @RestResource(exported = false) // Không expose endpoint này
    Optional<User> findByUsernameAndIsActiveTrue(String username);
    
    // Tìm user theo email và active
    @RestResource(exported = false) // Không expose endpoint này
    Optional<User> findByEmailAndIsActiveTrue(String email);
    
    // Tìm user theo status và active
    @RestResource(path = "by-status-active", rel = "by-status-active")
    List<User> findByStatusAndIsActiveTrue(User.UserStatus status);
    
    // Tìm kiếm user theo keyword
    @Query("SELECT u FROM User u WHERE u.isActive = true AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    @RestResource(path = "search", rel = "search")
    List<User> searchUsers(@Param("keyword") String keyword);
}
