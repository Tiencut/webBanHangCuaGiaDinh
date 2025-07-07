package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.SupplierImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SupplierImportController {

    @Autowired
    private SupplierImportService supplierImportService;

    @PostMapping("/import")
    public ResponseEntity<?> importSuppliers(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "updateExisting", defaultValue = "false") boolean updateExisting) {
        
        try {
            var result = supplierImportService.importFromCsv(file, updateExisting);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Lỗi khi import dữ liệu: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/template")
    public ResponseEntity<Resource> downloadTemplate() {
        try {
            Resource resource = new ClassPathResource("templates/template_nha_cung_cap.csv");
            
            if (!resource.exists()) {
                // Tạo template mẫu nếu không tồn tại
                String templateContent = "Mã nhà cung cấp,Tên nhà cung cấp,Số điện thoại,Email,Địa chỉ,Thành phố,Quận/Huyện,Phường/Xã,Vùng miền,Nhóm nhà cung cấp,Tổng giá trị mua,Nợ hiện tại,Ngày giao dịch cuối,Ghi chú\n" +
                                       "SUP001,Công ty ABC,0123456789,abc@email.com,123 Đường ABC,Hà Nội,Ba Đình,Phúc Xá,Miền Bắc,Nhóm A,1000000,0,01/01/2024,Ghi chú mẫu";
                
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"template_nha_cung_cap.csv\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(new org.springframework.core.io.ByteArrayResource(templateContent.getBytes("UTF-8")));
            }

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"template_nha_cung_cap.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
                
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
