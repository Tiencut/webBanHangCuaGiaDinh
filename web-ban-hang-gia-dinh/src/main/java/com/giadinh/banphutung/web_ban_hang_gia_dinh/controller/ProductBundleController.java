package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductBundle;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.ProductBundleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/product-bundles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Bundle", description = "API quản lý sản phẩm combo/bundle")
public class ProductBundleController {
    
    private final ProductBundleService productBundleService;
    
    @Operation(summary = "Tạo bundle mới", 
               description = "Tạo một bundle mới từ sản phẩm cha và sản phẩm con")
    @PostMapping
    public ResponseEntity<ProductBundle> createBundle(@RequestBody ProductBundle bundle) {
        log.info("Creating new product bundle");
        ProductBundle createdBundle = productBundleService.createBundle(bundle);
        return ResponseEntity.ok(createdBundle);
    }
    
    @Operation(summary = "Lấy bundle theo ID", 
               description = "Trả về thông tin chi tiết của một bundle")
    @GetMapping("/{id}")
    public ResponseEntity<ProductBundle> getBundleById(
            @Parameter(description = "ID của bundle") @PathVariable Long id) {
        log.info("Getting bundle by ID: {}", id);
        return productBundleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Lấy tất cả sản phẩm con của một sản phẩm cha", 
               description = "Trả về danh sách các sản phẩm con trong một combo")
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<ProductBundle>> getBundlesByParent(
            @Parameter(description = "ID của sản phẩm cha") @PathVariable Long parentId) {
        log.info("Getting bundles by parent product ID: {}", parentId);
        List<ProductBundle> bundles = productBundleService.findByParentProduct(parentId);
        return ResponseEntity.ok(bundles);
    }
    
    @Operation(summary = "Lấy tất cả sản phẩm cha của một sản phẩm con", 
               description = "Trả về danh sách các combo chứa sản phẩm con")
    @GetMapping("/child/{childId}")
    public ResponseEntity<List<ProductBundle>> getBundlesByChild(
            @Parameter(description = "ID của sản phẩm con") @PathVariable Long childId) {
        log.info("Getting bundles by child product ID: {}", childId);
        List<ProductBundle> bundles = productBundleService.findByChildProduct(childId);
        return ResponseEntity.ok(bundles);
    }
    
    @Operation(summary = "Lấy tất cả sản phẩm combo", 
               description = "Trả về danh sách tất cả sản phẩm có combo")
    @GetMapping("/combo-products")
    public ResponseEntity<List<Product>> getAllComboProducts() {
        log.info("Getting all combo products");
        List<Product> comboProducts = productBundleService.findAllComboProducts();
        return ResponseEntity.ok(comboProducts);
    }
    
    @Operation(summary = "Lấy sản phẩm thay thế", 
               description = "Trả về danh sách sản phẩm có thể thay thế cho một sản phẩm con")
    @GetMapping("/substitutes/{childId}")
    public ResponseEntity<List<Product>> getSubstituteProducts(
            @Parameter(description = "ID của sản phẩm con") @PathVariable Long childId) {
        log.info("Getting substitute products for child ID: {}", childId);
        List<Product> substitutes = productBundleService.findSubstituteProducts(childId);
        return ResponseEntity.ok(substitutes);
    }
    
    @Operation(summary = "Cập nhật bundle", 
               description = "Cập nhật thông tin của một bundle")
    @PutMapping("/{id}")
    public ResponseEntity<ProductBundle> updateBundle(
            @Parameter(description = "ID của bundle") @PathVariable Long id,
            @RequestBody ProductBundle bundleUpdate) {
        log.info("Updating bundle with ID: {}", id);
        ProductBundle updatedBundle = productBundleService.updateBundle(id, bundleUpdate);
        return ResponseEntity.ok(updatedBundle);
    }
    
    @Operation(summary = "Xóa bundle", 
               description = "Xóa một bundle")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBundle(
            @Parameter(description = "ID của bundle") @PathVariable Long id) {
        log.info("Deleting bundle with ID: {}", id);
        productBundleService.deleteBundle(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Thêm sản phẩm thay thế", 
               description = "Thêm một sản phẩm vào danh sách thay thế của bundle")
    @PostMapping("/{bundleId}/substitutes/{substituteId}")
    public ResponseEntity<ProductBundle> addSubstituteProduct(
            @Parameter(description = "ID của bundle") @PathVariable Long bundleId,
            @Parameter(description = "ID của sản phẩm thay thế") @PathVariable Long substituteId) {
        log.info("Adding substitute product {} to bundle {}", substituteId, bundleId);
        ProductBundle updatedBundle = productBundleService.addSubstituteProduct(bundleId, substituteId);
        return ResponseEntity.ok(updatedBundle);
    }
    
    @Operation(summary = "Xóa sản phẩm thay thế", 
               description = "Xóa một sản phẩm khỏi danh sách thay thế của bundle")
    @DeleteMapping("/{bundleId}/substitutes/{substituteId}")
    public ResponseEntity<ProductBundle> removeSubstituteProduct(
            @Parameter(description = "ID của bundle") @PathVariable Long bundleId,
            @Parameter(description = "ID của sản phẩm thay thế") @PathVariable Long substituteId) {
        log.info("Removing substitute product {} from bundle {}", substituteId, bundleId);
        ProductBundle updatedBundle = productBundleService.removeSubstituteProduct(bundleId, substituteId);
        return ResponseEntity.ok(updatedBundle);
    }
    
    @Operation(summary = "Tính tổng giá combo", 
               description = "Tính tổng giá của một combo")
    @GetMapping("/{parentId}/total-price")
    public ResponseEntity<BigDecimal> calculateBundleTotalPrice(
            @Parameter(description = "ID của sản phẩm cha") @PathVariable Long parentId) {
        log.info("Calculating total price for bundle with parent ID: {}", parentId);
        BigDecimal totalPrice = productBundleService.calculateBundleTotalPrice(parentId);
        return ResponseEntity.ok(totalPrice);
    }
    
    @Operation(summary = "Kiểm tra sản phẩm combo", 
               description = "Kiểm tra xem một sản phẩm có phải là combo không")
    @GetMapping("/{productId}/is-combo")
    public ResponseEntity<Boolean> isComboProduct(
            @Parameter(description = "ID của sản phẩm") @PathVariable Long productId) {
        log.info("Checking if product {} is a combo", productId);
        boolean isCombo = productBundleService.isComboProduct(productId);
        return ResponseEntity.ok(isCombo);
    }
    
    @Operation(summary = "Tạo combo từ danh sách sản phẩm", 
               description = "Tạo một combo mới từ danh sách sản phẩm con")
    @PostMapping("/create-combo")
    public ResponseEntity<ProductBundle> createComboFromProducts(
            @RequestParam Long parentProductId,
            @RequestParam List<Long> childProductIds,
            @RequestParam List<Integer> quantities,
            @RequestParam List<BigDecimal> prices) {
        log.info("Creating combo from products for parent ID: {}", parentProductId);
        ProductBundle bundle = productBundleService.createComboFromProducts(
                parentProductId, childProductIds, quantities, prices);
        return ResponseEntity.ok(bundle);
    }
} 