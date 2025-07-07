package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho việc import dữ liệu nhà cung cấp từ file CSV KiotViet
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KiotVietSupplierImportDTO {
    
    // Dữ liệu gốc từ CSV KiotViet
    private String code;                    // Mã nhà cung cấp
    private String name;                    // Tên nhà cung cấp  
    private String phone;                   // Số điện thoại
    private String email;                   // Email
    private String address;                 // Địa chỉ
    private String city;                    // Thành phố
    private String district;                // Quận/Huyện
    private String ward;                    // Phường/Xã
    private String region;                  // Vùng miền
    private String supplierGroup;           // Nhóm nhà cung cấp
    private BigDecimal totalPurchased;      // Tổng tiền đã mua
    private BigDecimal currentDebt;         // Công nợ hiện tại
    private LocalDate lastTransactionDate;  // Ngày giao dịch cuối
    private String notes;                   // Ghi chú
    
    // Metadata cho import
    private int rowNumber;                  // Số dòng trong file
    private ImportStatus status;            // Trạng thái import
    private String errorMessage;            // Thông báo lỗi
    private boolean isValid;                // Có hợp lệ không
    
    public enum ImportStatus {
        PENDING("Chờ xử lý"),
        VALID("Dữ liệu hợp lệ"),
        INVALID("Dữ liệu không hợp lệ"),
        DUPLICATE("Trùng lặp"),
        IMPORTED("Đã import"),
        FAILED("Thất bại");
        
        private final String description;
        
        ImportStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Kiểm tra dữ liệu có hợp lệ không
     */
    public boolean validate() {
        StringBuilder errors = new StringBuilder();
        
        // Kiểm tra các trường bắt buộc
        if (name == null || name.trim().isEmpty()) {
            errors.append("Tên nhà cung cấp không được để trống; ");
        }
        
        if (phone == null || phone.trim().isEmpty()) {
            errors.append("Số điện thoại không được để trống; ");
        }
        
        // Kiểm tra format email
        if (email != null && !email.trim().isEmpty()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")) {
                errors.append("Email không đúng định dạng; ");
            }
        }
        
        // Kiểm tra format phone
        if (phone != null && !phone.trim().isEmpty()) {
            String cleanPhone = phone.replaceAll("[^0-9]", "");
            if (cleanPhone.length() < 10 || cleanPhone.length() > 11) {
                errors.append("Số điện thoại phải có 10-11 chữ số; ");
            }
        }
        
        // Kiểm tra số tiền
        if (totalPurchased != null && totalPurchased.compareTo(BigDecimal.ZERO) < 0) {
            errors.append("Tổng tiền mua không được âm; ");
        }
        
        if (currentDebt != null && currentDebt.compareTo(BigDecimal.ZERO) < 0) {
            errors.append("Công nợ hiện tại không được âm; ");
        }
        
        // Cập nhật trạng thái
        if (errors.length() > 0) {
            this.errorMessage = errors.toString();
            this.status = ImportStatus.INVALID;
            this.isValid = false;
        } else {
            this.status = ImportStatus.VALID;
            this.isValid = true;
        }
        
        return this.isValid;
    }
    
    /**
     * Chuẩn hóa dữ liệu
     */
    public void normalize() {
        // Chuẩn hóa tên
        if (name != null) {
            name = name.trim();
        }
        
        // Chuẩn hóa phone
        if (phone != null) {
            phone = phone.replaceAll("[^0-9]", "");
        }
        
        // Chuẩn hóa email
        if (email != null) {
            email = email.trim().toLowerCase();
        }
        
        // Chuẩn hóa code
        if (code != null) {
            code = code.trim().toUpperCase();
        }
        
        // Chuẩn hóa địa chỉ
        if (address != null) {
            address = address.trim();
        }
        
        if (city != null) {
            city = city.trim();
        }
        
        if (district != null) {
            district = district.trim();
        }
        
        if (ward != null) {
            ward = ward.trim();
        }
        
        if (region != null) {
            region = region.trim();
        }
        
        if (supplierGroup != null) {
            supplierGroup = supplierGroup.trim();
        }
        
        if (notes != null) {
            notes = notes.trim();
        }
    }
}
