package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Data
public class KiotVietSupplierDTO {
    
    // Mapping từ KiotViet CSV columns
    @NotBlank(message = "Mã nhà cung cấp không được để trống")
    @Size(max = 20, message = "Mã nhà cung cấp không được quá 20 ký tự")
    private String supplierCode; // Mã nhà cung cấp
    
    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    @Size(max = 200, message = "Tên nhà cung cấp không được quá 200 ký tự")
    private String name; // Tên nhà cung cấp
    
    @Email(message = "Email không hợp lệ")
    private String email; // Email
    
    @Size(max = 15, message = "Số điện thoại không được quá 15 ký tự")
    private String phone; // Điện thoại
    
    @Size(max = 500, message = "Địa chỉ không được quá 500 ký tự")
    private String address; // Địa chỉ
    
    @Size(max = 100, message = "Khu vực không được quá 100 ký tự")
    private String region; // Khu vực
    
    @Size(max = 100, message = "Phường/Xã không được quá 100 ký tự")
    private String ward; // Phường/Xã
    
    private BigDecimal totalPurchased; // Tổng mua
    
    private BigDecimal currentDebt; // Nợ cần trả hiện tại
    
    @Size(max = 20, message = "Mã số thuế không được quá 20 ký tự")
    private String taxCode; // Mã số thuế
    
    @Size(max = 1000, message = "Ghi chú không được quá 1000 ký tự")
    private String notes; // Ghi chú
    
    @Size(max = 100, message = "Nhóm nhà cung cấp không được quá 100 ký tự")
    private String supplierGroup; // Nhóm nhà cung cấp
    
    private Integer status; // Trạng thái (1 = Active, 0 = Inactive)
    
    private String company; // Công ty
    
    private String createdBy; // Người tạo
    
    // Helper method để convert status từ KiotViet
    public Boolean getIsActive() {
        return status != null && status == 1;
    }
    
    // Helper method để parse BigDecimal từ String
    public static BigDecimal parseMoney(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        try {
            // Remove comma separators và parse
            String cleanValue = value.replace(",", "").replace("\"", "");
            return new BigDecimal(cleanValue);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
