package com.giadinh.banphutung.web_ban_hang_gia_dinh.demo;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Category;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.TrainingContent;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CategoryRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CustomerRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.SupplierRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.VehicleModelRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.TrainingContentRepository;

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

    @Autowired
    private TrainingContentRepository trainingContentRepository;

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
        
        // Tạo nội dung đào tạo mẫu
        createTrainingContents();
        
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
        ollin350.setVehicleType(VehicleModel.VehicleType.LIGHT_TRUCK);
        ollin350.setYearFrom(2018);
        ollin350.setYearTo(2021);
        ollin350.setTonnage(3.5);
        ollin350.setEngineModel("Isuzu 4JB1");
        ollin350.setEngineDisplacement(2771);
        ollin350.setEnginePower(98);
        ollin350.setTransmission("5 cấp số");
        ollin350.setTireSize("7.00-16");
        ollin350.setAlternativeNames("Ollin 350, Ollin3.5T, OL350");
        vehicleModelRepository.save(ollin350);
        
        VehicleModel ollin500 = new VehicleModel();
        ollin500.setCode("OLLIN500");
        ollin500.setName("Thaco Ollin 500");
        ollin500.setBrand("Thaco");
        ollin500.setVehicleType(VehicleModel.VehicleType.LIGHT_TRUCK);
        ollin500.setYearFrom(2019);
        ollin500.setTonnage(5.0);
        ollin500.setEngineModel("Isuzu 4JJ1");
        ollin500.setEngineDisplacement(2999);
        ollin500.setEnginePower(130);
        ollin500.setTransmission("6 cấp số");
        ollin500.setTireSize("8.25-16");
        ollin500.setAlternativeNames("Ollin 500, Ollin5T, OL500");
        vehicleModelRepository.save(ollin500);
        
        // Hyundai HD series
        VehicleModel hd65 = new VehicleModel();
        hd65.setCode("HD65");
        hd65.setName("Hyundai HD65");
        hd65.setBrand("Hyundai");
        hd65.setVehicleType(VehicleModel.VehicleType.LIGHT_TRUCK);
        hd65.setYearFrom(2015);
        hd65.setTonnage(3.2);
        hd65.setEngineModel("D4BB");
        hd65.setEngineDisplacement(2607);
        hd65.setEnginePower(97);
        hd65.setTransmission("5 cấp số");
        hd65.setTireSize("7.00-16");
        vehicleModelRepository.save(hd65);
        
        VehicleModel hd72 = new VehicleModel();
        hd72.setCode("HD72");
        hd72.setName("Hyundai HD72");
        hd72.setBrand("Hyundai");
        hd72.setVehicleType(VehicleModel.VehicleType.LIGHT_TRUCK);
        hd72.setYearFrom(2015);
        hd72.setTonnage(3.5);
        hd72.setEngineModel("D4AL");
        hd72.setEngineDisplacement(3298);
        hd72.setEnginePower(120);
        hd72.setTransmission("6 cấp số");
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
        isuzu.setRating(4.5);
        supplierRepository.save(isuzu);
        
        Supplier hyundai = new Supplier();
        hyundai.setCode("HYUNDAI");
        hyundai.setName("Hyundai Thành Công");
        hyundai.setAddress("Ninh Bình");
        hyundai.setPhone("0229-876-5432");
        hyundai.setEmail("info@hyundai-tc.vn");
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
        piston4jb1.setCode("PISTON-4JB1-STD");
        piston4jb1.setName("Piston Isuzu 4JB1 Standard");
        piston4jb1.setDescription("Piston tiêu chuẩn cho động cơ Isuzu 4JB1, dùng cho xe Thaco Ollin 350");
        piston4jb1.setCategory(engineCat);
        piston4jb1.setBasePrice(new BigDecimal("450000"));
        piston4jb1.setSellingPrice(new BigDecimal("450000"));
        piston4jb1.setCostPrice(new BigDecimal("400000"));
        piston4jb1.setMinStockLevel(10);
        piston4jb1.setPartNumber("PISTON-4JB1-STD");
        piston4jb1.setOemNumber("8-97176-851-0");
        piston4jb1.setCompatibleVehicles(Arrays.asList(ollin350));
        productRepository.save(piston4jb1);
        
        // Sản phẩm 2: Hộp số cho Ollin 500
        Product gearbox500 = new Product();
        gearbox500.setCode("GEARBOX-OLLIN500");
        gearbox500.setName("Hộp số 6 cấp Thaco Ollin 500");
        gearbox500.setDescription("Hộp số 6 cấp cho xe Thaco Ollin 500, động cơ Isuzu 4JJ1");
        gearbox500.setCategory(transmissionCat);
        gearbox500.setBasePrice(new BigDecimal("15000000"));
        gearbox500.setSellingPrice(new BigDecimal("15000000"));
        gearbox500.setCostPrice(new BigDecimal("13500000"));
        gearbox500.setMinStockLevel(2);
        gearbox500.setCompatibleVehicles(Arrays.asList(ollin500));
        productRepository.save(gearbox500);
        
        // Sản phẩm 3: Má phanh chung cho nhiều xe
        Product brakePad = new Product();
        brakePad.setCode("BRAKE-PAD-UNIVERSAL");
        brakePad.setName("Má phanh trước Universal");
        brakePad.setDescription("Má phanh trước phù hợp với nhiều dòng xe tải nhẹ");
        brakePad.setBasePrice(new BigDecimal("280000"));
        brakePad.setSellingPrice(new BigDecimal("280000"));
        brakePad.setCostPrice(new BigDecimal("250000"));
        brakePad.setMinStockLevel(20);
        brakePad.setCompatibleVehicles(Arrays.asList(ollin350, ollin500, hd65));
        productRepository.save(brakePad);

        System.out.println("✅ Đã tạo dữ liệu mẫu thành công!");
        
        // Initialize Training Content
        initializeTrainingContent();
    }
    
    private void initializeTrainingContent() {
        if (trainingContentRepository.count() > 0) {
            System.out.println("⏭️ Training content đã có, bỏ qua...");
            return;
        }
        
        System.out.println("📚 Tạo nội dung đào tạo...");
        
        // Training content for product identification
        TrainingContent productIdContent = new TrainingContent();
        productIdContent.setTitle("Cách nhận diện má phanh Hyundai HD65");
        productIdContent.setCategory(TrainingContent.TrainingCategory.PRODUCT_IDENTIFICATION);
        productIdContent.setSummary("Hướng dẫn chi tiết cách phân biệt má phanh chính hãng và tương thích cho xe Hyundai HD65");
        productIdContent.setContent("""
            <h4>Má phanh Hyundai HD65 - Điểm nhận biết</h4>
            <ul>
                <li><strong>OEM Number:</strong> 58101-5H000 (trước), 58302-5H000 (sau)</li>
                <li><strong>Kích thước:</strong> Dày 12mm, rộng 140mm, dài 95mm</li>
                <li><strong>Logo:</strong> Có dập logo Hyundai hoặc Sangsin (nhà sản xuất OEM)</li>
                <li><strong>Màu sắc:</strong> Đen hoặc xám đậm, không bị ố vàng</li>
                <li><strong>Bao bì:</strong> Hộp màu xanh Hyundai chính hãng</li>
            </ul>
            <h5>⚠️ Lưu ý quan trọng:</h5>
            <p>- <strong>KHÔNG dùng cho HD72:</strong> HD72 có kích thước khác (dày 14mm)</p>
            <p>- <strong>Thay cả bộ:</strong> Luôn thay cả 4 miếng cùng lúc</p>
            <p>- <strong>Kiểm tra phanh dầu:</strong> Nên thay phanh dầu kèm theo</p>
            <p>- <strong>Bảo hành:</strong> 6 tháng hoặc 30,000km</p>
            <h5>💡 Tips bán hàng:</h5>
            <p>- Hỏi thêm về tình trạng đĩa phanh</p>
            <p>- Gợi ý thay cả bộ để đảm bảo an toàn</p>
            <p>- Có thể combo với phanh dầu DOT4</p>
            """);
        productIdContent.setPriority(1);
        productIdContent.setEstimatedReadTime(3);
        productIdContent.setShowInQuickHelp(true);
        productIdContent.setTags("hyundai,hd65,brake,pad,recognition,oem");
        trainingContentRepository.save(productIdContent);

        // Training content for vehicle knowledge
        TrainingContent vehicleKnowledge = new TrainingContent();
        vehicleKnowledge.setTitle("Phân biệt các dòng xe Hyundai HD Series");
        vehicleKnowledge.setCategory(TrainingContent.TrainingCategory.VEHICLE_KNOWLEDGE);
        vehicleKnowledge.setSummary("Cách phân biệt HD65, HD72, HD99 và các phụ tùng tương ứng");
        vehicleKnowledge.setContent("""
            <h4>Hyundai HD Series - Điểm khác biệt</h4>
            <table border="1" style="width:100%; border-collapse: collapse;">
                <tr>
                    <th>Mẫu xe</th>
                    <th>Năm SX</th>
                    <th>Tải trọng</th>
                    <th>Động cơ</th>
                    <th>Má phanh</th>
                </tr>
                <tr>
                    <td><strong>HD65</strong></td>
                    <td>2015-2020</td>
                    <td>3.5 tấn</td>
                    <td>D4BB 2.6L</td>
                    <td>58101-5H000</td>
                </tr>
                <tr>
                    <td><strong>HD72</strong></td>
                    <td>2021-nay</td>
                    <td>5.0 tấn</td>
                    <td>D4DB 3.9L</td>
                    <td>58101-6A000</td>
                </tr>
                <tr>
                    <td><strong>HD99</strong></td>
                    <td>2018-nay</td>
                    <td>6.5 tấn</td>
                    <td>D4GA 3.9L</td>
                    <td>58101-7B000</td>
                </tr>
            </table>
            <h5>🔍 Cách nhận biết nhanh:</h5>
            <ul>
                <li><strong>HD65:</strong> Xe nhỏ, cabin tròn, đèn pha nhỏ</li>
                <li><strong>HD72:</strong> Cabin vuông hơn, đèn pha to, có DRL LED</li>
                <li><strong>HD99:</strong> Xe to nhất, có đèn sương mù, la-zăng to</li>
            </ul>
            """);
        vehicleKnowledge.setPriority(1);
        vehicleKnowledge.setEstimatedReadTime(5);
        vehicleKnowledge.setShowInQuickHelp(true);
        vehicleKnowledge.setTags("hyundai,hd65,hd72,hd99,vehicle,identification");
        trainingContentRepository.save(vehicleKnowledge);

        // Training content for sales tips
        TrainingContent salesTips = new TrainingContent();
        salesTips.setTitle("Kỹ thuật tư vấn khách hàng khi mua phụ tùng");
        salesTips.setCategory(TrainingContent.TrainingCategory.SALES_TIPS);
        salesTips.setSummary("Các câu hỏi quan trọng cần hỏi khách và cách tư vấn hiệu quả");
        salesTips.setContent("""
            <h4>Quy trình tư vấn chuẩn (5 bước)</h4>
            <h5>1. 🔍 Thu thập thông tin:</h5>
            <ul>
                <li>"Anh cho em biết xe gì, năm sản xuất bao nhiêu ạ?"</li>
                <li>"Xe đang có vấn đề gì ạ?" (triệu chứng)</li>
                <li>"Lần trước thay khi nào ạ?" (lịch sử bảo dưỡng)</li>
                <li>"Xe chạy bao nhiêu km/ngày ạ?" (cường độ sử dụng)</li>
            </ul>
            <h5>2. 🎯 Xác định nhu cầu:</h5>
            <ul>
                <li>Phụ tùng chính hãng hay tương thích?</li>
                <li>Ưu tiên giá rẻ hay chất lượng?</li>
                <li>Cần gấp hay có thể đợi order?</li>
            </ul>
            <h5>3. 💡 Đưa ra gợi ý:</h5>
            <ul>
                <li>Giải thích sự khác biệt giữa các loại</li>
                <li>Đề xuất combo tiết kiệm</li>
                <li>Cảnh báo rủi ro nếu dùng hàng kém chất lượng</li>
            </ul>
            <h5>4. 📋 Xác nhận đơn hàng:</h5>
            <ul>
                <li>Đọc lại thông tin xe và phụ tùng</li>
                <li>Xác nhận giá và thời gian giao hàng</li>
                <li>Hướng dẫn thanh toán</li>
            </ul>
            <h5>5. 🤝 Chăm sóc sau bán:</h5>
            <ul>
                <li>Hướng dẫn lắp đặt (nếu cần)</li>
                <li>Cam kết bảo hành</li>
                <li>Hẹn lịch bảo dưỡng tiếp theo</li>
            </ul>
            """);
        salesTips.setPriority(2);
        salesTips.setEstimatedReadTime(7);
        salesTips.setTags("sales,consultation,customer,service");
        trainingContentRepository.save(salesTips);

        // Training content for common mistakes
        TrainingContent commonMistakes = new TrainingContent();
        commonMistakes.setTitle("Những lỗi thường gặp khi bán phụ tùng xe tải");
        commonMistakes.setCategory(TrainingContent.TrainingCategory.COMMON_MISTAKES);
        commonMistakes.setSummary("Các lỗi phổ biến và cách tránh để không mất khách hàng");
        commonMistakes.setContent("""
            <h4>⚠️ 10 lỗi thường gặp và cách khắc phục</h4>
            <h5>1. Không hỏi năm sản xuất xe</h5>
            <p><strong>Hậu quả:</strong> Giao sai phụ tùng, khách phải đổi trả</p>
            <p><strong>Cách tránh:</strong> Luôn hỏi: "Xe anh năm mấy ạ?"</p>
            
            <h5>2. Nhầm lẫn HD65 và HD72</h5>
            <p><strong>Hậu quả:</strong> Má phanh không vừa, khách mất niềm tin</p>
            <p><strong>Cách tránh:</strong> Ghi rõ: HD65 (2015-2020), HD72 (2021+)</p>
            
            <h5>3. Không kiểm tra OEM number</h5>
            <p><strong>Hậu quả:</strong> Giao hàng sai, mất thời gian</p>
            <p><strong>Cách tránh:</strong> Đối chiếu OEM với catalog</p>
            
            <h5>4. Báo giá không rõ ràng</h5>
            <p><strong>Hậu quả:</strong> Khách hiểu nhầm, tranh cãi về giá</p>
            <p><strong>Cách tránh:</strong> Ghi rõ: giá bán lẻ/sỉ, có/không VAT</p>
            
            <h5>5. Không hỏi số lượng cần thiết</h5>
            <p><strong>Hậu quả:</strong> Khách mua thiếu, phải đặt thêm</p>
            <p><strong>Cách tránh:</strong> "Anh cần mấy bộ ạ? Má phanh nên thay cả 4 miếng"</p>
            
            <h5>6. Không tư vấn sản phẩm đi kèm</h5>
            <p><strong>Hậu quả:</strong> Bỏ lỡ cơ hội tăng doanh số</p>
            <p><strong>Cách tránh:</strong> "Thay má phanh rồi nên thay luôn phanh dầu ạ"</p>
            
            <h5>7. Hứa giao hàng không thực tế</h5>
            <p><strong>Hậu quả:</strong> Khách đợi, mất uy tín</p>
            <p><strong>Cách tránh:</strong> Kiểm tra kho trước khi hứa</p>
            """);
        commonMistakes.setPriority(1);
        commonMistakes.setEstimatedReadTime(5);
        commonMistakes.setShowInQuickHelp(true);
        commonMistakes.setTags("mistakes,errors,avoid,sales,tips");
        trainingContentRepository.save(commonMistakes);

        // Quick help content
        TrainingContent quickHelp1 = new TrainingContent();
        quickHelp1.setTitle("OEM Numbers phổ biến cho xe Hyundai");
        quickHelp1.setCategory(TrainingContent.TrainingCategory.TECHNICAL_SPECS);
        quickHelp1.setSummary("Danh sách OEM number thường dùng");
        quickHelp1.setContent("""
            <h4>🔢 OEM Numbers Hyundai thường gặp</h4>
            <h5>Má phanh:</h5>
            <ul>
                <li>HD65 trước: 58101-5H000</li>
                <li>HD65 sau: 58302-5H000</li>
                <li>HD72 trước: 58101-6A000</li>
                <li>HD72 sau: 58302-6A000</li>
            </ul>
            <h5>Lọc dầu:</h5>
            <ul>
                <li>HD65: 26300-4A000</li>
                <li>HD72: 26300-4A100</li>
            </ul>
            <h5>Lọc gió:</h5>
            <ul>
                <li>HD65: 28113-5H000</li>
                <li>HD72: 28113-6A000</li>
            </ul>
            """);
        quickHelp1.setPriority(3);
        quickHelp1.setEstimatedReadTime(2);
        quickHelp1.setShowInQuickHelp(true);
        quickHelp1.setTags("oem,numbers,hyundai,reference");
        trainingContentRepository.save(quickHelp1);

        System.out.println("✅ Đã tạo nội dung đào tạo thành công!");
    }
}
