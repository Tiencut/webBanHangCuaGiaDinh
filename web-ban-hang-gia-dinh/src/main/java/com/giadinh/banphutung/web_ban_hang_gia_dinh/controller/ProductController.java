package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product.ProductStatus;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProductController {
    
    private final ProductService productService;
    
    // GET /api/products - Lấy tất cả product active
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Getting all active products");
        List<Product> products = productService.findAllActiveProducts();
        return ResponseEntity.ok(products);
    }
    
    // GET /api/products/{id} - Lấy product theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.info("Getting product with id: {}", id);
        return productService.findById(id)
            .map(product -> ResponseEntity.ok(product))
            .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/products/code/{code} - Lấy product theo code
    @GetMapping("/code/{code}")
    public ResponseEntity<Product> getProductByCode(@PathVariable String code) {
        log.info("Getting product with code: {}", code);
        return productService.findByCode(code)
            .map(product -> ResponseEntity.ok(product))
            .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/products/part-number/{partNumber} - Lấy product theo part number
    @GetMapping("/part-number/{partNumber}")
    public ResponseEntity<Product> getProductByPartNumber(@PathVariable String partNumber) {
        log.info("Getting product with part number: {}", partNumber);
        return productService.findByPartNumber(partNumber)
            .map(product -> ResponseEntity.ok(product))
            .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/products/category/{categoryId} - Lấy product theo category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        log.info("Getting products by category id: {}", categoryId);
        List<Product> products = productService.findByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    
    // GET /api/products/brand/{brand} - Lấy product theo brand
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        log.info("Getting products by brand: {}", brand);
        List<Product> products = productService.findByBrand(brand);
        return ResponseEntity.ok(products);
    }
    
    // GET /api/products/vehicle/{vehicleType} - Lấy product theo loại xe
    @GetMapping("/vehicle/{vehicleType}")
    public ResponseEntity<List<Product>> getProductsByVehicleType(@PathVariable String vehicleType) {
        log.info("Getting products by vehicle type: {}", vehicleType);
        List<Product> products = productService.findByVehicleType(vehicleType);
        return ResponseEntity.ok(products);
    }
    
    // GET /api/products/combo - Lấy product combo
    @GetMapping("/combo")
    public ResponseEntity<List<Product>> getComboProducts() {
        log.info("Getting combo products");
        List<Product> products = productService.findComboProducts();
        return ResponseEntity.ok(products);
    }
    
    // GET /api/products/low-stock - Lấy product cần nhập thêm
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        log.info("Getting low stock products");
        List<Product> products = productService.findLowStockProducts();
        return ResponseEntity.ok(products);
    }
    
    // GET /api/products/price-range - Lấy product theo khoảng giá
    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.info("Getting products by price range: {} - {}", minPrice, maxPrice);
        List<Product> products = productService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    // GET /api/products/search - Tìm kiếm product
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Searching products with term: {}", term);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.searchProducts(term, pageable);
        return ResponseEntity.ok(products);
    }
    
    // POST /api/products - Tạo product mới
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        log.info("Creating new product: {}", product.getName());
        try {
            Product savedProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (RuntimeException e) {
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT /api/products/{id} - Cập nhật product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {
        log.info("Updating product with id: {}", id);
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            log.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT /api/products/{id}/price - Cập nhật giá product
    @PutMapping("/{id}/price")
    public ResponseEntity<Product> updateProductPrice(
            @PathVariable Long id,
            @RequestParam BigDecimal price) {
        log.info("Updating price for product id: {} to {}", id, price);
        try {
            Product updatedProduct = productService.updateProductPrice(id, price);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            log.error("Error updating product price: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT /api/products/{id}/status - Cập nhật trạng thái product
    @PutMapping("/{id}/status")
    public ResponseEntity<Product> updateProductStatus(
            @PathVariable Long id,
            @RequestParam ProductStatus status) {
        log.info("Updating status for product id: {} to {}", id, status);
        try {
            Product updatedProduct = productService.updateProductStatus(id, status);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            log.error("Error updating product status: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DELETE /api/products/{id} - Xóa product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with id: {}", id);
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
