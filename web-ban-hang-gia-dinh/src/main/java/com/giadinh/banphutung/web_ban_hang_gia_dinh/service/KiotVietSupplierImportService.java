package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.KiotVietSupplierImportDTO;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.SupplierImportResponse;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service xử lý import dữ liệu nhà cung cấp từ file CSV KiotViet
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KiotVietSupplierImportService {
    
    private final SupplierRepository supplierRepository;
    private final SupplierService supplierService;
    
    // Mapping tên cột CSV KiotViet với field
    private static final Map<String, String> CSV_COLUMN_MAPPING = Map.of(
        "Mã nhà cung cấp", "code",
        "Tên nhà cung cấp", "name",
        "Số điện thoại", "phone",
        "Email", "email",
        "Địa chỉ", "address",
        "Thành phố", "city",
        "Quận/Huyện", "district",
        "Phường/Xã", "ward",
        "Vùng miền", "region",
        "Nhóm nhà cung cấp", "supplierGroup",
        "Tổng tiền mua", "totalPurchased",
        "Công nợ hiện tại", "currentDebt",
        "Ngày giao dịch cuối", "lastTransactionDate",
        "Ghi chú", "notes"
    );
    
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy")
    };
    
    /**
     * Parse file CSV và trả về danh sách DTO
     */
    public List<KiotVietSupplierImportDTO> parseCSVFile(MultipartFile file) throws IOException {
        log.info("Parsing CSV file: {}", file.getOriginalFilename());
        
        List<KiotVietSupplierImportDTO> suppliers = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            
            String line;
            int rowNumber = 0;
            String[] headers = null;
            Map<String, Integer> columnIndexMap = new HashMap<>();
            
            while ((line = reader.readLine()) != null) {
                rowNumber++;
                
                // Bỏ qua dòng trống
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] columns = parseCSVLine(line);
                
                // Dòng đầu tiên là header
                if (headers == null) {
                    headers = columns;
                    columnIndexMap = buildColumnIndexMap(headers);
                    log.info("Found {} columns in CSV", headers.length);
                    continue;
                }
                
                // Parse dữ liệu
                try {
                    KiotVietSupplierImportDTO supplier = parseSupplierFromCSVRow(columns, columnIndexMap, rowNumber);
                    suppliers.add(supplier);
                } catch (Exception e) {
                    log.error("Error parsing row {}: {}", rowNumber, e.getMessage());
                    KiotVietSupplierImportDTO errorSupplier = new KiotVietSupplierImportDTO();
                    errorSupplier.setRowNumber(rowNumber);
                    errorSupplier.setStatus(KiotVietSupplierImportDTO.ImportStatus.INVALID);
                    errorSupplier.setErrorMessage("Lỗi parse dữ liệu: " + e.getMessage());
                    errorSupplier.setValid(false);
                    suppliers.add(errorSupplier);
                }
            }
        }
        
        log.info("Parsed {} suppliers from CSV", suppliers.size());
        return suppliers;
    }
    
    /**
     * Validate và chuẩn hóa dữ liệu
     */
    public List<KiotVietSupplierImportDTO> validateAndNormalize(List<KiotVietSupplierImportDTO> suppliers) {
        log.info("Validating and normalizing {} suppliers", suppliers.size());
        
        // Chuẩn hóa dữ liệu
        suppliers.forEach(KiotVietSupplierImportDTO::normalize);
        
        // Validate từng supplier
        suppliers.forEach(KiotVietSupplierImportDTO::validate);
        
        // Kiểm tra trùng lặp trong file
        checkDuplicatesInFile(suppliers);
        
        // Kiểm tra trùng lặp với database
        checkDuplicatesWithDatabase(suppliers);
        
        return suppliers;
    }
    
    /**
     * Import dữ liệu vào database
     */
    public SupplierImportResponse importSuppliers(List<KiotVietSupplierImportDTO> suppliers) {
        log.info("Importing {} suppliers to database", suppliers.size());
        
        SupplierImportResponse.ImportSummary summary = new SupplierImportResponse.ImportSummary();
        summary.setTotalRows(suppliers.size());
        
        List<String> errors = new ArrayList<>();
        List<KiotVietSupplierImportDTO> importedSuppliers = new ArrayList<>();
        
        // Chỉ import những supplier hợp lệ
        List<KiotVietSupplierImportDTO> validSuppliers = suppliers.stream()
            .filter(s -> s.getStatus() == KiotVietSupplierImportDTO.ImportStatus.VALID)
            .collect(Collectors.toList());
        
        summary.setValidRows(validSuppliers.size());
        summary.setInvalidRows((int) suppliers.stream()
            .filter(s -> s.getStatus() == KiotVietSupplierImportDTO.ImportStatus.INVALID)
            .count());
        summary.setDuplicateRows((int) suppliers.stream()
            .filter(s -> s.getStatus() == KiotVietSupplierImportDTO.ImportStatus.DUPLICATE)
            .count());
        
        // Batch import
        int batchSize = 50;
        for (int i = 0; i < validSuppliers.size(); i += batchSize) {
            int end = Math.min(i + batchSize, validSuppliers.size());
            List<KiotVietSupplierImportDTO> batch = validSuppliers.subList(i, end);
            
            try {
                importBatch(batch, importedSuppliers, errors);
            } catch (Exception e) {
                log.error("Error importing batch {}-{}: {}", i, end, e.getMessage());
                batch.forEach(s -> {
                    s.setStatus(KiotVietSupplierImportDTO.ImportStatus.FAILED);
                    s.setErrorMessage("Lỗi import: " + e.getMessage());
                });
                errors.add("Lỗi import batch " + (i/batchSize + 1) + ": " + e.getMessage());
            }
        }
        
        summary.setImportedRows(importedSuppliers.size());
        summary.setFailedRows(validSuppliers.size() - importedSuppliers.size());
        summary.setSkippedRows(summary.getInvalidRows() + summary.getDuplicateRows());
        
        // Tạo response
        if (summary.getImportedRows() > 0) {
            if (summary.hasErrors()) {
                return SupplierImportResponse.warning(
                    "Import hoàn tất với một số lỗi", 
                    summary, 
                    importedSuppliers, 
                    errors
                );
            } else {
                return SupplierImportResponse.success(summary, importedSuppliers);
            }
        } else {
            return SupplierImportResponse.failure("Không có dữ liệu nào được import", errors);
        }
    }
    
    /**
     * Import một batch suppliers
     */
    private void importBatch(List<KiotVietSupplierImportDTO> batch, 
                           List<KiotVietSupplierImportDTO> importedSuppliers, 
                           List<String> errors) {
        for (KiotVietSupplierImportDTO dto : batch) {
            try {
                Supplier supplier = convertToSupplier(dto);
                Supplier savedSupplier = supplierService.createSupplier(supplier);
                
                dto.setStatus(KiotVietSupplierImportDTO.ImportStatus.IMPORTED);
                importedSuppliers.add(dto);
                
                log.debug("Imported supplier: {} (ID: {})", savedSupplier.getName(), savedSupplier.getId());
                
            } catch (Exception e) {
                log.error("Error importing supplier {}: {}", dto.getName(), e.getMessage());
                dto.setStatus(KiotVietSupplierImportDTO.ImportStatus.FAILED);
                dto.setErrorMessage("Lỗi import: " + e.getMessage());
                errors.add("Dòng " + dto.getRowNumber() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Chuyển DTO thành entity
     */
    private Supplier convertToSupplier(KiotVietSupplierImportDTO dto) {
        Supplier supplier = new Supplier();
        
        supplier.setCode(dto.getCode());
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
        supplier.setNotes(dto.getNotes());
        
        // Set default values
        supplier.setStatus(Supplier.SupplierStatus.ACTIVE);
        supplier.setRating(5.0); // Default rating
        supplier.setDeliveryTimeDays(7); // Default delivery time
        
        return supplier;
    }
    
    /**
     * Parse một dòng CSV
     */
    private String[] parseCSVLine(String line) {
        // Simple CSV parser - có thể cần cải tiến cho các trường hợp phức tạp
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString().trim());
        
        return result.toArray(new String[0]);
    }
    
    /**
     * Tạo map index của các cột
     */
    private Map<String, Integer> buildColumnIndexMap(String[] headers) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i].trim();
            map.put(header, i);
        }
        return map;
    }
    
    /**
     * Parse supplier từ dòng CSV
     */
    private KiotVietSupplierImportDTO parseSupplierFromCSVRow(String[] columns, 
                                                             Map<String, Integer> columnIndexMap, 
                                                             int rowNumber) {
        KiotVietSupplierImportDTO supplier = new KiotVietSupplierImportDTO();
        supplier.setRowNumber(rowNumber);
        supplier.setStatus(KiotVietSupplierImportDTO.ImportStatus.PENDING);
        
        // Parse từng field
        supplier.setCode(getColumnValue(columns, columnIndexMap, "Mã nhà cung cấp"));
        supplier.setName(getColumnValue(columns, columnIndexMap, "Tên nhà cung cấp"));
        supplier.setPhone(getColumnValue(columns, columnIndexMap, "Số điện thoại"));
        supplier.setEmail(getColumnValue(columns, columnIndexMap, "Email"));
        supplier.setAddress(getColumnValue(columns, columnIndexMap, "Địa chỉ"));
        supplier.setCity(getColumnValue(columns, columnIndexMap, "Thành phố"));
        supplier.setDistrict(getColumnValue(columns, columnIndexMap, "Quận/Huyện"));
        supplier.setWard(getColumnValue(columns, columnIndexMap, "Phường/Xã"));
        supplier.setRegion(getColumnValue(columns, columnIndexMap, "Vùng miền"));
        supplier.setSupplierGroup(getColumnValue(columns, columnIndexMap, "Nhóm nhà cung cấp"));
        supplier.setNotes(getColumnValue(columns, columnIndexMap, "Ghi chú"));
        
        // Parse số tiền
        supplier.setTotalPurchased(parseMoneyValue(getColumnValue(columns, columnIndexMap, "Tổng tiền mua")));
        supplier.setCurrentDebt(parseMoneyValue(getColumnValue(columns, columnIndexMap, "Công nợ hiện tại")));
        
        // Parse ngày
        supplier.setLastTransactionDate(parseDateValue(getColumnValue(columns, columnIndexMap, "Ngày giao dịch cuối")));
        
        return supplier;
    }
    
    /**
     * Lấy giá trị cột
     */
    private String getColumnValue(String[] columns, Map<String, Integer> columnIndexMap, String columnName) {
        Integer index = columnIndexMap.get(columnName);
        if (index == null || index >= columns.length) {
            return null;
        }
        String value = columns[index];
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }
    
    /**
     * Parse giá trị tiền tệ
     */
    private BigDecimal parseMoneyValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Loại bỏ ký tự không phải số
            String cleanValue = value.replaceAll("[^0-9.]", "");
            return new BigDecimal(cleanValue);
        } catch (NumberFormatException e) {
            log.warn("Cannot parse money value: {}", value);
            return null;
        }
    }
    
    /**
     * Parse giá trị ngày
     */
    private LocalDate parseDateValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(value.trim(), formatter);
            } catch (DateTimeParseException e) {
                // Thử formatter tiếp theo
            }
        }
        
        log.warn("Cannot parse date value: {}", value);
        return null;
    }
    
    /**
     * Kiểm tra trùng lặp trong file
     */
    private void checkDuplicatesInFile(List<KiotVietSupplierImportDTO> suppliers) {
        Set<String> seenCodes = new HashSet<>();
        Set<String> seenPhones = new HashSet<>();
        Set<String> seenEmails = new HashSet<>();
        
        for (KiotVietSupplierImportDTO supplier : suppliers) {
            if (supplier.getStatus() != KiotVietSupplierImportDTO.ImportStatus.VALID) {
                continue;
            }
            
            boolean isDuplicate = false;
            StringBuilder duplicateMessage = new StringBuilder();
            
            // Kiểm tra code
            if (supplier.getCode() != null && !supplier.getCode().isEmpty()) {
                if (seenCodes.contains(supplier.getCode())) {
                    isDuplicate = true;
                    duplicateMessage.append("Mã nhà cung cấp trùng lặp trong file; ");
                } else {
                    seenCodes.add(supplier.getCode());
                }
            }
            
            // Kiểm tra phone
            if (supplier.getPhone() != null && !supplier.getPhone().isEmpty()) {
                if (seenPhones.contains(supplier.getPhone())) {
                    isDuplicate = true;
                    duplicateMessage.append("Số điện thoại trùng lặp trong file; ");
                } else {
                    seenPhones.add(supplier.getPhone());
                }
            }
            
            // Kiểm tra email
            if (supplier.getEmail() != null && !supplier.getEmail().isEmpty()) {
                if (seenEmails.contains(supplier.getEmail())) {
                    isDuplicate = true;
                    duplicateMessage.append("Email trùng lặp trong file; ");
                } else {
                    seenEmails.add(supplier.getEmail());
                }
            }
            
            if (isDuplicate) {
                supplier.setStatus(KiotVietSupplierImportDTO.ImportStatus.DUPLICATE);
                supplier.setErrorMessage(duplicateMessage.toString());
                supplier.setValid(false);
            }
        }
    }
    
    /**
     * Kiểm tra trùng lặp với database
     */
    private void checkDuplicatesWithDatabase(List<KiotVietSupplierImportDTO> suppliers) {
        for (KiotVietSupplierImportDTO supplier : suppliers) {
            if (supplier.getStatus() != KiotVietSupplierImportDTO.ImportStatus.VALID) {
                continue;
            }
            
            boolean isDuplicate = false;
            StringBuilder duplicateMessage = new StringBuilder();
            
            // Kiểm tra code
            if (supplier.getCode() != null && supplierRepository.findByCode(supplier.getCode()).isPresent()) {
                isDuplicate = true;
                duplicateMessage.append("Mã nhà cung cấp đã tồn tại trong hệ thống; ");
            }
            
            // Kiểm tra phone
            if (supplier.getPhone() != null && supplierRepository.findByPhone(supplier.getPhone()).isPresent()) {
                isDuplicate = true;
                duplicateMessage.append("Số điện thoại đã tồn tại trong hệ thống; ");
            }
            
            // Kiểm tra email
            if (supplier.getEmail() != null && supplierRepository.findByEmail(supplier.getEmail()).isPresent()) {
                isDuplicate = true;
                duplicateMessage.append("Email đã tồn tại trong hệ thống; ");
            }
            
            if (isDuplicate) {
                supplier.setStatus(KiotVietSupplierImportDTO.ImportStatus.DUPLICATE);
                supplier.setErrorMessage(duplicateMessage.toString());
                supplier.setValid(false);
            }
        }
    }
}
