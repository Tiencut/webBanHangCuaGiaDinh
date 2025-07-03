package com.giadinh.banphutung.web_ban_hang_gia_dinh.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ErrorResponse - Class định nghĩa cấu trúc response khi có lỗi
 * 
 * Đây là class đại diện cho response trả về khi xảy ra lỗi trong API.
 * Thay vì trả về lỗi mặc định của Spring Boot (thường khó hiểu),
 * ta sẽ trả về một JSON có cấu trúc thống nhất, dễ hiểu.
 * 
 * Cấu trúc JSON response:
 * {
 *   "timestamp": "2024-01-15T10:30:00",
 *   "status": 400,
 *   "error": "Bad Request",
 *   "message": "Validation failed",
 *   "path": "/api/customers",
 *   "validationErrors": {
 *     "name": "Tên không được để trống",
 *     "email": "Email không đúng định dạng"
 *   }
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * Thời gian xảy ra lỗi
     */
    private LocalDateTime timestamp;
    
    /**
     * HTTP status code (400, 404, 500, ...)
     */
    private int status;
    
    /**
     * Tên lỗi (Bad Request, Not Found, Internal Server Error, ...)
     */
    private String error;
    
    /**
     * Thông báo lỗi chính (tiếng Việt, dễ hiểu cho user)
     */
    private String message;
    
    /**
     * Đường dẫn API gây ra lỗi
     */
    private String path;
    
    /**
     * Chi tiết lỗi validation (nếu có)
     * Key: tên field, Value: thông báo lỗi
     */
    private Map<String, String> validationErrors;
    
    /**
     * Thông tin debug bổ sung (chỉ hiển thị trong development)
     */
    private String debugInfo;
}
