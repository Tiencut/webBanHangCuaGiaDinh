package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductSupplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.ProductSupplierService;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * ProductSupplierController - REST API cho quản lý quan hệ Product-Supplier
 * 
 * Cung cấp các endpoints để:
 * - CRUD operations cho Product-Supplier
 * - Tìm kiếm và lọc supplier
 * - Quản lý tồn kho theo supplier
 * - Analytics và reporting
 */
@RestController
@RequestMapping("/api/product-suppliers")
@CrossOrigin(origins = "*")
public class ProductSupplierController {
    
    @Autowired
    private ProductSupplierService productSupplierService;
    
    /**
     * Tạo quan hệ Product-Supplier mới
     */
    @PostMapping
    public ResponseEntity<ProductSupplier> createProductSupplier(@RequestBody ProductSupplier productSupplier) {
        try {
            ProductSupplier created = productSupplierService.createProductSupplier(productSupplier);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Cập nhật quan hệ Product-Supplier
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductSupplier> updateProductSupplier(
            @PathVariable Long id, 
            @RequestBody ProductSupplier productSupplier) {
        try {
            ProductSupplier updated = productSupplierService.updateProductSupplier(id, productSupplier);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Xóa quan hệ Product-Supplier (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductSupplier(@PathVariable Long id) {
        try {
            productSupplierService.deleteProductSupplier(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Lấy quan hệ Product-Supplier theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductSupplier> getProductSupplierById(@PathVariable Long id) {
        try {
            ProductSupplier productSupplier = productSupplierService.getProductSupplierById(id);
            return ResponseEntity.ok(productSupplier);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Lấy tất cả supplier của một sản phẩm
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductSupplier>> getSuppliersByProductId(@PathVariable Long productId) {
        List<ProductSupplier> suppliers = productSupplierService.getSuppliersByProductId(productId);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Lấy tất cả sản phẩm của một supplier
     */
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<ProductSupplier>> getProductsBySupplierId(@PathVariable Long supplierId) {
        List<ProductSupplier> products = productSupplierService.getProductsBySupplierId(supplierId);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Tìm supplier tối ưu cho một sản phẩm
     */
    @GetMapping("/product/{productId}/optimal")
    public ResponseEntity<List<ProductSupplier>> getOptimalSuppliers(@PathVariable Long productId) {
        List<ProductSupplier> suppliers = productSupplierService.getOptimalSuppliers(productId);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm supplier có giá thấp nhất
     */
    @GetMapping("/product/{productId}/lowest-price")
    public ResponseEntity<List<ProductSupplier>> getLowestPriceSuppliers(@PathVariable Long productId) {
        List<ProductSupplier> suppliers = productSupplierService.getLowestPriceSuppliers(productId);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm supplier có lợi nhuận cao nhất
     */
    @GetMapping("/product/{productId}/highest-profit")
    public ResponseEntity<List<ProductSupplier>> getHighestProfitSuppliers(@PathVariable Long productId) {
        List<ProductSupplier> suppliers = productSupplierService.getHighestProfitSuppliers(productId);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm supplier có chất lượng cao nhất
     */
    @GetMapping("/product/{productId}/highest-quality")
    public ResponseEntity<List<ProductSupplier>> getHighestQualitySuppliers(@PathVariable Long productId) {
        List<ProductSupplier> suppliers = productSupplierService.getHighestQualitySuppliers(productId);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm supplier cần đặt hàng lại
     */
    @GetMapping("/needing-reorder")
    public ResponseEntity<List<ProductSupplier>> getSuppliersNeedingReorder() {
        List<ProductSupplier> suppliers = productSupplierService.getSuppliersNeedingReorder();
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm supplier hết hàng
     */
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<ProductSupplier>> getOutOfStockSuppliers() {
        List<ProductSupplier> suppliers = productSupplierService.getOutOfStockSuppliers();
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm supplier sắp hết hàng
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductSupplier>> getLowStockSuppliers() {
        List<ProductSupplier> suppliers = productSupplierService.getLowStockSuppliers();
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Cập nhật tồn kho
     */
    @PutMapping("/{productId}/{supplierId}/stock")
    public ResponseEntity<Void> updateStockQuantity(
            @PathVariable Long productId,
            @PathVariable Long supplierId,
            @RequestParam Integer quantity) {
        try {
            productSupplierService.updateStockQuantity(productId, supplierId, quantity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Cập nhật giá
     */
    @PutMapping("/{productId}/{supplierId}/prices")
    public ResponseEntity<Void> updatePrices(
            @PathVariable Long productId,
            @PathVariable Long supplierId,
            @RequestParam BigDecimal costPrice,
            @RequestParam BigDecimal sellingPrice) {
        try {
            productSupplierService.updatePrices(productId, supplierId, costPrice, sellingPrice);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Cập nhật đánh giá chất lượng
     */
    @PutMapping("/{productId}/{supplierId}/quality-rating")
    public ResponseEntity<Void> updateQualityRating(
            @PathVariable Long productId,
            @PathVariable Long supplierId,
            @RequestParam Double qualityRating) {
        try {
            productSupplierService.updateQualityRating(productId, supplierId, qualityRating);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Cập nhật đánh giá độ tin cậy
     */
    @PutMapping("/{productId}/{supplierId}/reliability-rating")
    public ResponseEntity<Void> updateReliabilityRating(
            @PathVariable Long productId,
            @PathVariable Long supplierId,
            @RequestParam Double reliabilityRating) {
        try {
            productSupplierService.updateReliabilityRating(productId, supplierId, reliabilityRating);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Tìm supplier theo khoảng giá
     */
    @GetMapping("/product/{productId}/price-range")
    public ResponseEntity<List<ProductSupplier>> getSuppliersByPriceRange(
            @PathVariable Long productId,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ProductSupplier> suppliers = productSupplierService.getSuppliersByPriceRange(productId, minPrice, maxPrice);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm supplier theo đánh giá chất lượng
     */
    @GetMapping("/product/{productId}/quality-rating")
    public ResponseEntity<List<ProductSupplier>> getSuppliersByQualityRating(
            @PathVariable Long productId,
            @RequestParam Double minRating) {
        List<ProductSupplier> suppliers = productSupplierService.getSuppliersByQualityRating(productId, minRating);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm supplier theo thời gian giao hàng
     */
    @GetMapping("/product/{productId}/delivery-time")
    public ResponseEntity<List<ProductSupplier>> getSuppliersByDeliveryTime(
            @PathVariable Long productId,
            @RequestParam Integer maxDeliveryDays) {
        List<ProductSupplier> suppliers = productSupplierService.getSuppliersByDeliveryTime(productId, maxDeliveryDays);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm supplier theo category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductSupplier>> getSuppliersByCategoryId(@PathVariable Long categoryId) {
        List<ProductSupplier> suppliers = productSupplierService.getSuppliersByCategoryId(categoryId);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm supplier theo brand
     */
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<ProductSupplier>> getSuppliersByBrand(@PathVariable String brand) {
        List<ProductSupplier> suppliers = productSupplierService.getSuppliersByBrand(brand);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm supplier theo vehicle type
     */
    @GetMapping("/vehicle-type/{vehicleType}")
    public ResponseEntity<List<ProductSupplier>> getSuppliersByVehicleType(@PathVariable String vehicleType) {
        List<ProductSupplier> suppliers = productSupplierService.getSuppliersByVehicleType(vehicleType);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Top supplier doanh số cao
     */
    @GetMapping("/top-selling")
    public ResponseEntity<Page<ProductSupplier>> getTopSellingSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("soldQuantity").descending());
        Page<ProductSupplier> suppliers = productSupplierService.getTopSellingSuppliers(pageable);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Top supplier đánh giá cao
     */
    @GetMapping("/top-rated")
    public ResponseEntity<Page<ProductSupplier>> getTopRatedSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("qualityRating").descending());
        Page<ProductSupplier> suppliers = productSupplierService.getTopRatedSuppliers(pageable);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Top supplier margin cao
     */
    @GetMapping("/highest-margin")
    public ResponseEntity<Page<ProductSupplier>> getHighestMarginSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductSupplier> suppliers = productSupplierService.getHighestMarginSuppliers(pageable);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Top supplier tồn kho cao
     */
    @GetMapping("/highest-stock")
    public ResponseEntity<Page<ProductSupplier>> getHighestStockSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("currentQuantity").descending());
        Page<ProductSupplier> suppliers = productSupplierService.getHighestStockSuppliers(pageable);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm kiếm theo tên sản phẩm
     */
    @GetMapping("/search/product")
    public ResponseEntity<List<ProductSupplier>> searchByProductName(@RequestParam String productName) {
        List<ProductSupplier> suppliers = productSupplierService.searchByProductName(productName);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Tìm kiếm theo tên supplier
     */
    @GetMapping("/search/supplier")
    public ResponseEntity<List<ProductSupplier>> searchBySupplierName(@RequestParam String supplierName) {
        List<ProductSupplier> suppliers = productSupplierService.searchBySupplierName(supplierName);
        return ResponseEntity.ok(suppliers);
    }
    
    /**
     * Lấy tất cả Product-Supplier với pagination
     */
    @GetMapping
    public ResponseEntity<Page<ProductSupplier>> getAllProductSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductSupplier> productSuppliers = productSupplierService.getAllProductSuppliers(pageable);
        
        return ResponseEntity.ok(productSuppliers);
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ProductSupplier API is running");
    }
} 