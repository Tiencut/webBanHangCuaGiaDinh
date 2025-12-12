package com.giadinh.banphutung.web_ban_hang_gia_dinh.config;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Category;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CategoryRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.VehicleModelRepository;

/**
 * DataInitializer - Khởi tạo dữ liệu mẫu
 * 
 * Class này sẽ tạo dữ liệu mẫu khi ứng dụng khởi động lần đầu.
 * Bao gồm:
 * - Mẫu xe phổ biến (Thaco Ollin, Hyundai HD, Hino, Isuzu)
 * - Phụ tùng cơ bản (hộp số, má phanh, lọc dầu, v.v.)
 * - Quan hệ tương thích giữa xe và phụ tùng
 * 
 * Dữ liệu này giúp demo tính năng gợi ý thông minh.
 */
@Configuration
public class DataInitializer {

    @Autowired
    private VehicleModelRepository vehicleModelRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "init-data", havingValue = "true", matchIfMissing = true)
    @Transactional
    CommandLineRunner initDatabase() {
        return args -> {
            // Chỉ init nếu chưa có dữ liệu
            if (vehicleModelRepository.count() > 0) {
                return;
            }
            
            System.out.println("🚀 Initializing sample data...");
            
            // 1. Tạo categories
            initCategories();
            
            // 2. Tạo vehicle models
            initVehicleModels();
            
            // 3. Tạo products
            initProducts();
            
            // 4. Tạo quan hệ tương thích
            initCompatibility();
            
            System.out.println("✅ Sample data initialized successfully!");
        };
    }
    
    private void initCategories() {
        // Hộp số và ly hợp
        Category transmission = new Category();
        transmission.setName("Hộp số và ly hợp");
        transmission.setCode("TRANSMISSION");
        transmission.setDescription("Hộp số tay, hộp số tự động, bộ ly hợp");
        transmission.setIsActive(true);
        transmission.setLevel(0);
        categoryRepository.save(transmission);
        
        // Hệ thống phanh
        Category brake = new Category();
        brake.setName("Hệ thống phanh");
        brake.setCode("BRAKE");
        brake.setDescription("Má phanh, đĩa phanh, dầu phanh");
        brake.setIsActive(true);
        brake.setLevel(0);
        categoryRepository.save(brake);
        
        // Động cơ
        Category engine = new Category();
        engine.setName("Động cơ");
        engine.setCode("ENGINE");
        engine.setDescription("Phụ tùng động cơ, lọc dầu, lọc gió");
        engine.setIsActive(true);
        engine.setLevel(0);
        categoryRepository.save(engine);
        
        System.out.println("📁 Created categories");
    }
    
    private void initVehicleModels() {
        // Thaco Ollin 500
        VehicleModel ollin500 = new VehicleModel();
        ollin500.setCode("OLLIN500");
        ollin500.setName("Thaco Ollin 500");
        ollin500.setBrand("THACO");
        ollin500.setYearFrom(2018);
        ollin500.setYearTo(null); // Vẫn còn sản xuất
        ollin500.setVehicleType(VehicleModel.VehicleType.LIGHT_TRUCK);
        ollin500.setTonnage(2.5);
        ollin500.setEngineModel("Isuzu 4JB1");
        ollin500.setEngineDisplacement(2771);
        ollin500.setEnginePower(95);
        ollin500.setTransmission("5 cấp số tay");
        ollin500.setTireSize("7.00-16");
        ollin500.setOrigin("Lắp ráp trong nước");
        ollin500.setAlternativeNames("Ollin 500, OL500, Ollin5");
        ollin500.setProductionStatus(VehicleModel.ProductionStatus.IN_PRODUCTION);
        ollin500.setIsActive(true);
        vehicleModelRepository.save(ollin500);
        
        // Hyundai HD72
        VehicleModel hd72 = new VehicleModel();
        hd72.setCode("HD72");
        hd72.setName("Hyundai HD72");
        hd72.setBrand("HYUNDAI");
        hd72.setYearFrom(2015);
        hd72.setYearTo(2022);
        hd72.setVehicleType(VehicleModel.VehicleType.LIGHT_TRUCK);
        hd72.setTonnage(3.5);
        hd72.setEngineModel("Hyundai D4BB");
        hd72.setEngineDisplacement(2607);
        hd72.setEnginePower(98);
        hd72.setTransmission("5 cấp số tay");
        hd72.setTireSize("7.50-16");
        hd72.setOrigin("Lắp ráp trong nước");
        hd72.setAlternativeNames("HD 72, HD-72");
        hd72.setProductionStatus(VehicleModel.ProductionStatus.DISCONTINUED);
        hd72.setIsActive(true);
        vehicleModelRepository.save(hd72);
        
        // Hino 300
        VehicleModel hino300 = new VehicleModel();
        hino300.setCode("HINO300");
        hino300.setName("Hino 300");
        hino300.setBrand("HINO");
        hino300.setYearFrom(2020);
        hino300.setYearTo(null);
        hino300.setVehicleType(VehicleModel.VehicleType.MEDIUM_TRUCK);
        hino300.setTonnage(5.0);
        hino300.setEngineModel("Hino N04C");
        hino300.setEngineDisplacement(4009);
        hino300.setEnginePower(150);
        hino300.setTransmission("6 cấp số tay");
        hino300.setTireSize("8.25-20");
        hino300.setOrigin("Nhập khẩu nguyên chiếc");
        hino300.setAlternativeNames("Hino300, H300");
        hino300.setProductionStatus(VehicleModel.ProductionStatus.IN_PRODUCTION);
        hino300.setIsActive(true);
        vehicleModelRepository.save(hino300);
        
        System.out.println("🚛 Created vehicle models");
    }
    
    private void initProducts() {
        Category transmission = categoryRepository.findByCode("TRANSMISSION").orElse(null);
        Category brake = categoryRepository.findByCode("BRAKE").orElse(null);
        Category engine = categoryRepository.findByCode("ENGINE").orElse(null);
        
        // Hộp số 5 cấp Isuzu
        Product gearbox5 = new Product();
        gearbox5.setName("Hộp số 5 cấp Isuzu 4JB1");
        gearbox5.setCode("GB5_4JB1");
        gearbox5.setDescription("Hộp số 5 cấp dành cho động cơ Isuzu 4JB1");
        gearbox5.setCategory(transmission);
        gearbox5.setBasePrice(new BigDecimal("15000000"));
        gearbox5.setSellingPrice(new BigDecimal("18000000"));
        gearbox5.setBrand("ISUZU");
        gearbox5.setPartNumber("8-97363-814-0");
        gearbox5.setOemNumber("MSG-5E");
        gearbox5.setVehicleType("Thaco Ollin, Isuzu");
        gearbox5.setWarrantyPeriod(12);
        gearbox5.setStatus(Product.ProductStatus.ACTIVE);
        gearbox5.setTags(Arrays.asList("hộp số", "5 cấp", "isuzu", "4jb1", "ollin"));
        productRepository.save(gearbox5);
        
        // Má phanh Thaco Ollin
        Product brakePad = new Product();
        brakePad.setName("Má phanh trước Thaco Ollin 500");
        brakePad.setCode("BP_OLLIN500");
        brakePad.setDescription("Má phanh đĩa trước cho xe Thaco Ollin 500");
        brakePad.setCategory(brake);
        brakePad.setBasePrice(new BigDecimal("450000"));
        brakePad.setSellingPrice(new BigDecimal("550000"));
        brakePad.setBrand("GENUINE");
        brakePad.setPartNumber("58101-4A000");
        brakePad.setVehicleType("Thaco Ollin");
        brakePad.setWarrantyPeriod(6);
        brakePad.setStatus(Product.ProductStatus.ACTIVE);
        brakePad.setTags(Arrays.asList("má phanh", "phanh trước", "ollin", "thaco"));
        productRepository.save(brakePad);
        
        // Lọc dầu động cơ
        Product oilFilter = new Product();
        oilFilter.setName("Lọc dầu động cơ Isuzu 4JB1");
        oilFilter.setCode("OF_4JB1");
        oilFilter.setDescription("Lọc dầu động cơ cho Isuzu 4JB1");
        oilFilter.setCategory(engine);
        oilFilter.setBasePrice(new BigDecimal("120000"));
        oilFilter.setSellingPrice(new BigDecimal("150000"));
        oilFilter.setBrand("ISUZU");
        oilFilter.setPartNumber("8-94430-983-0");
        oilFilter.setOemNumber("JX0811");
        oilFilter.setVehicleType("Isuzu, Thaco");
        oilFilter.setWarrantyPeriod(3);
        oilFilter.setStatus(Product.ProductStatus.ACTIVE);
        oilFilter.setTags(Arrays.asList("lọc dầu", "động cơ", "isuzu", "4jb1"));
        productRepository.save(oilFilter);
        
        // Hộp số 6 cấp Hino
        Product gearbox6 = new Product();
        gearbox6.setName("Hộp số 6 cấp Hino N04C");
        gearbox6.setCode("GB6_N04C");
        gearbox6.setDescription("Hộp số 6 cấp dành cho động cơ Hino N04C");
        gearbox6.setCategory(transmission);
        gearbox6.setBasePrice(new BigDecimal("25000000"));
        gearbox6.setSellingPrice(new BigDecimal("30000000"));
        gearbox6.setBrand("HINO");
        gearbox6.setPartNumber("33010-E0800");
        gearbox6.setOemNumber("M5X6F");
        gearbox6.setVehicleType("Hino");
        gearbox6.setWarrantyPeriod(12);
        gearbox6.setStatus(Product.ProductStatus.ACTIVE);
        gearbox6.setTags(Arrays.asList("hộp số", "6 cấp", "hino", "n04c"));
        productRepository.save(gearbox6);
        
        System.out.println("🔧 Created products");
    }
    
    @Transactional
    private void initCompatibility() {
        // Lấy vehicles
        VehicleModel ollin500 = vehicleModelRepository.findByCode("OLLIN500").orElse(null);
        VehicleModel hd72 = vehicleModelRepository.findByCode("HD72").orElse(null);
        VehicleModel hino300 = vehicleModelRepository.findByCode("HINO300").orElse(null);
        
        // Lấy products với fetch join để tránh lazy loading
        Product gearbox5 = productRepository.findByCodeWithCompatibleVehicles("GB5_4JB1").orElse(null);
        Product brakePad = productRepository.findByCodeWithCompatibleVehicles("BP_OLLIN500").orElse(null);
        Product oilFilter = productRepository.findByCodeWithCompatibleVehicles("OF_4JB1").orElse(null);
        Product gearbox6 = productRepository.findByCodeWithCompatibleVehicles("GB6_N04C").orElse(null);
        
        if (ollin500 != null && gearbox5 != null) {
            // Hộp số 5 cấp tương thích với Ollin 500
            gearbox5.getCompatibleVehicles().add(ollin500);
            productRepository.save(gearbox5);
            
            // Má phanh tương thích với Ollin 500
            if (brakePad != null) {
                brakePad.getCompatibleVehicles().add(ollin500);
                productRepository.save(brakePad);
            }
            
            // Lọc dầu tương thích với Ollin 500 (cùng động cơ 4JB1)
            if (oilFilter != null) {
                oilFilter.getCompatibleVehicles().add(ollin500);
                productRepository.save(oilFilter);
            }
        }
        
        if (hd72 != null && gearbox5 != null && oilFilter != null) {
            // HD72 cũng có thể dùng hộp số 5 cấp (tương tự)
            gearbox5.getCompatibleVehicles().add(hd72);
            productRepository.save(gearbox5);
            
            // HD72 có động cơ khác nhưng vẫn có thể dùng lọc dầu tương tự
            oilFilter.getCompatibleVehicles().add(hd72);
            productRepository.save(oilFilter);
        }
        
        if (hino300 != null && gearbox6 != null) {
            // Hộp số 6 cấp tương thích với Hino 300
            gearbox6.getCompatibleVehicles().add(hino300);
            productRepository.save(gearbox6);
        }
        
        System.out.println("🔗 Created compatibility relationships");
    }
}
