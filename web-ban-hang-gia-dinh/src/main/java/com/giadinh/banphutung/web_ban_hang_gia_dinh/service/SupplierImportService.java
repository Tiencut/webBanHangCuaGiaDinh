package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.KiotVietSupplierImportDTO;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.SupplierImportResponse;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service xử lý import dữ liệu nhà cung cấp từ file CSV KiotViet
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierImportService {
    
    private final SupplierRepository supplierRepository;
    
    // Các format ngày có thể có trong file CSV
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("d/M/yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };
    
    /**
     * Import dữ liệu nhà cung cấp từ file CSV
     */
    @Transactional
    public SupplierImportResponse importFromCsv(MultipartFile file, boolean updateExisting) {
        log.info("Bắt đầu import dữ liệu nhà cung cấp từ file: {}", file.getOriginalFilename());
        
        try {
            // Đọc và parse file CSV
            List<KiotVietSupplierImportDTO> importData = parseCsvFile(file);
            
            // Validate dữ liệu
            validateData(importData);
            
            // Kiểm tra duplicate
            checkDuplicates(importData);
            
            // Import vào database
            SupplierImportResponse.ImportSummary summary = importToDatabase(importData, updateExisting);
            
            // Tạo response
            if (summary.hasErrors()) {
                return SupplierImportResponse.warning(
                    "Import hoàn tất với một số cảnh báo",
                    summary,
                    importData,
                    null
                );
            } else {
                return SupplierImportResponse.success(summary, importData);
            }
            
        } catch (Exception e) {
            log.error("Lỗi trong quá trình import: ", e);
            return SupplierImportResponse.failure(
                "Lỗi: " + e.getMessage(),
                List.of(e.getMessage())
            );
        }
    }
    
    /**
     * Đọc và parse file CSV
     */
    private List<KiotVietSupplierImportDTO> parseCsvFile(MultipartFile file) throws IOException {
        List<KiotVietSupplierImportDTO> result = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            
            String line;
            int rowNumber = 0;
            String[] headers = null;
            
            while ((line = reader.readLine()) != null) {
                rowNumber++;
                
                // Bỏ qua dòng trống
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] values = parseCSVLine(line);
                
                // Dòng đầu tiên là header
                if (headers == null) {
                    headers = values;
                    continue;
                }
                
                // Parse dữ liệu
                KiotVietSupplierImportDTO dto = parseDataRow(values, headers, rowNumber);
                if (dto != null) {
                    result.add(dto);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Parse một dòng CSV (xử lý dấu ngoặc kép, dấu phẩy trong giá trị)
     */
    private String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        values.add(current.toString().trim());
        return values.toArray(String[]::new);
    }
    
    /**
     * Parse dữ liệu từ một dòng CSV
     */
    private KiotVietSupplierImportDTO parseDataRow(String[] values, String[] headers, int rowNumber) {
        try {
            KiotVietSupplierImportDTO dto = new KiotVietSupplierImportDTO();
            dto.setRowNumber(rowNumber);
            dto.setStatus(KiotVietSupplierImportDTO.ImportStatus.PENDING);
            
            // Mapping theo header (flexible mapping)
            for (int i = 0; i < headers.length && i < values.length; i++) {
                String header = headers[i].toLowerCase().trim();
                String value = values[i].trim();
                
                if (value.isEmpty()) {
                    continue;
                }
                
                // Mapping các trường dựa trên header
                if (header.contains("mã") || header.contains("code")) {
                    dto.setCode(value);
                } else if (header.contains("tên") || header.contains("name")) {
                    dto.setName(value);
                } else if (header.contains("điện thoại") || header.contains("phone")) {
                    dto.setPhone(value);
                } else if (header.contains("email")) {
                    dto.setEmail(value);
                } else if (header.contains("địa chỉ") || header.contains("address")) {
                    dto.setAddress(value);
                } else if (header.contains("thành phố") || header.contains("city")) {
                    dto.setCity(value);
                } else if (header.contains("quận") || header.contains("huyện") || header.contains("district")) {
                    dto.setDistrict(value);
                } else if (header.contains("phường") || header.contains("xã") || header.contains("ward")) {
                    dto.setWard(value);
                } else if (header.contains("vùng") || header.contains("region")) {
                    dto.setRegion(value);
                } else if (header.contains("nhóm") || header.contains("group")) {
                    dto.setSupplierGroup(value);
                } else if (header.contains("tổng tiền") || header.contains("total")) {
                    dto.setTotalPurchased(parseDecimal(value));
                } else if (header.contains("công nợ") || header.contains("debt")) {
                    dto.setCurrentDebt(parseDecimal(value));
                } else if (header.contains("ngày") || header.contains("date")) {
                    dto.setLastTransactionDate(parseDate(value));
                } else if (header.contains("ghi chú") || header.contains("note")) {
                    dto.setNotes(value);
                }
            }
            
            // Normalize và validate
            dto.normalize();
            dto.validate();
            
            return dto;
            
        } catch (Exception e) {
            log.error("Lỗi parse dòng {}: {}", rowNumber, e.getMessage());
            return null;
        }
    }
    
    /**
     * Parse số thập phân từ string (xử lý format tiền tệ VN)
     */
    private BigDecimal parseDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Loại bỏ dấu phẩy, dấu chấm phân cách nghìn, ký tự không phải số
            String cleanValue = value.replaceAll("[^0-9.-]", "");
            
            if (cleanValue.isEmpty()) {
                return null;
            }
            
            return new BigDecimal(cleanValue);
        } catch (Exception e) {
            log.warn("Không thể parse decimal: {}", value);
            return null;
        }
    }
    
    /**
     * Parse ngày tháng từ string
     */
    private LocalDate parseDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(value.trim(), formatter);
            } catch (Exception e) {
                // Thử formatter tiếp theo
            }
        }
        
        log.warn("Không thể parse date: {}", value);
        return null;
    }
    
    /**
     * Validate dữ liệu
     */
    private void validateData(List<KiotVietSupplierImportDTO> importData) {
        log.info("Validate {} dòng dữ liệu", importData.size());
        
        for (KiotVietSupplierImportDTO dto : importData) {
            dto.validate();
        }
        
        long validCount = importData.stream()
                .filter(KiotVietSupplierImportDTO::isValid)
                .count();
        
        log.info("Validation hoàn tất: {}/{} dòng hợp lệ", validCount, importData.size());
    }
    
    /**
     * Kiểm tra duplicate
     */
    private void checkDuplicates(List<KiotVietSupplierImportDTO> importData) {
        log.info("Kiểm tra duplicate data");
        
        for (KiotVietSupplierImportDTO dto : importData) {
            if (!dto.isValid()) {
                continue;
            }
            
            // Kiểm tra duplicate trong DB
            boolean isDuplicate = false;
            
            if (dto.getCode() != null && !dto.getCode().isEmpty()) {
                Optional<Supplier> existingByCode = supplierRepository.findByCode(dto.getCode());
                if (existingByCode.isPresent()) {
                    isDuplicate = true;
                }
            }
            
            if (!isDuplicate && dto.getPhone() != null && !dto.getPhone().isEmpty()) {
                Optional<Supplier> existingByPhone = supplierRepository.findByPhone(dto.getPhone());
                if (existingByPhone.isPresent()) {
                    isDuplicate = true;
                }
            }
            
            if (isDuplicate) {
                dto.setStatus(KiotVietSupplierImportDTO.ImportStatus.DUPLICATE);
                dto.setErrorMessage("Nhà cung cấp đã tồn tại trong hệ thống");
            }
        }
    }
    
    /**
     * Import vào database
     */
    private SupplierImportResponse.ImportSummary importToDatabase(List<KiotVietSupplierImportDTO> importData, 
                                                                 boolean updateExisting) {
        log.info("Bắt đầu import {} dòng vào database", importData.size());
        
        int successCount = 0;
        int errorCount = 0;
        int duplicateCount = 0;
        int validCount = 0;
        int invalidCount = 0;
        
        for (KiotVietSupplierImportDTO dto : importData) {
            try {
                if (!dto.isValid()) {
                    invalidCount++;
                    continue;
                }
                
                validCount++;
                
                if (dto.getStatus() == KiotVietSupplierImportDTO.ImportStatus.DUPLICATE) {
                    if (updateExisting) {
                        // Cập nhật dữ liệu existing
                        updateExistingSupplier(dto);
                        dto.setStatus(KiotVietSupplierImportDTO.ImportStatus.IMPORTED);
                        successCount++;
                    } else {
                        duplicateCount++;
                    }
                } else {
                    // Tạo mới
                    createNewSupplier(dto);
                    dto.setStatus(KiotVietSupplierImportDTO.ImportStatus.IMPORTED);
                    successCount++;
                }
                
            } catch (Exception e) {
                log.error("Lỗi import dòng {}: {}", dto.getRowNumber(), e.getMessage());
                dto.setStatus(KiotVietSupplierImportDTO.ImportStatus.FAILED);
                dto.setErrorMessage("Lỗi import: " + e.getMessage());
                errorCount++;
            }
        }
        
        log.info("Import hoàn tất: {} thành công, {} lỗi, {} duplicate", 
                successCount, errorCount, duplicateCount);
        
        // Tạo summary
        SupplierImportResponse.ImportSummary summary = new SupplierImportResponse.ImportSummary();
        summary.setTotalRows(importData.size());
        summary.setValidRows(validCount);
        summary.setInvalidRows(invalidCount);
        summary.setDuplicateRows(duplicateCount);
        summary.setImportedRows(successCount);
        summary.setFailedRows(errorCount);
        summary.setSkippedRows(duplicateCount + invalidCount);
        
        return summary;
    }
    
    /**
     * Tạo mới nhà cung cấp
     */
    private void createNewSupplier(KiotVietSupplierImportDTO dto) {
        Supplier supplier = new Supplier();
        mapDtoToEntity(dto, supplier);
        supplierRepository.save(supplier);
    }
    
    /**
     * Cập nhật nhà cung cấp existing
     */
    private void updateExistingSupplier(KiotVietSupplierImportDTO dto) {
        Optional<Supplier> existingOpt = Optional.empty();
        
        if (dto.getCode() != null && !dto.getCode().isEmpty()) {
            existingOpt = supplierRepository.findByCode(dto.getCode());
        }
        
        if (existingOpt.isEmpty() && dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            existingOpt = supplierRepository.findByPhone(dto.getPhone());
        }
        
        if (existingOpt.isPresent()) {
            Supplier existing = existingOpt.get();
            mapDtoToEntity(dto, existing);
            supplierRepository.save(existing);
        }
    }
    
    /**
     * Map DTO sang Entity
     */
    private void mapDtoToEntity(KiotVietSupplierImportDTO dto, Supplier supplier) {
        if (dto.getCode() != null && !dto.getCode().isEmpty()) {
            supplier.setCode(dto.getCode());
        }
        
        supplier.setName(dto.getName());
        supplier.setPhone(dto.getPhone());
        supplier.setEmail(dto.getEmail());
        supplier.setAddress(dto.getAddress());
        supplier.setCity(dto.getCity());
        supplier.setDistrict(dto.getDistrict());
        supplier.setWard(dto.getWard());
        supplier.setRegion(dto.getRegion());
        supplier.setSupplierGroup(dto.getSupplierGroup());
        supplier.setTotalPurchased(dto.getTotalPurchased());
        supplier.setCurrentDebt(dto.getCurrentDebt());
        supplier.setLastTransactionDate(dto.getLastTransactionDate());
        supplier.setNotes(dto.getNotes());
    }
}
