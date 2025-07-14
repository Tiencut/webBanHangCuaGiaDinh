package com.giadinh.banphutung.web_ban_hang_gia_dinh.exception;

/**
 * Exception được throw khi không tìm thấy resource
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 