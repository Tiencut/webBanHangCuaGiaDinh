package com.giadinh.banphutung.web_ban_hang_gia_dinh.demo;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.*;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * DataInitializer - Tạo dữ liệu mẫu cho demo
 * 
 * Component này sẽ tự động chạy khi ứng dụng start và tạo:
 * - Các mẫu xe phổ biến (Thaco Ollin, Hyundai HD...)
 * - Danh mục sản phẩm (động cơ, hộp số, phanh...)
 * - Sản phẩm mẫu với thông tin tương thích
 * - Khách hàng và nhà cung cấp mẫu
 * 
 * Dữ liệu này giúp test API và demo tính năng gợi ý sản phẩm theo xe.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private VehicleModelRepository vehicleModelRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Bắt đầu khởi tạo dữ liệu demo...");
        
        // Tạo vehicle models nếu chưa có
        createVehicleModels();
        
        // Tạo categories
        createCategories();
        
        // Tạo suppliers
        createSuppliers();
        
        // Tạo customers
        createCustomers();
        
        // Tạo products với compatibility
        createProducts();
        
        System.out.println("✅ Khởi tạo dữ liệu demo hoàn tất!");
        System.out.println("🌐 Ứng dụng đang chạy tại: http://localhost:8080");
        System.out.println("📋 Swagger UI: http://localhost:8080/swagger-ui/index.html");
    }
    
    private void createVehicleModels() {
        if (vehicleModelRepository.count() > 0) {
            System.out.println("⏭️ Vehicle models đã có, bỏ qua...");
            return;
        }
        
        System.out.println("🚛 Tạo vehicle models...");
        
        // Thaco Ollin series
        VehicleModel ollin350 = new VehicleModel();
        ollin350.setCode("OLLIN350");
        ollin350.setName("Thaco Ollin 350");
        ollin350.setBrand("Thaco");
        ollin350.setModel("Ollin");
        ollin350.setVariant("350");
        ollin350.setVehicleType(VehicleModel.VehicleType.LIGHT_TRUCK);
        ollin350.setYearFrom(2018);
        ollin350.setYearTo(2021);
        ollin350.setEngineType("Isuzu 4JB1");
        ollin350.setEngineDisplacement(2771);
        ollin350.setEnginePower(98);
        ollin350.setTransmission("5 cấp số");
        ollin350.setPayloadCapacity(3500);
        ollin350.setTireSize("7.00-16");
        ollin350.setAlternativeNames("Ollin 350, Ollin3.5T, OL350");
        vehicleModelRepository.save(ollin350);
        
        VehicleModel ollin500 = new VehicleModel();
        ollin500.setCode("OLLIN500");
        ollin500.setName("Thaco Ollin 500");
        ollin500.setBrand("Thaco");
        ollin500.setModel("Ollin");
        ollin500.setVariant("500");
        ollin500.setVehicleType(VehicleModel.VehicleType.LIGHT_TRUCK);
        ollin500.setYearFrom(2019);
        ollin500.setEngineType("Isuzu 4JJ1");
        ollin500.setEngineDisplacement(2999);
        ollin500.setEnginePower(130);
        ollin500.setTransmission("6 cấp số");
        ollin500.setPayloadCapacity(5000);
        ollin500.setTireSize("8.25-16");
        ollin500.setAlternativeNames("Ollin 500, Ollin5T, OL500");
        vehicleModelRepository.save(ollin500);
        
        // Hyundai HD series
        VehicleModel hd65 = new VehicleModel();
        hd65.setCode("HD65");
        hd65.setName("Hyundai HD65");
        hd65.setBrand("Hyundai");
        hd65.setModel("HD65");
        hd65.setVehicleType(VehicleModel.VehicleType.LIGHT_TRUCK);
        hd65.setYearFrom(2015);
        hd65.setEngineType("D4BB");
        hd65.setEngineDisplacement(2607);
        hd65.setEnginePower(97);
        hd65.setTransmission("5 cấp số");
        hd65.setPayloadCapacity(3200);
        hd65.setTireSize("7.00-16");
        vehicleModelRepository.save(hd65);
        
        VehicleModel hd72 = new VehicleModel();
        hd72.setCode("HD72");
        hd72.setName("Hyundai HD72");
        hd72.setBrand("Hyundai");
        hd72.setModel("HD72");
        hd72.setVehicleType(VehicleModel.VehicleType.LIGHT_TRUCK);
        hd72.setYearFrom(2015);
        hd72.setEngineType("D4AL");
        hd72.setEngineDisplacement(3298);
        hd72.setEnginePower(120);
        hd72.setTransmission("6 cấp số");
        hd72.setPayloadCapacity(3500);
        hd72.setTireSize("7.50-16");
        vehicleModelRepository.save(hd72);
        
        System.out.println("✅ Đã tạo " + vehicleModelRepository.count() + " vehicle models");
    }
    
    private void createCategories() {
        if (categoryRepository.count() > 0) {
            System.out.println("⏭️ Categories đã có, bỏ qua...");
            return;
        }
        
        System.out.println("📂 Tạo categories...");
        
        Category dongCo = new Category();
        dongCo.setCode("ENGINE");
        dongCo.setName("Động cơ");
        dongCo.setDescription("Các phụ tùng động cơ");
        dongCo.setLevel(0);
        categoryRepository.save(dongCo);
        
        Category hopSo = new Category();
        hopSo.setCode("TRANSMISSION");
        hopSo.setName("Hộp số");
        hopSo.setDescription("Các phụ tùng hộp số");
        hopSo.setLevel(0);
        categoryRepository.save(hopSo);
        
        Category phanh = new Category();
        phanh.setCode("BRAKE");
        phanh.setName("Hệ thống phanh");
        phanh.setDescription("Các phụ tùng phanh");
        phanh.setLevel(0);
        categoryRepository.save(phanh);
        
        System.out.println("✅ Đã tạo " + categoryRepository.count() + " categories");
    }
    
    private void createSuppliers() {
        if (supplierRepository.count() > 0) {
            System.out.println("⏭️ Suppliers đã có, bỏ qua...");
            return;
        }
        
        System.out.println("🏭 Tạo suppliers...");
        
        Supplier isuzu = new Supplier();
        isuzu.setCode("ISUZU");
        isuzu.setName("Isuzu Việt Nam");
        isuzu.setAddress("Quận 7, TP.HCM");
        isuzu.setPhone("028-123-4567");
        isuzu.setEmail("contact@isuzu.vn");
        isuzu.setVehicleBrands("Isuzu, Thaco");
        isuzu.setRating(4.5);
        supplierRepository.save(isuzu);
        
        Supplier hyundai = new Supplier();
        hyundai.setCode("HYUNDAI");
        hyundai.setName("Hyundai Thành Công");
        hyundai.setAddress("Ninh Bình");
        hyundai.setPhone("0229-876-5432");
        hyundai.setEmail("info@hyundai-tc.vn");
        hyundai.setVehicleBrands("Hyundai");
        hyundai.setRating(4.3);
        supplierRepository.save(hyundai);
        
        System.out.println("✅ Đã tạo " + supplierRepository.count() + " suppliers");
    }
    
    private void createCustomers() {
        if (customerRepository.count() > 0) {
            System.out.println("⏭️ Customers đã có, bỏ qua...");
            return;
        }
        
        System.out.println("👥 Tạo customers...");
        
        Customer customer1 = new Customer();
        customer1.setCode("KH001");
        customer1.setName("Công ty TNHH Vận tải ABC");
        customer1.setPhone("0909-123-456");
        customer1.setEmail("abc@transport.com");
        customer1.setAddress("Quận 1, TP.HCM");
        customer1.setCustomerType(Customer.CustomerType.WHOLESALE);
        customer1.setPreferredVehicleBrands("Thaco, Hyundai");
        customer1.setCreditLimit(new BigDecimal("50000000"));
        customerRepository.save(customer1);
        
        Customer customer2 = new Customer();
        customer2.setCode("KH002");
        customer2.setName("Garage Minh Tuấn");
        customer2.setPhone("0918-765-432");
        customer2.setAddress("Quận Gò Vấp, TP.HCM");
        customer2.setCustomerType(Customer.CustomerType.GARAGE);
        customer2.setPreferredVehicleBrands("Isuzu, Hino");
        customerRepository.save(customer2);
        
        System.out.println("✅ Đã tạo " + customerRepository.count() + " customers");
    }
    
    private void createProducts() {
        if (productRepository.count() > 0) {
            System.out.println("⏭️ Products đã có, bỏ qua...");
            return;
        }
        
        System.out.println("🔧 Tạo products...");
        
        // Lấy categories
        Category engineCat = categoryRepository.findByCode("ENGINE").orElse(null);
        Category transmissionCat = categoryRepository.findByCode("TRANSMISSION").orElse(null);
        
        // Lấy vehicle models
        VehicleModel ollin350 = vehicleModelRepository.findByCode("OLLIN350").orElse(null);
        VehicleModel ollin500 = vehicleModelRepository.findByCode("OLLIN500").orElse(null);
        VehicleModel hd65 = vehicleModelRepository.findByCode("HD65").orElse(null);
        
        // Lấy supplier
        Supplier isuzu = supplierRepository.findByCode("ISUZU").orElse(null);
        
        // Sản phẩm 1: Piston Isuzu 4JB1 (cho Ollin 350)
        Product piston4jb1 = new Product();
        piston4jb1.setSku("PISTON-4JB1-STD");
        piston4jb1.setName("Piston Isuzu 4JB1 Standard");
        piston4jb1.setDescription("Piston tiêu chuẩn cho động cơ Isuzu 4JB1, dùng cho xe Thaco Ollin 350");
        piston4jb1.setCategory(engineCat);
        piston4jb1.setSupplier(isuzu);
        piston4jb1.setPrice(new BigDecimal("450000"));
        piston4jb1.setWholesalePrice(new BigDecimal("400000"));
        piston4jb1.setStockQuantity(50);
        piston4jb1.setMinStockLevel(10);
        piston4jb1.setOemPartNumber("8-97176-851-0");
        piston4jb1.setCompatibleVehicles(Arrays.asList(ollin350));
        productRepository.save(piston4jb1);
        
        // Sản phẩm 2: Hộp số cho Ollin 500
        Product gearbox500 = new Product();
        gearbox500.setSku("GEARBOX-OLLIN500");
        gearbox500.setName("Hộp số 6 cấp Thaco Ollin 500");
        gearbox500.setDescription("Hộp số 6 cấp cho xe Thaco Ollin 500, động cơ Isuzu 4JJ1");
        gearbox500.setCategory(transmissionCat);
        gearbox500.setPrice(new BigDecimal("15000000"));
        gearbox500.setWholesalePrice(new BigDecimal("13500000"));
        gearbox500.setStockQuantity(5);
        gearbox500.setMinStockLevel(2);
        gearbox500.setCompatibleVehicles(Arrays.asList(ollin500));
        productRepository.save(gearbox500);
        
        // Sản phẩm 3: Má phanh chung cho nhiều xe
        Product brakePad = new Product();
        brakePad.setSku("BRAKE-PAD-UNIVERSAL");
        brakePad.setName("Má phanh trước Universal");
        brakePad.setDescription("Má phanh trước phù hợp với nhiều dòng xe tải nhẹ");
        brakePad.setPrice(new BigDecimal("280000"));
        brakePad.setWholesalePrice(new BigDecimal("250000"));
        brakePad.setStockQuantity(100);
        brakePad.setMinStockLevel(20);
        brakePad.setCompatibleVehicles(Arrays.asList(ollin350, ollin500, hd65));
        productRepository.save(brakePad);
        
        System.out.println("✅ Đã tạo " + productRepository.count() + " products");
    }
}
