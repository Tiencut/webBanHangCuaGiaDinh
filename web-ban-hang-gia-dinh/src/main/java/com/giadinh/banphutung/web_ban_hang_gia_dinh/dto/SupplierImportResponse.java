package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho response của quá trình import nhà cung cấp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierImportResponse {
    
    private boolean success;
    private String message;
    private ImportSummary summary;
    private List<KiotVietSupplierImportDTO> importedData;
    private List<String> errors;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImportSummary {
        private int totalRows;          // Tổng số dòng
        private int validRows;          // Số dòng hợp lệ
        private int invalidRows;        // Số dòng không hợp lệ
        private int duplicateRows;      // Số dòng trùng lặp
        private int importedRows;       // Số dòng đã import
        private int failedRows;         // Số dòng thất bại
        private int skippedRows;        // Số dòng bỏ qua
        
        public double getSuccessRate() {
            if (totalRows == 0) return 0.0;
            return (double) importedRows / totalRows * 100;
        }
        
        public boolean hasErrors() {
            return invalidRows > 0 || duplicateRows > 0 || failedRows > 0;
        }
    }
    
    /**
     * Tạo response thành công
     */
    public static SupplierImportResponse success(ImportSummary summary, List<KiotVietSupplierImportDTO> importedData) {
        SupplierImportResponse response = new SupplierImportResponse();
        response.setSuccess(true);
        response.setMessage("Import dữ liệu thành công");
        response.setSummary(summary);
        response.setImportedData(importedData);
        return response;
    }
    
    /**
     * Tạo response thất bại
     */
    public static SupplierImportResponse failure(String message, List<String> errors) {
        SupplierImportResponse response = new SupplierImportResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrors(errors);
        return response;
    }
    
    /**
     * Tạo response có cảnh báo
     */
    public static SupplierImportResponse warning(String message, ImportSummary summary, 
                                               List<KiotVietSupplierImportDTO> importedData, 
                                               List<String> errors) {
        SupplierImportResponse response = new SupplierImportResponse();
        response.setSuccess(true);
        response.setMessage(message);
        response.setSummary(summary);
        response.setImportedData(importedData);
        response.setErrors(errors);
        return response;
    }
}
