package com.giadinh.banphutung.web_ban_hang_gia_dinh.exception;

/**
 * Exception được throw khi có lỗi business logic
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
} 