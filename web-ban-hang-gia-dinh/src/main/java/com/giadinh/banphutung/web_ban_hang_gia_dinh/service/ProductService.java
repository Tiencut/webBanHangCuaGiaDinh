package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Category;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product.ProductStatus;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CategoryRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    // Tạo product mới
    public Product createProduct(Product product) {
        log.info("Creating new product: {}", product.getName());
        
        // Kiểm tra code đã tồn tại
        if (product.getCode() != null && productRepository.existsByCode(product.getCode())) {
            throw new BusinessException("Product code đã tồn tại: " + product.getCode());
        }
        
        // Tự động tạo code nếu chưa có
        if (product.getCode() == null || product.getCode().trim().isEmpty()) {
            product.setCode(generateProductCode(product.getName()));
        }
        
        // Kiểm tra part number đã tồn tại
        if (product.getPartNumber() != null && 
            productRepository.existsByPartNumber(product.getPartNumber())) {
            throw new BusinessException("Part number đã tồn tại: " + product.getPartNumber());
        }
        
        // Validate category
        if (product.getCategory() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }
        
        // Set default values
        if (product.getSellingPrice() == null) {
            product.setSellingPrice(product.getBasePrice());
        }
        
        return productRepository.save(product);
    }
    
    // Tìm product theo ID
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    
    // Tìm product theo code
    @Transactional(readOnly = true)
    public Optional<Product> findByCode(String code) {
        return productRepository.findByCode(code);
    }
    
    // Tìm product theo part number
    @Transactional(readOnly = true)
    public Optional<Product> findByPartNumber(String partNumber) {
        return productRepository.findByPartNumber(partNumber);
    }
    
    // Tìm tất cả product active
    @Transactional(readOnly = true)
    public List<Product> findAllActiveProducts() {
        return productRepository.findByStatusAndIsActiveTrueOrderByNameAsc(ProductStatus.ACTIVE);
    }
    
    // Tìm product theo category
    @Transactional(readOnly = true)
    public List<Product> findByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return productRepository.findByCategoryOrderByNameAsc(category);
    }
    
    // Tìm product theo brand
    @Transactional(readOnly = true)
    public List<Product> findByBrand(String brand) {
        return productRepository.findByBrandIgnoreCaseOrderByNameAsc(brand);
    }
    
    // Tìm product theo vehicle type
    @Transactional(readOnly = true)
    public List<Product> findByVehicleType(String vehicleType) {
        return productRepository.findByVehicleTypeIgnoreCaseOrderByNameAsc(vehicleType);
    }
    
    // Tìm product trong khoảng giá
    @Transactional(readOnly = true)
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByBasePriceBetweenOrderByBasePriceAsc(minPrice, maxPrice);
    }
    
    // Tìm product combo
    @Transactional(readOnly = true)
    public List<Product> findComboProducts() {
        return productRepository.findByIsComboTrue();
    }
    
    // Tìm product cần nhập thêm (low stock)
    @Transactional(readOnly = true)
    public List<Product> findLowStockProducts() {
        return productRepository.findLowStockProducts();
    }
    
    // Tìm kiếm product
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String searchTerm, Pageable pageable) {
        return productRepository.searchProducts(searchTerm, pageable);
    }
    
    // Cập nhật product
    public Product updateProduct(Long id, Product productUpdate) {
        log.info("Updating product with id: {}", id);
        
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        // Cập nhật thông tin cơ bản
        existingProduct.setName(productUpdate.getName());
        existingProduct.setDescription(productUpdate.getDescription());
        existingProduct.setBasePrice(productUpdate.getBasePrice());
        existingProduct.setSellingPrice(productUpdate.getSellingPrice());
        existingProduct.setCostPrice(productUpdate.getCostPrice());
        existingProduct.setWeight(productUpdate.getWeight());
        existingProduct.setDimensions(productUpdate.getDimensions());
        existingProduct.setBrand(productUpdate.getBrand());
        existingProduct.setModel(productUpdate.getModel());
        existingProduct.setVehicleType(productUpdate.getVehicleType());
        existingProduct.setWarrantyPeriod(productUpdate.getWarrantyPeriod());
        existingProduct.setMinStockLevel(productUpdate.getMinStockLevel());
        existingProduct.setReorderPoint(productUpdate.getReorderPoint());
        
        // Cập nhật code nếu khác
        if (!existingProduct.getCode().equals(productUpdate.getCode())) {
            if (productRepository.existsByCode(productUpdate.getCode())) {
                throw new BusinessException("Product code đã tồn tại: " + productUpdate.getCode());
            }
            existingProduct.setCode(productUpdate.getCode());
        }
        
        // Cập nhật part number nếu khác
        if (productUpdate.getPartNumber() != null && 
            !productUpdate.getPartNumber().equals(existingProduct.getPartNumber())) {
            if (productRepository.existsByPartNumber(productUpdate.getPartNumber())) {
                throw new BusinessException("Part number đã tồn tại: " + productUpdate.getPartNumber());
            }
            existingProduct.setPartNumber(productUpdate.getPartNumber());
        }
        
        // Cập nhật category
        if (productUpdate.getCategory() != null) {
            Category category = categoryRepository.findById(productUpdate.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existingProduct.setCategory(category);
        }
        
        return productRepository.save(existingProduct);
    }
    
    // Cập nhật giá sản phẩm
    public Product updateProductPrice(Long id, BigDecimal newPrice) {
        log.info("Updating price for product id: {} to {}", id, newPrice);
        
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        product.setSellingPrice(newPrice);
        return productRepository.save(product);
    }
    
    // Cập nhật trạng thái sản phẩm
    public Product updateProductStatus(Long id, ProductStatus status) {
        log.info("Updating status for product id: {} to {}", id, status);
        
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        product.setStatus(status);
        return productRepository.save(product);
    }
    
    // Xóa product (soft delete)
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        product.setIsActive(false);
        productRepository.save(product);
        
        log.info("Product {} has been deactivated", product.getName());
    }
    
    // Helper methods
    private String generateProductCode(String name) {
        // Tạo code từ tên (lấy 3 ký tự đầu + timestamp)
        String prefix = name.toLowerCase()
                           .replaceAll("[^a-z0-9]", "")
                           .substring(0, Math.min(3, name.length()));
        
        String code = prefix.toUpperCase() + System.currentTimeMillis() % 10000;
        
        // Đảm bảo unique
        int counter = 1;
        String originalCode = code;
        while (productRepository.existsByCode(code)) {
            code = originalCode + "_" + counter++;
        }
        
        return code;
    }
}
