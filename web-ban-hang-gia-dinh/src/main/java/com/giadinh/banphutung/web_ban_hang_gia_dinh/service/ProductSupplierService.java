package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductSupplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductSupplierRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.SupplierRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ProductSupplierService - Service cho quản lý quan hệ Product-Supplier
 * 
 * Cung cấp các business logic để:
 * - Quản lý multi-supplier cho sản phẩm
 * - Tối ưu hóa lựa chọn supplier
 * - Quản lý tồn kho theo supplier
 * - Phân tích hiệu quả supplier
 */
@Service
@Transactional
public class ProductSupplierService {
    
    @Autowired
    private ProductSupplierRepository productSupplierRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    /**
     * Tạo quan hệ Product-Supplier mới
     */
    public ProductSupplier createProductSupplier(ProductSupplier productSupplier) {
        // Validate product và supplier tồn tại
        Product product = productRepository.findById(productSupplier.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        Supplier supplier = supplierRepository.findById(productSupplier.getSupplier().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        
        // Kiểm tra quan hệ đã tồn tại
        Optional<ProductSupplier> existing = productSupplierRepository
                .findByProductIdAndSupplierId(product.getId(), supplier.getId());
        
        if (existing.isPresent()) {
            throw new BusinessException("Product-Supplier relationship already exists");
        }
        
        // Set default values
        productSupplier.setProduct(product);
        productSupplier.setSupplier(supplier);
        productSupplier.setIsActive(true);
        productSupplier.setCreatedAt(LocalDateTime.now());
        productSupplier.setUpdatedAt(LocalDateTime.now());
        
        // Set default priority order nếu chưa có
        if (productSupplier.getPriorityOrder() == null) {
            long count = productSupplierRepository.countByProductIdAndIsActiveTrue(product.getId());
            productSupplier.setPriorityOrder((int) (count + 1));
        }
        
        return productSupplierRepository.save(productSupplier);
    }
    
    /**
     * Cập nhật quan hệ Product-Supplier
     */
    public ProductSupplier updateProductSupplier(Long id, ProductSupplier updatedProductSupplier) {
        ProductSupplier existing = productSupplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductSupplier not found"));
        
        // Update fields
        if (updatedProductSupplier.getCostPrice() != null) {
            existing.setCostPrice(updatedProductSupplier.getCostPrice());
        }
        if (updatedProductSupplier.getSellingPrice() != null) {
            existing.setSellingPrice(updatedProductSupplier.getSellingPrice());
        }
        if (updatedProductSupplier.getStockQuantity() != null) {
            existing.setStockQuantity(updatedProductSupplier.getStockQuantity());
        }
        if (updatedProductSupplier.getMinStockLevel() != null) {
            existing.setMinStockLevel(updatedProductSupplier.getMinStockLevel());
        }
        if (updatedProductSupplier.getReorderPoint() != null) {
            existing.setReorderPoint(updatedProductSupplier.getReorderPoint());
        }
        if (updatedProductSupplier.getMaxStockLevel() != null) {
            existing.setMaxStockLevel(updatedProductSupplier.getMaxStockLevel());
        }
        if (updatedProductSupplier.getDeliveryTimeDays() != null) {
            existing.setDeliveryTimeDays(updatedProductSupplier.getDeliveryTimeDays());
        }
        if (updatedProductSupplier.getQualityRating() != null) {
            existing.setQualityRating(updatedProductSupplier.getQualityRating());
        }
        if (updatedProductSupplier.getReliabilityRating() != null) {
            existing.setReliabilityRating(updatedProductSupplier.getReliabilityRating());
        }
        if (updatedProductSupplier.getPriorityOrder() != null) {
            existing.setPriorityOrder(updatedProductSupplier.getPriorityOrder());
        }
        if (updatedProductSupplier.getIsActive() != null) {
            existing.setIsActive(updatedProductSupplier.getIsActive());
        }
        if (updatedProductSupplier.getNotes() != null) {
            existing.setNotes(updatedProductSupplier.getNotes());
        }
        
        existing.setUpdatedAt(LocalDateTime.now());
        
        return productSupplierRepository.save(existing);
    }
    
    /**
     * Xóa quan hệ Product-Supplier (soft delete)
     */
    public void deleteProductSupplier(Long id) {
        ProductSupplier productSupplier = productSupplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductSupplier not found"));
        
        productSupplier.setIsActive(false);
        productSupplier.setUpdatedAt(LocalDateTime.now());
        
        productSupplierRepository.save(productSupplier);
    }
    
    /**
     * Lấy quan hệ Product-Supplier theo ID
     */
    @Transactional(readOnly = true)
    public ProductSupplier getProductSupplierById(Long id) {
        return productSupplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductSupplier not found"));
    }
    
    /**
     * Lấy tất cả supplier của một sản phẩm
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getSuppliersByProductId(Long productId) {
        return productSupplierRepository.findByProductIdAndIsActiveTrueOrderByPriorityOrderAsc(productId);
    }
    
    /**
     * Lấy tất cả sản phẩm của một supplier
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getProductsBySupplierId(Long supplierId) {
        return productSupplierRepository.findBySupplierIdAndIsActiveTrueOrderByStockQuantityDesc(supplierId);
    }
    
    /**
     * Tìm supplier tối ưu cho một sản phẩm
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getOptimalSuppliers(Long productId) {
        return productSupplierRepository.findOptimalSuppliers(productId);
    }
    
    /**
     * Tìm supplier có giá thấp nhất
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getLowestPriceSuppliers(Long productId) {
        return productSupplierRepository.findLowestPriceSuppliers(productId);
    }
    
    /**
     * Tìm supplier có lợi nhuận cao nhất
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getHighestProfitSuppliers(Long productId) {
        return productSupplierRepository.findHighestProfitSuppliers(productId);
    }
    
    /**
     * Tìm supplier có chất lượng cao nhất
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getHighestQualitySuppliers(Long productId) {
        return productSupplierRepository.findHighestQualitySuppliers(productId);
    }
    
    /**
     * Tìm supplier cần đặt hàng lại
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getSuppliersNeedingReorder() {
        return productSupplierRepository.findSuppliersNeedingReorder();
    }
    
    /**
     * Tìm supplier hết hàng
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getOutOfStockSuppliers() {
        return productSupplierRepository.findOutOfStockSuppliers();
    }
    
    /**
     * Tìm supplier sắp hết hàng
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getLowStockSuppliers() {
        return productSupplierRepository.findLowStockSuppliers();
    }
    
    /**
     * Cập nhật tồn kho
     */
    public void updateStockQuantity(Long productId, Long supplierId, Integer newQuantity) {
        ProductSupplier productSupplier = productSupplierRepository
                .findByProductIdAndSupplierId(productId, supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductSupplier not found"));
        
        productSupplier.setStockQuantity(newQuantity);
        productSupplier.setUpdatedAt(LocalDateTime.now());
        
        // Cập nhật trạng thái tồn kho
        updateStockStatus(productSupplier);
        
        productSupplierRepository.save(productSupplier);
    }
    
    /**
     * Cập nhật giá
     */
    public void updatePrices(Long productId, Long supplierId, BigDecimal costPrice, BigDecimal sellingPrice) {
        ProductSupplier productSupplier = productSupplierRepository
                .findByProductIdAndSupplierId(productId, supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductSupplier not found"));
        
        productSupplier.setCostPrice(costPrice);
        productSupplier.setSellingPrice(sellingPrice);
        productSupplier.setLastPurchaseDate(LocalDateTime.now());
        productSupplier.setUpdatedAt(LocalDateTime.now());
        
        productSupplierRepository.save(productSupplier);
    }
    
    /**
     * Cập nhật đánh giá chất lượng
     */
    public void updateQualityRating(Long productId, Long supplierId, Double qualityRating) {
        ProductSupplier productSupplier = productSupplierRepository
                .findByProductIdAndSupplierId(productId, supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductSupplier not found"));
        
        productSupplier.setQualityRating(qualityRating);
        productSupplier.setUpdatedAt(LocalDateTime.now());
        
        productSupplierRepository.save(productSupplier);
    }
    
    /**
     * Cập nhật đánh giá độ tin cậy
     */
    public void updateReliabilityRating(Long productId, Long supplierId, Double reliabilityRating) {
        ProductSupplier productSupplier = productSupplierRepository
                .findByProductIdAndSupplierId(productId, supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductSupplier not found"));
        
        productSupplier.setReliabilityRating(reliabilityRating);
        productSupplier.setUpdatedAt(LocalDateTime.now());
        
        productSupplierRepository.save(productSupplier);
    }
    
    /**
     * Tìm supplier theo khoảng giá
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getSuppliersByPriceRange(Long productId, BigDecimal minPrice, BigDecimal maxPrice) {
        return productSupplierRepository.findByPriceRange(productId, minPrice, maxPrice);
    }
    
    /**
     * Tìm supplier theo đánh giá chất lượng
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getSuppliersByQualityRating(Long productId, Double minRating) {
        return productSupplierRepository.findByQualityRating(productId, minRating);
    }
    
    /**
     * Tìm supplier theo thời gian giao hàng
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getSuppliersByDeliveryTime(Long productId, Integer maxDeliveryDays) {
        return productSupplierRepository.findByDeliveryTime(productId, maxDeliveryDays);
    }
    
    /**
     * Tìm supplier theo category
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getSuppliersByCategoryId(Long categoryId) {
        return productSupplierRepository.findByCategoryId(categoryId);
    }
    
    /**
     * Tìm supplier theo brand
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getSuppliersByBrand(String brand) {
        return productSupplierRepository.findByBrand(brand);
    }
    
    /**
     * Tìm supplier theo vehicle type
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> getSuppliersByVehicleType(String vehicleType) {
        return productSupplierRepository.findByVehicleType(vehicleType);
    }
    
    /**
     * Tìm supplier có doanh số cao nhất
     */
    @Transactional(readOnly = true)
    public Page<ProductSupplier> getTopSellingSuppliers(Pageable pageable) {
        return productSupplierRepository.findTopSellingSuppliers(pageable);
    }
    
    /**
     * Tìm supplier có đánh giá cao nhất
     */
    @Transactional(readOnly = true)
    public Page<ProductSupplier> getTopRatedSuppliers(Pageable pageable) {
        return productSupplierRepository.findTopRatedSuppliers(pageable);
    }
    
    /**
     * Tìm supplier có lợi nhuận margin cao nhất
     */
    @Transactional(readOnly = true)
    public Page<ProductSupplier> getHighestMarginSuppliers(Pageable pageable) {
        return productSupplierRepository.findHighestMarginSuppliers(pageable);
    }
    
    /**
     * Tìm supplier có tồn kho cao nhất
     */
    @Transactional(readOnly = true)
    public Page<ProductSupplier> getHighestStockSuppliers(Pageable pageable) {
        return productSupplierRepository.findHighestStockSuppliers(pageable);
    }
    
    /**
     * Tìm supplier theo tên sản phẩm
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> searchByProductName(String productName) {
        return productSupplierRepository.findByProductNameContainingIgnoreCase(productName);
    }
    
    /**
     * Tìm supplier theo tên supplier
     */
    @Transactional(readOnly = true)
    public List<ProductSupplier> searchBySupplierName(String supplierName) {
        return productSupplierRepository.findBySupplierNameContainingIgnoreCase(supplierName);
    }
    
    /**
     * Cập nhật trạng thái tồn kho
     */
    private void updateStockStatus(ProductSupplier productSupplier) {
        Integer currentQuantity = productSupplier.getStockQuantity();
        Integer minStockLevel = productSupplier.getMinStockLevel();
        Integer maxStockLevel = productSupplier.getMaxStockLevel();
        
        if (currentQuantity == 0) {
            productSupplier.setStockStatus(ProductSupplier.StockStatus.OUT_OF_STOCK);
        } else if (currentQuantity <= minStockLevel) {
            productSupplier.setStockStatus(ProductSupplier.StockStatus.LOW);
        } else if (currentQuantity >= maxStockLevel) {
            productSupplier.setStockStatus(ProductSupplier.StockStatus.OVERSTOCKED);
        } else {
            productSupplier.setStockStatus(ProductSupplier.StockStatus.NORMAL);
        }
    }
    
    /**
     * Tính toán vòng quay tồn kho
     */
    public void calculateTurnoverRate(Long productId, Long supplierId) {
        ProductSupplier productSupplier = productSupplierRepository
                .findByProductIdAndSupplierId(productId, supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductSupplier not found"));
        
        // Tính vòng quay dựa trên số lượng bán và tồn kho trung bình
        if (productSupplier.getSoldQuantity() != null && productSupplier.getSoldQuantity() > 0) {
            double averageStock = (productSupplier.getMaxStockLevel() + productSupplier.getMinStockLevel()) / 2.0;
            if (averageStock > 0) {
                double turnoverRate = productSupplier.getSoldQuantity() / averageStock;
                productSupplier.setTurnoverRate(turnoverRate);
            }
        }
        
        productSupplierRepository.save(productSupplier);
    }
    
    /**
     * Tính toán số ngày tồn kho trung bình
     */
    public void calculateAverageDaysInStock(Long productId, Long supplierId) {
        ProductSupplier productSupplier = productSupplierRepository
                .findByProductIdAndSupplierId(productId, supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductSupplier not found"));
        
        // Tính số ngày tồn kho dựa trên vòng quay
        if (productSupplier.getTurnoverRate() != null && productSupplier.getTurnoverRate() > 0) {
            int averageDays = (int) (365 / productSupplier.getTurnoverRate());
            productSupplier.setAverageDaysInStock(averageDays);
        }
        
        productSupplierRepository.save(productSupplier);
    }
    
    /**
     * Lấy tất cả Product-Supplier với pagination
     */
    @Transactional(readOnly = true)
    public Page<ProductSupplier> getAllProductSuppliers(Pageable pageable) {
        return productSupplierRepository.findAll(pageable);
    }
} 