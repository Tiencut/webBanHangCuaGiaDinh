package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.KiotVietImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller để xử lý import dữ liệu từ KiotViet
 */
@RestController
@RequestMapping("/api/kiotviet")
@CrossOrigin(origins = "*")
public class KiotVietImportController {

    private static final Logger logger = LoggerFactory.getLogger(KiotVietImportController.class);

    @Autowired
    private KiotVietImportService kiotVietImportService;

    /**
     * Validate file CSV trước khi import
     */
    @PostMapping(value = "/suppliers/validate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> validateSuppliersFile(
            @RequestParam("file") MultipartFile file) {
        
        logger.info("Validating KiotViet suppliers file: {}", file.getOriginalFilename());
        
        try {
            // Validate file type
            if (!isValidCSVFile(file)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid file type. Please upload a CSV file.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validate file content
            Map<String, Object> validationResult = kiotVietImportService.validateKiotVietFile(file);
            validationResult.put("success", true);
            validationResult.put("message", "File validation completed");
            
            return ResponseEntity.ok(validationResult);
            
        } catch (Exception e) {
            logger.error("Error validating file: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error validating file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Import suppliers từ file CSV KiotViet
     */
    @PostMapping(value = "/suppliers/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> importSuppliers(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "skipValidation", defaultValue = "false") boolean skipValidation) {
        
        logger.info("Importing KiotViet suppliers from file: {}", file.getOriginalFilename());
        
        try {
            // Validate file type
            if (!isValidCSVFile(file)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid file type. Please upload a CSV file.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validate file content nếu không skip
            if (!skipValidation) {
                Map<String, Object> validationResult = kiotVietImportService.validateKiotVietFile(file);
                @SuppressWarnings("unchecked")
                boolean isValid = (boolean) validationResult.get("isValid");
                
                if (!isValid) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "File validation failed");
                    response.put("validationResult", validationResult);
                    return ResponseEntity.badRequest().body(response);
                }
            }
            
            // Import dữ liệu
            Map<String, Object> importResult = kiotVietImportService.importSuppliersFromKiotViet(file);
            importResult.put("success", true);
            importResult.put("message", "Import completed successfully");
            
            return ResponseEntity.ok(importResult);
            
        } catch (Exception e) {
            logger.error("Error importing suppliers: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error importing suppliers: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Lấy thông tin về format file CSV KiotViet
     */
    @GetMapping("/suppliers/format")
    public ResponseEntity<Map<String, Object>> getFileFormat() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("success", true);
        response.put("message", "KiotViet supplier CSV format information");
        response.put("fileFormat", Map.of(
            "type", "CSV",
            "encoding", "UTF-8",
            "delimiter", ",",
            "hasHeader", true,
            "columns", new String[]{
                "Mã nhà cung cấp", // code
                "Tên nhà cung cấp", // name
                "Công ty", // company
                "Địa chỉ", // address
                "Tỉnh/Thành phố", // region
                "Quận/Huyện", // ward
                "Điện thoại", // phone
                "Email", // email
                "Nhóm nhà cung cấp", // supplierGroup
                "Tổng mua", // totalPurchased
                "Nợ hiện tại", // currentDebt
                "Trạng thái", // isActive
                "Người tạo" // createdBy
            }
        ));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Lấy thống kê về suppliers hiện tại
     */
    @GetMapping("/suppliers/stats")
    public ResponseEntity<Map<String, Object>> getSuppliersStats() {
        try {
            // TODO: Implement statistics from SupplierService
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Supplier statistics");
            response.put("stats", Map.of(
                "totalSuppliers", 0,
                "activeSuppliers", 0,
                "inactiveSuppliers", 0,
                "suppliersWithDebt", 0,
                "avgTotalPurchased", 0.0,
                "avgCurrentDebt", 0.0
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error getting supplier statistics: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error getting statistics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Kiểm tra file có phải là CSV không
     */
    private boolean isValidCSVFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }
        
        // Kiểm tra extension
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!"csv".equals(extension)) {
            return false;
        }
        
        // Kiểm tra content type
        String contentType = file.getContentType();
        return contentType != null && (
            contentType.equals("text/csv") ||
            contentType.equals("application/csv") ||
            contentType.equals("text/plain") ||
            contentType.equals("application/vnd.ms-excel")
        );
    }
}
