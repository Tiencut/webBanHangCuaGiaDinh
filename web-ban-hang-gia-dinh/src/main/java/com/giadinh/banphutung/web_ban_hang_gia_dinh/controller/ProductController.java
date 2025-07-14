package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Product Management", description = "APIs để quản lý sản phẩm")
public class ProductController {
    
    private final ProductService productService;
    
    @Operation(summary = "Lấy tất cả sản phẩm đang hoạt động", 
               description = "Trả về danh sách tất cả sản phẩm có trạng thái ACTIVE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Getting all active products");
        List<Product> products = productService.findAllActiveProducts();
        return ResponseEntity.ok(products);
    }
    
    @Operation(summary = "Lấy sản phẩm theo ID", 
               description = "Trả về thông tin chi tiết của sản phẩm theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID của sản phẩm") @PathVariable Long id) {
        log.info("Getting product with id: {}", id);
        return productService.findById(id)
            .map(product -> ResponseEntity.ok(product))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Lấy sản phẩm theo mã sản phẩm", 
               description = "Trả về thông tin sản phẩm theo mã sản phẩm")
    @GetMapping("/code/{code}")
    public ResponseEntity<Product> getProductByCode(
            @Parameter(description = "Mã sản phẩm") @PathVariable String code) {
        log.info("Getting product with code: {}", code);
        return productService.findByCode(code)
            .map(product -> ResponseEntity.ok(product))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Lấy sản phẩm theo part number", 
               description = "Trả về thông tin sản phẩm theo part number")
    @GetMapping("/part-number/{partNumber}")
    public ResponseEntity<Product> getProductByPartNumber(
            @Parameter(description = "Part number của sản phẩm") @PathVariable String partNumber) {
        log.info("Getting product with part number: {}", partNumber);
        return productService.findByPartNumber(partNumber)
            .map(product -> ResponseEntity.ok(product))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Lấy sản phẩm theo danh mục", 
               description = "Trả về danh sách sản phẩm thuộc một danh mục")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @Parameter(description = "ID của danh mục") @PathVariable Long categoryId) {
        log.info("Getting products by category id: {}", categoryId);
        List<Product> products = productService.findByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    
    @Operation(summary = "Lấy sản phẩm theo thương hiệu", 
               description = "Trả về danh sách sản phẩm theo thương hiệu")
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(
            @Parameter(description = "Tên thương hiệu") @PathVariable String brand) {
        log.info("Getting products by brand: {}", brand);
        List<Product> products = productService.findByBrand(brand);
        return ResponseEntity.ok(products);
    }
    
    @Operation(summary = "Lấy sản phẩm theo loại xe", 
               description = "Trả về danh sách sản phẩm phù hợp với loại xe")
    @GetMapping("/vehicle/{vehicleType}")
    public ResponseEntity<List<Product>> getProductsByVehicleType(
            @Parameter(description = "Loại xe (car, motorcycle, truck, etc.)") @PathVariable String vehicleType) {
        log.info("Getting products by vehicle type: {}", vehicleType);
        List<Product> products = productService.findByVehicleType(vehicleType);
        return ResponseEntity.ok(products);
    }
    
    @Operation(summary = "Lấy sản phẩm combo", 
               description = "Trả về danh sách các sản phẩm combo")
    @GetMapping("/combo")
    public ResponseEntity<List<Product>> getComboProducts() {
        log.info("Getting combo products");
        List<Product> products = productService.findComboProducts();
        return ResponseEntity.ok(products);
    }
    
    @Operation(summary = "Lấy sản phẩm cần nhập thêm", 
               description = "Trả về danh sách sản phẩm có số lượng tồn kho thấp")
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        log.info("Getting low stock products");
        List<Product> products = productService.findLowStockProducts();
        return ResponseEntity.ok(products);
    }
    
    @Operation(summary = "Lấy sản phẩm theo khoảng giá", 
               description = "Trả về danh sách sản phẩm trong khoảng giá chỉ định")
    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @Parameter(description = "Giá tối thiểu") @RequestParam BigDecimal minPrice,
            @Parameter(description = "Giá tối đa") @RequestParam BigDecimal maxPrice) {
        log.info("Getting products by price range: {} - {}", minPrice, maxPrice);
        List<Product> products = productService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    @Operation(summary = "Tìm kiếm sản phẩm", 
               description = "Tìm kiếm sản phẩm theo từ khóa với phân trang")
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam String term,
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Kích thước trang") @RequestParam(defaultValue = "10") int size) {
        log.info("Searching products with term: {}", term);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.searchProducts(term, pageable);
        return ResponseEntity.ok(products);
    }
    
    @Operation(summary = "Tạo sản phẩm mới", 
               description = "Tạo một sản phẩm mới trong hệ thống")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tạo thành công"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
        @ApiResponse(responseCode = "404", description = "Danh mục không tồn tại"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Thông tin sản phẩm cần tạo") @Valid @RequestBody Product product) {
        try {
            Product savedProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (BusinessException e) {
            log.error("Business error creating product: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found creating product: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cập nhật sản phẩm", 
               description = "Cập nhật thông tin của sản phẩm theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
        @ApiResponse(responseCode = "404", description = "Sản phẩm không tồn tại"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "ID của sản phẩm") @PathVariable Long id, 
            @Parameter(description = "Thông tin sản phẩm cần cập nhật") @Valid @RequestBody Product product
    ) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (BusinessException e) {
            log.error("Business error updating product: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Product not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cập nhật giá sản phẩm", 
               description = "Cập nhật giá của sản phẩm theo ID")
    @PutMapping("/{id}/price")
    public ResponseEntity<Product> updateProductPrice(
            @Parameter(description = "ID của sản phẩm") @PathVariable Long id,
            @Parameter(description = "Giá mới") @RequestParam BigDecimal newPrice
    ) {
        try {
            Product updatedProduct = productService.updateProductPrice(id, newPrice);
            return ResponseEntity.ok(updatedProduct);
        } catch (BusinessException e) {
            log.error("Business error updating product price: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Product not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating product price: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cập nhật trạng thái sản phẩm", 
               description = "Cập nhật trạng thái của sản phẩm (ACTIVE, INACTIVE, DISCONTINUED)")
    @PutMapping("/{id}/status")
    public ResponseEntity<Product> updateProductStatus(
            @Parameter(description = "ID của sản phẩm") @PathVariable Long id,
            @Parameter(description = "Trạng thái mới") @RequestParam ProductStatus status
    ) {
        try {
            Product updatedProduct = productService.updateProductStatus(id, status);
            return ResponseEntity.ok(updatedProduct);
        } catch (BusinessException e) {
            log.error("Business error updating product status: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Product not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating product status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Xóa sản phẩm", 
               description = "Xóa sản phẩm theo ID (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Xóa thành công"),
        @ApiResponse(responseCode = "404", description = "Sản phẩm không tồn tại"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID của sản phẩm") @PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Product not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
