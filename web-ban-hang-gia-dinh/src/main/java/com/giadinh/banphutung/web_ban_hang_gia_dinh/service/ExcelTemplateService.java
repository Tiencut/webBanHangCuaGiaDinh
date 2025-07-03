package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * ExcelTemplateService - Tạo file template Excel mẫu cho import
 */
@Service
@Slf4j
public class ExcelTemplateService {

    /**
     * Tạo file template Excel với các sheet mẫu
     */
    public ByteArrayResource generateTemplate() throws IOException {
        log.info("🎨 Tạo template Excel...");
        
        try (Workbook workbook = new XSSFWorkbook()) {
            
            // Tạo styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle sampleStyle = createSampleStyle(workbook);
            
            // Tạo các sheet
            createCategoriesSheet(workbook, headerStyle, sampleStyle);
            createSuppliersSheet(workbook, headerStyle, sampleStyle);
            createVehicleModelsSheet(workbook, headerStyle, sampleStyle);
            createProductsSheet(workbook, headerStyle, sampleStyle);
            createCustomersSheet(workbook, headerStyle, sampleStyle);
            createTrainingContentSheet(workbook, headerStyle, sampleStyle);
            
            // Convert to ByteArrayResource
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            
            log.info("✅ Template Excel đã được tạo thành công");
            return new ByteArrayResource(outputStream.toByteArray());
            
        } catch (IOException e) {
            log.error("❌ Lỗi tạo template Excel: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Tạo sheet Categories
     */
    private void createCategoriesSheet(Workbook workbook, CellStyle headerStyle, CellStyle sampleStyle) {
        Sheet sheet = workbook.createSheet("Categories");
        
        // Header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"name", "description", "parentCategory"};
        createHeaderRow(headerRow, headers, headerStyle);
        
        // Sample data
        Row sampleRow = sheet.createRow(1);
        sampleRow.createCell(0).setCellValue("Phụ tùng động cơ");
        sampleRow.createCell(1).setCellValue("Các phụ tùng liên quan đến động cơ xe tải");
        sampleRow.createCell(2).setCellValue(""); // No parent
        applySampleStyle(sampleRow, sampleStyle);
        
        Row sampleRow2 = sheet.createRow(2);
        sampleRow2.createCell(0).setCellValue("Piston");
        sampleRow2.createCell(1).setCellValue("Piston động cơ các loại");
        sampleRow2.createCell(2).setCellValue("Phụ tùng động cơ");
        applySampleStyle(sampleRow2, sampleStyle);
        
        autoSizeColumns(sheet, headers.length);
    }
    
    /**
     * Tạo sheet Suppliers
     */
    private void createSuppliersSheet(Workbook workbook, CellStyle headerStyle, CellStyle sampleStyle) {
        Sheet sheet = workbook.createSheet("Suppliers");
        
        // Header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"name", "contactInfo", "address", "email", "phone"};
        createHeaderRow(headerRow, headers, headerStyle);
        
        // Sample data
        Row sampleRow = sheet.createRow(1);
        sampleRow.createCell(0).setCellValue("Công ty TNHH Phụ Tùng ABC");
        sampleRow.createCell(1).setCellValue("Mr. Nguyễn Văn A - 0901234567");
        sampleRow.createCell(2).setCellValue("123 Đường Lê Lợi, Q1, TP.HCM");
        sampleRow.createCell(3).setCellValue("contact@phutungabc.com");
        sampleRow.createCell(4).setCellValue("0901234567");
        applySampleStyle(sampleRow, sampleStyle);
        
        autoSizeColumns(sheet, headers.length);
    }
    
    /**
     * Tạo sheet VehicleModels
     */
    private void createVehicleModelsSheet(Workbook workbook, CellStyle headerStyle, CellStyle sampleStyle) {
        Sheet sheet = workbook.createSheet("VehicleModels");
        
        // Header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"brand", "model", "year", "engineType", "description"};
        createHeaderRow(headerRow, headers, headerStyle);
        
        // Sample data
        Row sampleRow = sheet.createRow(1);
        sampleRow.createCell(0).setCellValue("Hyundai");
        sampleRow.createCell(1).setCellValue("HD120SL");
        sampleRow.createCell(2).setCellValue("2020");
        sampleRow.createCell(3).setCellValue("D4DB");
        sampleRow.createCell(4).setCellValue("Xe tải 8 tấn Hyundai HD120SL");
        applySampleStyle(sampleRow, sampleStyle);
        
        Row sampleRow2 = sheet.createRow(2);
        sampleRow2.createCell(0).setCellValue("Isuzu");
        sampleRow2.createCell(1).setCellValue("NPR85K");
        sampleRow2.createCell(2).setCellValue("2021");
        sampleRow2.createCell(3).setCellValue("4JJ1");
        sampleRow2.createCell(4).setCellValue("Xe tải 3.5 tấn Isuzu NPR85K");
        applySampleStyle(sampleRow2, sampleStyle);
        
        autoSizeColumns(sheet, headers.length);
    }
    
    /**
     * Tạo sheet Products
     */
    private void createProductsSheet(Workbook workbook, CellStyle headerStyle, CellStyle sampleStyle) {
        Sheet sheet = workbook.createSheet("Products");
        
        // Header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"name", "sku", "price", "category", "description", "compatibleVehicles", "supplier"};
        createHeaderRow(headerRow, headers, headerStyle);
        
        // Sample data
        Row sampleRow = sheet.createRow(1);
        sampleRow.createCell(0).setCellValue("Piston Hyundai HD120SL");
        sampleRow.createCell(1).setCellValue("PST-HD120-001");
        sampleRow.createCell(2).setCellValue("1500000");
        sampleRow.createCell(3).setCellValue("Piston");
        sampleRow.createCell(4).setCellValue("Piston động cơ D4DB cho xe Hyundai HD120SL");
        sampleRow.createCell(5).setCellValue("Hyundai HD120SL");
        sampleRow.createCell(6).setCellValue("Công ty TNHH Phụ Tùng ABC");
        applySampleStyle(sampleRow, sampleStyle);
        
        autoSizeColumns(sheet, headers.length);
    }
    
    /**
     * Tạo sheet Customers
     */
    private void createCustomersSheet(Workbook workbook, CellStyle headerStyle, CellStyle sampleStyle) {
        Sheet sheet = workbook.createSheet("Customers");
        
        // Header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"name", "phone", "email", "address", "customerType", "taxCode"};
        createHeaderRow(headerRow, headers, headerStyle);
        
        // Sample data
        Row sampleRow = sheet.createRow(1);
        sampleRow.createCell(0).setCellValue("Công ty Vận Tải XYZ");
        sampleRow.createCell(1).setCellValue("0912345678");
        sampleRow.createCell(2).setCellValue("info@vantaixyz.com");
        sampleRow.createCell(3).setCellValue("456 Đường Nguyễn Trãi, Q5, TP.HCM");
        sampleRow.createCell(4).setCellValue("BUSINESS");
        sampleRow.createCell(5).setCellValue("0123456789");
        applySampleStyle(sampleRow, sampleStyle);
        
        Row sampleRow2 = sheet.createRow(2);
        sampleRow2.createCell(0).setCellValue("Anh Nguyễn Văn B");
        sampleRow2.createCell(1).setCellValue("0987654321");
        sampleRow2.createCell(2).setCellValue("nguyenvanb@gmail.com");
        sampleRow2.createCell(3).setCellValue("789 Đường Lê Văn Việt, Q9, TP.HCM");
        sampleRow2.createCell(4).setCellValue("INDIVIDUAL");
        sampleRow2.createCell(5).setCellValue("");
        applySampleStyle(sampleRow2, sampleStyle);
        
        autoSizeColumns(sheet, headers.length);
    }
    
    /**
     * Tạo sheet TrainingContent
     */
    private void createTrainingContentSheet(Workbook workbook, CellStyle headerStyle, CellStyle sampleStyle) {
        Sheet sheet = workbook.createSheet("TrainingContent");
        
        // Header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"title", "content", "type", "tags", "difficulty", "productSku"};
        createHeaderRow(headerRow, headers, headerStyle);
        
        // Sample data
        Row sampleRow = sheet.createRow(1);
        sampleRow.createCell(0).setCellValue("Cách nhận diện Piston Hyundai");
        sampleRow.createCell(1).setCellValue("Piston Hyundai HD120SL có đặc điểm: đường kính 104mm, có 4 rãnh piston, chất liệu hợp kim nhôm. Mã số in trên đỉnh piston: D4DB-104.");
        sampleRow.createCell(2).setCellValue("IDENTIFICATION");
        sampleRow.createCell(3).setCellValue("piston,hyundai,hd120sl,động cơ");
        sampleRow.createCell(4).setCellValue("BASIC");
        sampleRow.createCell(5).setCellValue("PST-HD120-001");
        applySampleStyle(sampleRow, sampleStyle);
        
        autoSizeColumns(sheet, headers.length);
    }
    
    /**
     * Tạo style cho header
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    /**
     * Tạo style cho dữ liệu mẫu
     */
    private CellStyle createSampleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    /**
     * Tạo header row
     */
    private void createHeaderRow(Row row, String[] headers, CellStyle style) {
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }
    
    /**
     * Áp dụng style cho sample row
     */
    private void applySampleStyle(Row row, CellStyle style) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                cell.setCellStyle(style);
            }
        }
    }
    
    /**
     * Auto-size columns
     */
    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            // Set minimum width
            int currentWidth = sheet.getColumnWidth(i);
            if (currentWidth < 3000) {
                sheet.setColumnWidth(i, 3000);
            }
        }
    }
}
