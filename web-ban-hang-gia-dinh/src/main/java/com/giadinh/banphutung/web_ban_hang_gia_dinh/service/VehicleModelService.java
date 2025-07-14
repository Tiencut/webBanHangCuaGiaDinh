package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel.VehicleType;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.VehicleModelRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * VehicleModelService - Service xử lý logic nghiệp vụ mẫu xe
 * 
 * Đây là service quan trọng nhất để hỗ trợ nhân viên bán hàng.
 * Khi khách hàng nói tên xe, service này sẽ:
 * 
 * 1. Tìm mẫu xe chính xác
 * 2. Gợi ý tất cả sản phẩm tương thích  
 * 3. Hiển thị thông tin kỹ thuật
 * 4. Đưa ra giá tham khảo
 * 
 * Ví dụ: "hộp số xe Thaco Ollin" 
 * -> Tìm Thaco Ollin models
 * -> Gợi ý các hộp số tương thích
 * -> Hiển thị thông tin động cơ, năm sản xuất
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VehicleModelService {
    
    private final VehicleModelRepository vehicleModelRepository;

    // ========== CRUD Methods cơ bản ==========
    
    // Tạo vehicle model mới
    public VehicleModel createVehicleModel(VehicleModel vehicleModel) {
        log.info("Creating new vehicle model: {}", vehicleModel.getName());
        
        // Kiểm tra code đã tồn tại
        if (vehicleModel.getCode() != null && vehicleModelRepository.existsByCode(vehicleModel.getCode())) {
            throw new BusinessException("Mã xe đã tồn tại: " + vehicleModel.getCode());
        }
        
        // Tự động tạo code nếu chưa có
        if (vehicleModel.getCode() == null || vehicleModel.getCode().trim().isEmpty()) {
            vehicleModel.setCode(generateVehicleModelCode(vehicleModel.getName()));
        }
        
        return vehicleModelRepository.save(vehicleModel);
    }
    
    @Transactional(readOnly = true)
    public Optional<VehicleModel> findById(Long id) {
        return vehicleModelRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<VehicleModel> findByCode(String code) {
        return vehicleModelRepository.findByCode(code);
    }
    
    @Transactional(readOnly = true)
    public List<VehicleModel> findAll() {
        return vehicleModelRepository.findByIsActiveTrueOrderByBrandAscNameAsc();
    }
    
    // Cập nhật vehicle model
    public VehicleModel updateVehicleModel(Long id, VehicleModel vehicleModelUpdate) {
        log.info("Updating vehicle model with id: {}", id);
        
        VehicleModel existingVehicleModel = vehicleModelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle model not found with id: " + id));
        
        // Cập nhật thông tin cơ bản
        existingVehicleModel.setName(vehicleModelUpdate.getName());
        existingVehicleModel.setBrand(vehicleModelUpdate.getBrand());
        existingVehicleModel.setModel(vehicleModelUpdate.getModel());
        existingVehicleModel.setYearFrom(vehicleModelUpdate.getYearFrom());
        existingVehicleModel.setEngineType(vehicleModelUpdate.getEngineType());
        existingVehicleModel.setTransmissionType(vehicleModelUpdate.getTransmissionType());
        existingVehicleModel.setFuelType(vehicleModelUpdate.getFuelType());
        existingVehicleModel.setDescription(vehicleModelUpdate.getDescription());
        
        // Cập nhật code nếu khác
        if (!existingVehicleModel.getCode().equals(vehicleModelUpdate.getCode())) {
            if (vehicleModelRepository.existsByCode(vehicleModelUpdate.getCode())) {
                throw new BusinessException("Vehicle model code đã tồn tại: " + vehicleModelUpdate.getCode());
            }
            existingVehicleModel.setCode(vehicleModelUpdate.getCode());
        }
        
        return vehicleModelRepository.save(existingVehicleModel);
    }
    
    public void deleteVehicleModel(Long id) {
        VehicleModel vehicleModel = vehicleModelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle model not found"));
        
        vehicleModel.setIsActive(false);
        vehicleModelRepository.save(vehicleModel);
        
        log.info("Vehicle model {} has been deactivated", vehicleModel.getName());
    }
    
    // ========== Tính năng gợi ý thông minh ==========
    
    /**
     * Tìm kiếm xe thông minh - Core function!
     * 
     * Khi khách hàng nói: "hộp số xe Thaco Ollin"
     * -> keyword = "Thaco Ollin"
     * -> Trả về tất cả model Thaco Ollin
     */
    @Transactional(readOnly = true)
    public List<VehicleModel> intelligentSearch(String keyword) {
        log.info("Intelligent search for: {}", keyword);
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // Tìm kiếm theo tên, thương hiệu, mã, tên gọi khác
        List<VehicleModel> results = vehicleModelRepository.searchVehicles(keyword.trim());
        
        log.info("Found {} vehicle models for keyword: {}", results.size(), keyword);
        return results;
    }
    
    /**
     * Gợi ý sản phẩm theo mẫu xe - Core function!
     * 
     * Sau khi tìm được xe, gợi ý tất cả sản phẩm tương thích
     * Ví dụ: Thaco Ollin -> hộp số, má phanh, lọc dầu, v.v.
     */
    @Transactional(readOnly = true)
    public List<Product> suggestProductsForVehicle(Long vehicleModelId) {
        log.info("Suggesting products for vehicle model id: {}", vehicleModelId);
        
        VehicleModel vehicleModel = vehicleModelRepository.findById(vehicleModelId)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle model not found"));
        
        // Lấy tất cả sản phẩm tương thích
        List<Product> compatibleProducts = vehicleModel.getCompatibleProducts();
        
        log.info("Found {} compatible products for {}", 
                compatibleProducts.size(), vehicleModel.getName());
        
        return compatibleProducts;
    }
    
    /**
     * Gợi ý sản phẩm theo từ khóa và xe
     * 
     * Ví dụ: keyword="hộp số", vehicleName="Thaco Ollin"
     * -> Tìm xe Thaco Ollin
     * -> Lọc chỉ sản phẩm hộp số
     */
    @Transactional(readOnly = true)
    public VehicleProductSuggestion suggestProductsByKeywordAndVehicle(String productKeyword, String vehicleKeyword) {
        log.info("Suggesting products by keyword '{}' for vehicle '{}'", productKeyword, vehicleKeyword);
        
        // 1. Tìm xe
        List<VehicleModel> vehicles = intelligentSearch(vehicleKeyword);
        
        if (vehicles.isEmpty()) {
            throw new BusinessException("Không tìm thấy xe phù hợp với: " + vehicleKeyword);
        }
        
        // 2. Lấy tất cả sản phẩm tương thích
        List<Product> allProducts = vehicles.stream()
            .flatMap(v -> v.getCompatibleProducts().stream())
            .distinct()
            .collect(Collectors.toList());
        
        // 3. Lọc sản phẩm theo từ khóa
        List<Product> filteredProducts = allProducts.stream()
            .filter(p -> matchesProductKeyword(p, productKeyword))
            .collect(Collectors.toList());
        
        VehicleProductSuggestion suggestion = new VehicleProductSuggestion();
        suggestion.setVehicles(vehicles);
        suggestion.setAllCompatibleProducts(allProducts);
        suggestion.setSuggestedProducts(filteredProducts);
        suggestion.setSearchKeyword(productKeyword + " " + vehicleKeyword);
        
        log.info("Suggestion result: {} vehicles, {} total products, {} filtered products", 
                vehicles.size(), allProducts.size(), filteredProducts.size());
        
        return suggestion;
    }
    
    /**
     * Gợi ý xe theo sản phẩm
     * 
     * Ví dụ: Có sản phẩm "Hộp số 5 cấp ABC"
     * -> Gợi ý tất cả xe tương thích
     */
    @Transactional(readOnly = true)
    public List<VehicleModel> suggestVehiclesForProduct(Long productId) {
        log.info("Suggesting vehicles for product id: {}", productId);
        
        return vehicleModelRepository.findCompatibleWithProduct(productId);
    }
    
    // ========== Search Methods ==========
    
    @Transactional(readOnly = true)
    public List<VehicleModel> findByBrand(String brand) {
        return vehicleModelRepository.findByBrandIgnoreCase(brand);
    }
    
    @Transactional(readOnly = true)
    public List<VehicleModel> findByVehicleType(VehicleType vehicleType) {
        return vehicleModelRepository.findByVehicleType(vehicleType);
    }
    
    @Transactional(readOnly = true)
    public List<VehicleModel> findByProductionYear(int year) {
        return vehicleModelRepository.findByProductionYear(year);
    }
    
    @Transactional(readOnly = true)
    public List<VehicleModel> findByEngine(String engine) {
        return vehicleModelRepository.findByEngineContaining(engine);
    }
    
    @Transactional(readOnly = true)
    public List<VehicleModel> findPopularVehicles() {
        List<Object[]> results = vehicleModelRepository.findPopularVehicles();
        return results.stream()
                .map(row -> (VehicleModel) row[0])
                .collect(Collectors.toList());
    }
    
    // ========== Helper Methods ==========
    
    private String generateVehicleModelCode(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("Tên xe không được để trống");
        }
        
        // Loại bỏ ký tự đặc biệt và chuyển thành uppercase
        String code = name.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        
        // Giới hạn độ dài
        if (code.length() > 20) {
            code = code.substring(0, 20);
        }
        
        // Thêm timestamp để đảm bảo unique
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        
        return code + timestamp;
    }
    
    private String generateVehicleCode(String brand, String name) {
        // Tạo code từ thương hiệu và tên
        String brandCode = brand.toUpperCase().replaceAll("[^A-Z0-9]", "").substring(0, Math.min(4, brand.length()));
        String nameCode = name.toUpperCase().replaceAll("[^A-Z0-9]", "").substring(0, Math.min(6, name.length()));
        
        String code = brandCode + nameCode;
        
        // Đảm bảo unique
        int counter = 1;
        String originalCode = code;
        while (vehicleModelRepository.existsByCode(code)) {
            code = originalCode + counter++;
        }
        
        return code;
    }
    
    private boolean matchesProductKeyword(Product product, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }
        
        String lowerKeyword = keyword.toLowerCase();
        
        return (product.getName() != null && product.getName().toLowerCase().contains(lowerKeyword)) ||
               (product.getDescription() != null && product.getDescription().toLowerCase().contains(lowerKeyword)) ||
               (product.getTags() != null && product.getTags().stream()
                   .anyMatch(tag -> tag.toLowerCase().contains(lowerKeyword))) ||
               (product.getCategory() != null && product.getCategory().getName() != null && 
                   product.getCategory().getName().toLowerCase().contains(lowerKeyword));
    }
    
    // ========== Inner Classes ==========
    
    /**
     * Class chứa kết quả gợi ý sản phẩm theo xe
     */
    public static class VehicleProductSuggestion {
        private List<VehicleModel> vehicles;
        private List<Product> allCompatibleProducts;
        private List<Product> suggestedProducts;
        private String searchKeyword;
        
        // Getters and setters
        public List<VehicleModel> getVehicles() { return vehicles; }
        public void setVehicles(List<VehicleModel> vehicles) { this.vehicles = vehicles; }
        
        public List<Product> getAllCompatibleProducts() { return allCompatibleProducts; }
        public void setAllCompatibleProducts(List<Product> allCompatibleProducts) { this.allCompatibleProducts = allCompatibleProducts; }
        
        public List<Product> getSuggestedProducts() { return suggestedProducts; }
        public void setSuggestedProducts(List<Product> suggestedProducts) { this.suggestedProducts = suggestedProducts; }
        
        public String getSearchKeyword() { return searchKeyword; }
        public void setSearchKeyword(String searchKeyword) { this.searchKeyword = searchKeyword; }
    }
}
