package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.ExcelImportService;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.ExcelTemplateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ExcelImportController - API endpoints cho import dữ liệu từ Excel
 */
@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@Slf4j
public class ExcelImportController {

    private final ExcelImportService excelImportService;
    private final ExcelTemplateService excelTemplateService;

    /**
     * Import dữ liệu từ file Excel tổng hợp
     */
    @PostMapping("/excel")
    public ResponseEntity<Map<String, Object>> importFromExcel(
            @RequestParam("file") MultipartFile file) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate file
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "File không được để trống");
                return ResponseEntity.badRequest().body(response);
            }

            String filename = file.getOriginalFilename();
            if (filename == null || !filename.toLowerCase().endsWith(".xlsx")) {
                response.put("success", false);
                response.put("message", "Chỉ hỗ trợ file .xlsx");
                return ResponseEntity.badRequest().body(response);
            }

            log.info("📥 Nhận file import: {} ({}KB)", filename, file.getSize() / 1024);

            // Import data
            ExcelImportService.ImportResult result = excelImportService.importFromExcel(file);

            response.put("success", result.isSuccess());
            response.put("summary", result.getSummary());
            response.put("successes", result.getSuccesses());
            response.put("warnings", result.getWarnings());
            response.put("errors", result.getErrors());
            response.put("importTime", result.getImportTime());

            HttpStatus status = result.hasErrors() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;
            return ResponseEntity.status(status).body(response);

        } catch (IOException e) {
            log.error("❌ Lỗi đọc file: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Lỗi đọc file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (RuntimeException e) {
            log.error("❌ Lỗi xử lý dữ liệu: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Lỗi xử lý dữ liệu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Download template Excel để import
     */
    @GetMapping("/template")
    public ResponseEntity<ByteArrayResource> downloadTemplate() {
        try {
            log.info("📥 Request download template Excel");
            
            ByteArrayResource template = excelTemplateService.generateTemplate();
            
            String filename = "template-import-data-" + LocalDate.now() + ".xlsx";
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(template.contentLength())
                    .body(template);
                    
        } catch (IOException | RuntimeException e) {
            log.error("❌ Lỗi tạo template: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy thông tin về template (không download file)
     */
    @GetMapping("/template/info")
    public ResponseEntity<Map<String, Object>> getTemplateInfo() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("title", "Template Import Dữ Liệu");
        response.put("description", "File Excel mẫu để import dữ liệu vào hệ thống");
        response.put("fileFormat", ".xlsx (Excel 2007+)");
        response.put("downloadUrl", "/api/import/template");
        response.put("sheets", new String[]{
            "Categories - Danh mục sản phẩm", 
            "Suppliers - Nhà cung cấp", 
            "VehicleModels - Mẫu xe", 
            "Products - Sản phẩm", 
            "Customers - Khách hàng", 
            "TrainingContent - Nội dung đào tạo"
        });
        
        return ResponseEntity.ok(response);
    }

    /**
     * Lấy hướng dẫn format Excel
     */
    @GetMapping("/guide")
    public ResponseEntity<Map<String, Object>> getImportGuide() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("title", "Hướng dẫn import dữ liệu từ Excel");
        response.put("fileFormat", ".xlsx (Excel 2007+)");
        response.put("maxSize", "10MB");
        
        Map<String, Object> sheets = new HashMap<>();
        
        // Categories sheet
        Map<String, Object> categoriesSheet = new HashMap<>();
        categoriesSheet.put("description", "Danh mục sản phẩm");
        categoriesSheet.put("columns", new String[]{
            "Code (Mã danh mục)", "Name (Tên danh mục)", 
            "Description (Mô tả)", "Parent Code (Mã danh mục cha)"
        });
        categoriesSheet.put("example", new String[]{
            "ENGINE", "Phụ tùng động cơ", "Các bộ phận của động cơ", ""
        });
        sheets.put("Categories", categoriesSheet);

        // Suppliers sheet
        Map<String, Object> suppliersSheet = new HashMap<>();
        suppliersSheet.put("description", "Nhà cung cấp");
        suppliersSheet.put("columns", new String[]{
            "Code (Mã NCC)", "Name (Tên NCC)", "Address (Địa chỉ)", 
            "Phone (Điện thoại)", "Email", "Contact Person (Người liên hệ)", "Tax Code (Mã số thuế)"
        });
        suppliersSheet.put("example", new String[]{
            "ISUZU", "Isuzu Vietnam", "HCM", "028-123456", "info@isuzu.vn", "Nguyễn Văn A", "0123456789"
        });
        sheets.put("Suppliers", suppliersSheet);

        // VehicleModels sheet
        Map<String, Object> vehicleSheet = new HashMap<>();
        vehicleSheet.put("description", "Mẫu xe");
        vehicleSheet.put("columns", new String[]{
            "Code (Mã xe)", "Name (Tên xe)", "Brand (Thương hiệu)", 
            "Year From (Năm bắt đầu)", "Year To (Năm kết thúc)", "Tonnage (Tải trọng)", 
            "Engine Model (Động cơ)", "Transmission (Hộp số)", "Vehicle Type (Loại xe)"
        });
        vehicleSheet.put("example", new String[]{
            "HD65", "Hyundai HD65", "HYUNDAI", "2015", "2020", "3.5", "D4BB", "5 cấp số", "LIGHT_TRUCK"
        });
        vehicleSheet.put("vehicleTypes", new String[]{
            "LIGHT_TRUCK", "MEDIUM_TRUCK", "HEAVY_TRUCK", "PICKUP", "VAN", "BUS", "SPECIALIZED"
        });
        sheets.put("VehicleModels", vehicleSheet);

        // Products sheet
        Map<String, Object> productsSheet = new HashMap<>();
        productsSheet.put("description", "Sản phẩm");
        productsSheet.put("columns", new String[]{
            "Code (Mã SP)", "Name (Tên SP)", "Description (Mô tả)", 
            "Category Code (Mã danh mục)", "Base Price (Giá gốc)", "Selling Price (Giá bán)", 
            "Brand (Thương hiệu)", "Part Number (Mã phụ tùng)", "OEM Number (Mã OEM)", 
            "Compatible Vehicles (Xe tương thích - cách nhau bởi dấu phẩy)"
        });
        productsSheet.put("example", new String[]{
            "BP-HD65-001", "Má phanh Hyundai HD65", "Má phanh trước cho HD65", 
            "BRAKE", "250000", "280000", "Sangsin", "58101-5H000", "58101-5H000", "HD65,HD72"
        });
        sheets.put("Products", productsSheet);

        // Customers sheet
        Map<String, Object> customersSheet = new HashMap<>();
        customersSheet.put("description", "Khách hàng");
        customersSheet.put("columns", new String[]{
            "Code (Mã KH)", "Name (Tên KH)", "Phone (Điện thoại)", 
            "Email", "Address (Địa chỉ)", "Tax Code (Mã số thuế)", "Customer Type (Loại KH)"
        });
        customersSheet.put("example", new String[]{
            "KH001", "Nguyễn Văn A", "0909123456", "nguyenvana@email.com", 
            "123 ABC, HCM", "0123456789", "INDIVIDUAL"
        });
        customersSheet.put("customerTypes", new String[]{
            "INDIVIDUAL", "BUSINESS", "GOVERNMENT", "VIP"
        });
        sheets.put("Customers", customersSheet);

        // TrainingContent sheet
        Map<String, Object> trainingSheet = new HashMap<>();
        trainingSheet.put("description", "Nội dung đào tạo");
        trainingSheet.put("columns", new String[]{
            "Title (Tiêu đề)", "Category (Danh mục)", "Summary (Tóm tắt)", 
            "Content (Nội dung HTML)", "Priority (Độ ưu tiên 1-3)", 
            "Read Time (Thời gian đọc - phút)", "Tags (Từ khóa - cách nhau bởi dấu phẩy)", 
            "Show In Quick Help (true/false)"
        });
        trainingSheet.put("example", new String[]{
            "Cách nhận diện má phanh HD65", "PRODUCT_IDENTIFICATION", 
            "Hướng dẫn phân biệt má phanh chính hãng", 
            "<h4>Má phanh HD65</h4><p>OEM: 58101-5H000</p>", 
            "1", "3", "hyundai,brake,hd65", "true"
        });
        trainingSheet.put("categories", new String[]{
            "PRODUCT_IDENTIFICATION", "VEHICLE_KNOWLEDGE", "SALES_TIPS", 
            "TECHNICAL_SPECS", "COMMON_MISTAKES", "QUALITY_CHECK", 
            "CUSTOMER_SERVICE", "PRICING_GUIDE"
        });
        sheets.put("TrainingContent", trainingSheet);

        response.put("sheets", sheets);
        
        return ResponseEntity.ok(response);
    }
}
