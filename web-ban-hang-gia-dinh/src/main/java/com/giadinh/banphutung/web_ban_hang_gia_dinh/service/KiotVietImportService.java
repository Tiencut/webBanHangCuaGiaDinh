package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.KiotVietSupplierDTO;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.SupplierRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service để xử lý import dữ liệu nhà cung cấp từ file CSV KiotViet
 */
@Service
public class KiotVietImportService {

    private static final Logger logger = LoggerFactory.getLogger(KiotVietImportService.class);

    @Autowired
    private SupplierRepository supplierRepository;

    /**
     * Import suppliers from KiotViet CSV file
     * @param file CSV file từ KiotViet
     * @return Map chứa thống kê import
     */
    @Transactional
    public Map<String, Object> importSuppliersFromKiotViet(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int totalRecords = 0;
        int successCount = 0;
        int errorCount = 0;
        List<String> errors = new ArrayList<>();
        
        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                totalRecords++;
                
                try {
                    // Tạo DTO từ dòng CSV
                    KiotVietSupplierDTO dto = parseCSVLine(nextLine);
                    
                    // Tìm supplier hiện tại theo code hoặc name
                    Supplier existingSupplier = findExistingSupplier(dto);
                    
                    if (existingSupplier != null) {
                        // Cập nhật supplier hiện tại
                        updateSupplierFromDTO(existingSupplier, dto);
                        supplierRepository.save(existingSupplier);
                        logger.info("Updated supplier: {}", dto.getName());
                    } else {
                        // Tạo supplier mới
                        Supplier newSupplier = createSupplierFromDTO(dto);
                        supplierRepository.save(newSupplier);
                        logger.info("Created new supplier: {}", dto.getName());
                    }
                    
                    successCount++;
                    
                } catch (Exception e) {
                    errorCount++;
                    String errorMsg = String.format("Error processing row %d: %s", totalRecords, e.getMessage());
                    errors.add(errorMsg);
                    logger.error(errorMsg, e);
                }
            }
            
        } catch (Exception e) {
            String errorMsg = "Error reading CSV file: " + e.getMessage();
            errors.add(errorMsg);
            logger.error(errorMsg, e);
        }
        
        result.put("totalRecords", totalRecords);
        result.put("successCount", successCount);
        result.put("errorCount", errorCount);
        result.put("errors", errors);
        result.put("timestamp", LocalDateTime.now());
        
        return result;
    }

    /**
     * Parse một dòng CSV thành KiotVietSupplierDTO
     */
    private KiotVietSupplierDTO parseCSVLine(String[] csvLine) {
        KiotVietSupplierDTO dto = new KiotVietSupplierDTO();
        
        if (csvLine.length >= 1) dto.setCode(csvLine[0]);
        if (csvLine.length >= 2) dto.setName(csvLine[1]);
        if (csvLine.length >= 3) dto.setCompany(csvLine[2]);
        if (csvLine.length >= 4) dto.setAddress(csvLine[3]);
        if (csvLine.length >= 5) dto.setRegion(csvLine[4]);
        if (csvLine.length >= 6) dto.setWard(csvLine[5]);
        if (csvLine.length >= 7) dto.setPhone(csvLine[6]);
        if (csvLine.length >= 8) dto.setEmail(csvLine[7]);
        if (csvLine.length >= 9) dto.setSupplierGroup(csvLine[8]);
        if (csvLine.length >= 10) dto.setTotalPurchased(csvLine[9]);
        if (csvLine.length >= 11) dto.setCurrentDebt(csvLine[10]);
        if (csvLine.length >= 12) dto.setIsActive(csvLine[11]);
        if (csvLine.length >= 13) dto.setCreatedBy(csvLine[12]);
        
        return dto;
    }

    /**
     * Tìm supplier hiện tại theo code hoặc name
     */
    private Supplier findExistingSupplier(KiotVietSupplierDTO dto) {
        // Ưu tiên tìm theo code
        if (dto.getCode() != null && !dto.getCode().trim().isEmpty()) {
            Optional<Supplier> byCode = supplierRepository.findByCode(dto.getCode().trim());
            if (byCode.isPresent()) {
                return byCode.get();
            }
        }
        
        // Nếu không tìm thấy theo code, tìm theo name
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            Optional<Supplier> byName = supplierRepository.findByName(dto.getName().trim());
            if (byName.isPresent()) {
                return byName.get();
            }
        }
        
        return null;
    }

    /**
     * Cập nhật supplier hiện tại từ DTO
     */
    private void updateSupplierFromDTO(Supplier supplier, KiotVietSupplierDTO dto) {
        // Cập nhật các trường cơ bản
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            supplier.setName(dto.getName().trim());
        }
        if (dto.getCompany() != null && !dto.getCompany().trim().isEmpty()) {
            supplier.setCompany(dto.getCompany().trim());
        }
        if (dto.getAddress() != null && !dto.getAddress().trim().isEmpty()) {
            supplier.setAddress(dto.getAddress().trim());
        }
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
            supplier.setPhone(dto.getPhone().trim());
        }
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            supplier.setEmail(dto.getEmail().trim());
        }
        
        // Cập nhật các trường mới từ KiotViet
        if (dto.getRegion() != null && !dto.getRegion().trim().isEmpty()) {
            supplier.setRegion(dto.getRegion().trim());
        }
        if (dto.getWard() != null && !dto.getWard().trim().isEmpty()) {
            supplier.setWard(dto.getWard().trim());
        }
        if (dto.getSupplierGroup() != null && !dto.getSupplierGroup().trim().isEmpty()) {
            supplier.setSupplierGroup(dto.getSupplierGroup().trim());
        }
        
        // Cập nhật số liệu tài chính
        supplier.setTotalPurchased(dto.getParsedTotalPurchased());
        supplier.setCurrentDebt(dto.getParsedCurrentDebt());
        
        // Cập nhật trạng thái
        supplier.setIsActive(dto.getParsedIsActive());
        
        supplier.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Tạo supplier mới từ DTO
     */
    private Supplier createSupplierFromDTO(KiotVietSupplierDTO dto) {
        Supplier supplier = new Supplier();
        
        // Các trường bắt buộc
        supplier.setCode(dto.getCode() != null ? dto.getCode().trim() : "");
        supplier.setName(dto.getName() != null ? dto.getName().trim() : "");
        
        // Các trường tùy chọn
        supplier.setCompany(dto.getCompany() != null ? dto.getCompany().trim() : "");
        supplier.setAddress(dto.getAddress() != null ? dto.getAddress().trim() : "");
        supplier.setPhone(dto.getPhone() != null ? dto.getPhone().trim() : "");
        supplier.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : "");
        
        // Các trường mới từ KiotViet
        supplier.setRegion(dto.getRegion() != null ? dto.getRegion().trim() : "");
        supplier.setWard(dto.getWard() != null ? dto.getWard().trim() : "");
        supplier.setSupplierGroup(dto.getSupplierGroup() != null ? dto.getSupplierGroup().trim() : "");
        
        // Số liệu tài chính
        supplier.setTotalPurchased(dto.getParsedTotalPurchased());
        supplier.setCurrentDebt(dto.getParsedCurrentDebt());
        
        // Trạng thái
        supplier.setIsActive(dto.getParsedIsActive());
        
        // Timestamps
        LocalDateTime now = LocalDateTime.now();
        supplier.setCreatedAt(now);
        supplier.setUpdatedAt(now);
        
        return supplier;
    }

    /**
     * Validate dữ liệu trước khi import
     */
    public Map<String, Object> validateKiotVietFile(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        int totalRecords = 0;
        
        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                totalRecords++;
                
                // Validate basic fields
                if (nextLine.length < 2) {
                    errors.add("Row " + totalRecords + ": Missing required fields (code, name)");
                    continue;
                }
                
                String code = nextLine[0];
                String name = nextLine[1];
                
                if (code == null || code.trim().isEmpty()) {
                    warnings.add("Row " + totalRecords + ": Empty supplier code");
                }
                
                if (name == null || name.trim().isEmpty()) {
                    errors.add("Row " + totalRecords + ": Empty supplier name");
                }
                
                // Validate email format if present
                if (nextLine.length >= 8 && nextLine[7] != null && !nextLine[7].trim().isEmpty()) {
                    String email = nextLine[7].trim();
                    if (!email.contains("@")) {
                        warnings.add("Row " + totalRecords + ": Invalid email format: " + email);
                    }
                }
            }
            
        } catch (Exception e) {
            errors.add("Error reading file: " + e.getMessage());
        }
        
        result.put("totalRecords", totalRecords);
        result.put("errors", errors);
        result.put("warnings", warnings);
        result.put("isValid", errors.isEmpty());
        
        return result;
    }
}
