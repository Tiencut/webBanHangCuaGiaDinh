package com.giadinh.banphutung.web_ban_hang_gia_dinh.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Category;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CategoryRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Value("${app.init-data:true}")
    private boolean shouldInit;

    @Override
    public void run(String... args) throws Exception {
        if (!shouldInit) {
            log.info("Data seeding is disabled (app.init-data=false).");
            return;
        }
        try {
            long productCount = 0;
            try {
                productCount = productRepository.count();
            } catch (Exception e) {
                log.warn("Could not check product count, table might not exist yet: {}", e.getMessage());
                return;
            }

            if (productCount > 0) {
                log.info("Products already exist — skipping data loader.");
                return;
            }

            log.info("Seeding mock truck spare parts into H2 database...");

            // Create categories
            Category brakes = new Category();
            brakes.setName("Hệ thống phanh");
            brakes.setCode("BRK");
            categoryRepository.save(brakes);

            Category drivetrain = new Category();
            drivetrain.setName("Truyền động & Hộp số");
            drivetrain.setCode("DRT");
            categoryRepository.save(drivetrain);

            Category filters = new Category();
            filters.setName("Lọc & Dầu nhớt");
            filters.setCode("FLT");
            categoryRepository.save(filters);

            Category tires = new Category();
            tires.setName("Bánh xe & Lốp");
            tires.setCode("TIR");
            categoryRepository.save(tires);

            // Create products (thông tin tham khảo từ loại phụ tùng xe tải phổ biến)
            List<Product> samples = new ArrayList<>();

            Product p1 = new Product();
            p1.setName("Má phanh đĩa trước Hino 700");
            p1.setCode("BRK-H700-001");
            p1.setBrand("Hino");
            p1.setVehicleType("Hino");
            p1.setPartNumber("HB-700-DFP");
            p1.setOemNumber("H700-BRK-01");
            p1.setBasePrice(new BigDecimal("1250000"));
            p1.setSellingPrice(new BigDecimal("1500000"));
            p1.setCategory(brakes);
            samples.add(p1);

            Product p2 = new Product();
            p2.setName("Tấm bố phanh Hino/Isuzu (bộ)");
            p2.setCode("BRK-TRK-002");
            p2.setBrand("Isuzu");
            p2.setVehicleType("Isuzu");
            p2.setPartNumber("ISZ-BRK-SET");
            p2.setOemNumber("ISZ-BRK-SET-01");
            p2.setBasePrice(new BigDecimal("850000"));
            p2.setSellingPrice(new BigDecimal("980000"));
            p2.setCategory(brakes);
            samples.add(p2);

            Product p3 = new Product();
            p3.setName("Hộp số sàn 6 cấp (suitable for Hyundai HD)");
            p3.setCode("DRT-HY-006");
            p3.setBrand("Hyundai");
            p3.setVehicleType("Hyundai");
            p3.setPartNumber("HD-6SPD");
            p3.setOemNumber("HD-6-BOX-01");
            p3.setBasePrice(new BigDecimal("9500000"));
            p3.setSellingPrice(new BigDecimal("11200000"));
            p3.setCategory(drivetrain);
            samples.add(p3);

            Product p4 = new Product();
            p4.setName("Lọc dầu động cơ Isuzu 4HF1");
            p4.setCode("FLT-ISZ-4HF1");
            p4.setBrand("Isuzu");
            p4.setVehicleType("Isuzu");
            p4.setPartNumber("ISZ-OIL-4HF1");
            p4.setOemNumber("ISZ-4HF1-FLT");
            p4.setBasePrice(new BigDecimal("120000"));
            p4.setSellingPrice(new BigDecimal("150000"));
            p4.setCategory(filters);
            samples.add(p4);

            Product p5 = new Product();
            p5.setName("Lọc gió động cơ Hino");
            p5.setCode("FLT-HNO-AIR1");
            p5.setBrand("Hino");
            p5.setVehicleType("Hino");
            p5.setPartNumber("HNO-AIR-1");
            p5.setOemNumber("HNO-AIR-01");
            p5.setBasePrice(new BigDecimal("220000"));
            p5.setSellingPrice(new BigDecimal("280000"));
            p5.setCategory(filters);
            samples.add(p5);

            Product p6 = new Product();
            p6.setName("Vành thép 17.5\"");
            p6.setCode("TIR-RIM-17.5");
            p6.setBrand("Generic");
            p6.setVehicleType("Truck");
            p6.setPartNumber("RIM-175-STD");
            p6.setOemNumber("RIM-175-01");
            p6.setBasePrice(new BigDecimal("1750000"));
            p6.setSellingPrice(new BigDecimal("1950000"));
            p6.setCategory(tires);
            samples.add(p6);

            Product p7 = new Product();
            p7.setName("Lốp tải 10.00R20 (All-Season)");
            p7.setCode("TIR-TYRE-1000R20");
            p7.setBrand("Michelin");
            p7.setVehicleType("Truck");
            p7.setPartNumber("MIC-1000R20");
            p7.setOemNumber("MIC-T-1000R20");
            p7.setBasePrice(new BigDecimal("4200000"));
            p7.setSellingPrice(new BigDecimal("4850000"));
            p7.setCategory(tires);
            samples.add(p7);

            Product p8 = new Product();
            p8.setName("Bugi đốt nóng động cơ diesel (set 6)");
            p8.setCode("GLOW-PLUG-SET6");
            p8.setBrand("Bosch");
            p8.setVehicleType("Truck");
            p8.setPartNumber("BOS-GLOW-6");
            p8.setOemNumber("BOS-G-6-01");
            p8.setBasePrice(new BigDecimal("650000"));
            p8.setSellingPrice(new BigDecimal("750000"));
            p8.setCategory(filters);
            samples.add(p8);

            productRepository.saveAll(samples);

            log.info("Seeded {} mock products", samples.size());
        } catch (Exception e) {
            log.error("Error while seeding mock data: {}", e.getMessage(), e);
        }
    }
}
