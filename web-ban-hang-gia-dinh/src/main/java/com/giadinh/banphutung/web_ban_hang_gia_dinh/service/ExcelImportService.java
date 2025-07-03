package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.*;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * ExcelImportService - Xử lý import dữ liệu từ file Excel
 * 
 * Hỗ trợ import:
 * - Danh sách xe (Vehicle Models)
 * - Sản phẩm (Products) 
 * - Khách hàng (Customers)
 * - Nhà cung cấp (Suppliers)
 * - Nội dung đào tạo (Training Content)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelImportService {

    private final VehicleModelRepository vehicleModelRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;
    private final TrainingContentRepository trainingContentRepository;

    /**
     * Import dữ liệu từ file Excel tổng hợp
     */
    @Transactional
    public ImportResult importFromExcel(MultipartFile file) throws IOException {
        log.info("🚀 Bắt đầu import từ file: {}", file.getOriginalFilename());
        
        ImportResult result = new ImportResult();
        
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            
            // Import theo thứ tự phụ thuộc
            importCategories(workbook, result);
            importSuppliers(workbook, result);
            importVehicleModels(workbook, result);
            importProducts(workbook, result);
            importCustomers(workbook, result);
            importTrainingContent(workbook, result);
            
            log.info("✅ Import hoàn tất: {}", result.getSummary());
            
        } catch (Exception e) {
            log.error("❌ Lỗi import: {}", e.getMessage(), e);
            result.addError("Lỗi xử lý file: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * Import danh mục sản phẩm
     */
    private void importCategories(Workbook workbook, ImportResult result) {
        Sheet sheet = workbook.getSheet("Categories");
        if (sheet == null) {
            result.addWarning("Không tìm thấy sheet 'Categories'");
            return;
        }

        log.info("📂 Import Categories...");
        int count = 0;

        // Bỏ qua header row (row 0)
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;

            try {
                String code = getCellValue(row, 0);
                String name = getCellValue(row, 1);
                String description = getCellValue(row, 2);
                String parentCode = getCellValue(row, 3);

                if (code == null || name == null) {
                    result.addError("Categories row " + (i+1) + ": Thiếu code hoặc name");
                    continue;
                }

                // Kiểm tra đã tồn tại chưa
                if (categoryRepository.findByCode(code).isPresent()) {
                    result.addWarning("Categories row " + (i+1) + ": Code '" + code + "' đã tồn tại");
                    continue;
                }

                Category category = new Category();
                category.setCode(code);
                category.setName(name);
                category.setDescription(description);

                // Set parent category nếu có
                if (parentCode != null && !parentCode.trim().isEmpty()) {
                    categoryRepository.findByCode(parentCode).ifPresent(category::setParent);
                }

                categoryRepository.save(category);
                count++;

            } catch (Exception e) {
                result.addError("Categories row " + (i+1) + ": " + e.getMessage());
            }
        }

        result.addSuccess("✅ Import " + count + " categories");
    }

    /**
     * Import nhà cung cấp
     */
    private void importSuppliers(Workbook workbook, ImportResult result) {
        Sheet sheet = workbook.getSheet("Suppliers");
        if (sheet == null) {
            result.addWarning("Không tìm thấy sheet 'Suppliers'");
            return;
        }

        log.info("🏭 Import Suppliers...");
        int count = 0;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;

            try {
                String code = getCellValue(row, 0);
                String name = getCellValue(row, 1);
                String address = getCellValue(row, 2);
                String phone = getCellValue(row, 3);
                String email = getCellValue(row, 4);
                String contactPerson = getCellValue(row, 5);
                String taxCode = getCellValue(row, 6);

                if (code == null || name == null) {
                    result.addError("Suppliers row " + (i+1) + ": Thiếu code hoặc name");
                    continue;
                }

                if (supplierRepository.findByCode(code).isPresent()) {
                    result.addWarning("Suppliers row " + (i+1) + ": Code '" + code + "' đã tồn tại");
                    continue;
                }

                Supplier supplier = new Supplier();
                supplier.setCode(code);
                supplier.setName(name);
                supplier.setAddress(address);
                supplier.setPhone(phone);
                supplier.setEmail(email);
                supplier.setContactPerson(contactPerson);
                supplier.setTaxCode(taxCode);

                supplierRepository.save(supplier);
                count++;

            } catch (Exception e) {
                result.addError("Suppliers row " + (i+1) + ": " + e.getMessage());
            }
        }

        result.addSuccess("✅ Import " + count + " suppliers");
    }

    /**
     * Import mẫu xe
     */
    private void importVehicleModels(Workbook workbook, ImportResult result) {
        Sheet sheet = workbook.getSheet("VehicleModels");
        if (sheet == null) {
            result.addWarning("Không tìm thấy sheet 'VehicleModels'");
            return;
        }

        log.info("🚛 Import Vehicle Models...");
        int count = 0;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;

            try {
                String code = getCellValue(row, 0);
                String name = getCellValue(row, 1);
                String brand = getCellValue(row, 2);
                String yearFromStr = getCellValue(row, 3);
                String yearToStr = getCellValue(row, 4);
                String tonnageStr = getCellValue(row, 5);
                String engineModel = getCellValue(row, 6);
                String transmission = getCellValue(row, 7);
                String vehicleTypeStr = getCellValue(row, 8);

                if (code == null || name == null || brand == null) {
                    result.addError("VehicleModels row " + (i+1) + ": Thiếu thông tin bắt buộc");
                    continue;
                }

                if (vehicleModelRepository.findByCode(code).isPresent()) {
                    result.addWarning("VehicleModels row " + (i+1) + ": Code '" + code + "' đã tồn tại");
                    continue;
                }

                VehicleModel vehicle = new VehicleModel();
                vehicle.setCode(code);
                vehicle.setName(name);
                vehicle.setBrand(brand);
                
                // Parse year
                if (yearFromStr != null) {
                    vehicle.setYearFrom(Integer.valueOf(yearFromStr));
                }
                if (yearToStr != null && !yearToStr.trim().isEmpty()) {
                    vehicle.setYearTo(Integer.valueOf(yearToStr));
                }
                
                // Parse tonnage
                if (tonnageStr != null) {
                    vehicle.setTonnage(Double.valueOf(tonnageStr));
                }
                
                vehicle.setEngineModel(engineModel);
                vehicle.setTransmission(transmission);
                
                // Parse vehicle type
                if (vehicleTypeStr != null) {
                    try {
                        vehicle.setVehicleType(VehicleModel.VehicleType.valueOf(vehicleTypeStr.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        result.addWarning("VehicleModels row " + (i+1) + ": VehicleType không hợp lệ: " + vehicleTypeStr);
                    }
                }

                vehicleModelRepository.save(vehicle);
                count++;

            } catch (Exception e) {
                result.addError("VehicleModels row " + (i+1) + ": " + e.getMessage());
            }
        }

        result.addSuccess("✅ Import " + count + " vehicle models");
    }

    /**
     * Import sản phẩm
     */
    private void importProducts(Workbook workbook, ImportResult result) {
        Sheet sheet = workbook.getSheet("Products");
        if (sheet == null) {
            result.addWarning("Không tìm thấy sheet 'Products'");
            return;
        }

        log.info("📦 Import Products...");
        int count = 0;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;

            try {
                String code = getCellValue(row, 0);
                String name = getCellValue(row, 1);
                String description = getCellValue(row, 2);
                String categoryCode = getCellValue(row, 3);
                String basePriceStr = getCellValue(row, 4);
                String sellingPriceStr = getCellValue(row, 5);
                String brand = getCellValue(row, 6);
                String partNumber = getCellValue(row, 7);
                String oemNumber = getCellValue(row, 8);
                String compatibleVehicles = getCellValue(row, 9); // Danh sách code xe cách nhau bởi dấu phẩy

                if (name == null) {
                    result.addError("Products row " + (i+1) + ": Thiếu tên sản phẩm");
                    continue;
                }

                Product product = new Product();
                product.setCode(code);
                product.setName(name);
                product.setDescription(description);
                product.setBrand(brand);
                product.setPartNumber(partNumber);
                product.setOemNumber(oemNumber);

                // Set category
                if (categoryCode != null) {
                    categoryRepository.findByCode(categoryCode).ifPresent(product::setCategory);
                }

                // Parse prices
                if (basePriceStr != null) {
                    product.setBasePrice(new BigDecimal(basePriceStr));
                }
                if (sellingPriceStr != null) {
                    product.setSellingPrice(new BigDecimal(sellingPriceStr));
                }

                // Set compatible vehicles
                if (compatibleVehicles != null && !compatibleVehicles.trim().isEmpty()) {
                    String[] vehicleCodes = compatibleVehicles.split(",");
                    List<VehicleModel> vehicles = new ArrayList<>();
                    for (String vehicleCode : vehicleCodes) {
                        vehicleModelRepository.findByCode(vehicleCode.trim()).ifPresent(vehicles::add);
                    }
                    product.setCompatibleVehicles(vehicles);
                }

                productRepository.save(product);
                count++;

            } catch (Exception e) {
                result.addError("Products row " + (i+1) + ": " + e.getMessage());
            }
        }

        result.addSuccess("✅ Import " + count + " products");
    }

    /**
     * Import khách hàng
     */
    private void importCustomers(Workbook workbook, ImportResult result) {
        Sheet sheet = workbook.getSheet("Customers");
        if (sheet == null) {
            result.addWarning("Không tìm thấy sheet 'Customers'");
            return;
        }

        log.info("👥 Import Customers...");
        int count = 0;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;

            try {
                String code = getCellValue(row, 0);
                String name = getCellValue(row, 1);
                String phone = getCellValue(row, 2);
                String email = getCellValue(row, 3);
                String address = getCellValue(row, 4);
                String taxCode = getCellValue(row, 5);
                String customerTypeStr = getCellValue(row, 6);

                if (name == null) {
                    result.addError("Customers row " + (i+1) + ": Thiếu tên khách hàng");
                    continue;
                }

                Customer customer = new Customer();
                customer.setCode(code);
                customer.setName(name);
                customer.setPhone(phone);
                customer.setEmail(email);
                customer.setAddress(address);
                customer.setTaxCode(taxCode);

                // Parse customer type
                if (customerTypeStr != null) {
                    try {
                        customer.setCustomerType(Customer.CustomerType.valueOf(customerTypeStr.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        result.addWarning("Customers row " + (i+1) + ": CustomerType không hợp lệ: " + customerTypeStr);
                    }
                }

                customerRepository.save(customer);
                count++;

            } catch (Exception e) {
                result.addError("Customers row " + (i+1) + ": " + e.getMessage());
            }
        }

        result.addSuccess("✅ Import " + count + " customers");
    }

    /**
     * Import nội dung đào tạo
     */
    private void importTrainingContent(Workbook workbook, ImportResult result) {
        Sheet sheet = workbook.getSheet("TrainingContent");
        if (sheet == null) {
            result.addWarning("Không tìm thấy sheet 'TrainingContent'");
            return;
        }

        log.info("📚 Import Training Content...");
        int count = 0;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;

            try {
                String title = getCellValue(row, 0);
                String categoryStr = getCellValue(row, 1);
                String summary = getCellValue(row, 2);
                String content = getCellValue(row, 3);
                String priorityStr = getCellValue(row, 4);
                String readTimeStr = getCellValue(row, 5);
                String tags = getCellValue(row, 6);
                String showInQuickHelpStr = getCellValue(row, 7);

                if (title == null) {
                    result.addError("TrainingContent row " + (i+1) + ": Thiếu tiêu đề");
                    continue;
                }

                TrainingContent trainingContent = new TrainingContent();
                trainingContent.setTitle(title);
                trainingContent.setSummary(summary);
                trainingContent.setContent(content);
                trainingContent.setTags(tags);

                // Parse category
                if (categoryStr != null) {
                    try {
                        trainingContent.setCategory(TrainingContent.TrainingCategory.valueOf(categoryStr.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        result.addWarning("TrainingContent row " + (i+1) + ": Category không hợp lệ: " + categoryStr);
                    }
                }

                // Parse priority
                if (priorityStr != null) {
                    trainingContent.setPriority(Integer.valueOf(priorityStr));
                }

                // Parse read time
                if (readTimeStr != null) {
                    trainingContent.setEstimatedReadTime(Integer.valueOf(readTimeStr));
                }

                // Parse show in quick help
                if (showInQuickHelpStr != null) {
                    trainingContent.setShowInQuickHelp(Boolean.valueOf(showInQuickHelpStr));
                }

                trainingContentRepository.save(trainingContent);
                count++;

            } catch (Exception e) {
                result.addError("TrainingContent row " + (i+1) + ": " + e.getMessage());
            }
        }

        result.addSuccess("✅ Import " + count + " training content");
    }

    /**
     * Utility methods
     */
    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                // Convert number to string
                double numValue = cell.getNumericCellValue();
                if (numValue == (long) numValue) {
                    return String.valueOf((long) numValue);
                } else {
                    return String.valueOf(numValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * DTO kết quả import
     */
    public static class ImportResult {
        private final List<String> successes = new ArrayList<>();
        private final List<String> warnings = new ArrayList<>();
        private final List<String> errors = new ArrayList<>();
        private final LocalDateTime importTime = LocalDateTime.now();

        public void addSuccess(String message) {
            successes.add(message);
        }

        public void addWarning(String message) {
            warnings.add(message);
        }

        public void addError(String message) {
            errors.add(message);
        }

        public String getSummary() {
            return String.format("Success: %d, Warnings: %d, Errors: %d", 
                successes.size(), warnings.size(), errors.size());
        }

        // Getters
        public List<String> getSuccesses() { return successes; }
        public List<String> getWarnings() { return warnings; }
        public List<String> getErrors() { return errors; }
        public LocalDateTime getImportTime() { return importTime; }
        public boolean hasErrors() { return !errors.isEmpty(); }
        public boolean isSuccess() { return !hasErrors(); }
    }
}
