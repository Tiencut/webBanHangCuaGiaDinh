package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.VehicleModelService;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.VehicleModelService.VehicleProductSuggestion;

import jakarta.validation.Valid;

/**
 * VehicleModelController - API quản lý mẫu xe và gợi ý sản phẩm
 * 
 * Controller này cung cấp các API quan trọng nhất cho nhân viên bán hàng:
 * 
 * 1. **API Gợi ý thông minh**: Khi khách nói "hộp số xe Thaco Ollin"
 *    -> Tìm xe -> Gợi ý sản phẩm phù hợp
 * 
 * 2. **API Tìm kiếm xe**: Tìm mẫu xe theo tên, thương hiệu
 * 
 * 3. **API Quản lý**: CRUD mẫu xe, cập nhật thông tin kỹ thuật
 * 
 * Endpoints chính:
 * - GET /api/vehicles/suggest?product=hộp số&vehicle=thaco ollin
 * - GET /api/vehicles/search?keyword=ollin
 * - GET /api/vehicles/{id}/products (sản phẩm tương thích)
 * - POST /api/vehicles (tạo mẫu xe mới)
 */
@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleModelController {

    @Autowired
    private VehicleModelService vehicleModelService;

    /**
     * ⭐ API GỢI Ý THÔNG MINH - QUAN TRỌNG NHẤT ⭐
     * 
     * Khi khách hàng nói: "tôi cần hộp số cho xe Thaco Ollin"
     * Nhân viên gọi: GET /api/vehicles/suggest?product=hộp số&vehicle=thaco ollin
     * 
     * Response sẽ bao gồm:
     * - Danh sách mẫu xe Thaco Ollin  
     * - Tất cả sản phẩm tương thích
     * - Sản phẩm được lọc (hộp số)
     * - Thông tin kỹ thuật xe
     */
    @GetMapping("/suggest")
    public ResponseEntity<VehicleProductSuggestion> suggestProducts(
            @RequestParam(required = false) String product,
            @RequestParam String vehicle
    ) {
        try {
            if (product == null || product.trim().isEmpty()) {
                // Chỉ tìm xe, không lọc sản phẩm
                product = "";
            }
            
            VehicleProductSuggestion suggestion = vehicleModelService
                .suggestProductsByKeywordAndVehicle(product, vehicle);
            
            return ResponseEntity.ok(suggestion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * API Tìm kiếm xe thông minh
     * GET /api/vehicles/search?keyword=thaco ollin
     */
    @GetMapping("/search")
    public ResponseEntity<List<VehicleModel>> searchVehicles(
            @RequestParam String keyword
    ) {
        try {
            List<VehicleModel> vehicles = vehicleModelService.intelligentSearch(keyword);
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy tất cả sản phẩm tương thích với 1 mẫu xe
     * GET /api/vehicles/1/products
     */
    @GetMapping("/{id}/products")
    public ResponseEntity<List<Product>> getCompatibleProducts(@PathVariable Long id) {
        try {
            List<Product> products = vehicleModelService.suggestProductsForVehicle(id);
            return ResponseEntity.ok(products);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy danh sách tất cả mẫu xe
     * GET /api/vehicles
     */
    @GetMapping
    public ResponseEntity<List<VehicleModel>> getAllVehicles() {
        try {
            List<VehicleModel> vehicles = vehicleModelService.findAll();
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy thông tin 1 mẫu xe
     * GET /api/vehicles/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleModel> getVehicleById(@PathVariable Long id) {
        try {
            Optional<VehicleModel> vehicle = vehicleModelService.findById(id);
            return vehicle.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tạo mẫu xe mới
     * POST /api/vehicles
     */
    @PostMapping
    public ResponseEntity<VehicleModel> createVehicle(@Valid @RequestBody VehicleModel vehicle) {
        try {
            VehicleModel savedVehicle = vehicleModelService.createVehicleModel(vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Cập nhật mẫu xe
     * PUT /api/vehicles/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleModel> updateVehicle(
            @PathVariable Long id, 
            @Valid @RequestBody VehicleModel vehicle
    ) {
        try {
            VehicleModel updatedVehicle = vehicleModelService.updateVehicleModel(id, vehicle);
            return ResponseEntity.ok(updatedVehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Xóa mẫu xe (soft delete)
     * DELETE /api/vehicles/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        try {
            vehicleModelService.deleteVehicleModel(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tìm xe theo thương hiệu
     * GET /api/vehicles/brand/THACO
     */
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<VehicleModel>> getVehiclesByBrand(@PathVariable String brand) {
        try {
            List<VehicleModel> vehicles = vehicleModelService.findByBrand(brand);
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tìm xe theo năm sản xuất
     * GET /api/vehicles/year/2020
     */
    @GetMapping("/year/{year}")
    public ResponseEntity<List<VehicleModel>> getVehiclesByYear(@PathVariable int year) {
        try {
            List<VehicleModel> vehicles = vehicleModelService.findByProductionYear(year);
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tìm xe theo động cơ
     * GET /api/vehicles/engine/isuzu
     */
    @GetMapping("/engine/{engine}")
    public ResponseEntity<List<VehicleModel>> getVehiclesByEngine(@PathVariable String engine) {
        try {
            List<VehicleModel> vehicles = vehicleModelService.findByEngine(engine);
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy xe phổ biến (có nhiều sản phẩm)
     * GET /api/vehicles/popular
     */
    @GetMapping("/popular")
    public ResponseEntity<List<VehicleModel>> getPopularVehicles() {
        try {
            List<VehicleModel> vehicles = vehicleModelService.findPopularVehicles();
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * API gợi ý xe theo sản phẩm (ngược lại)
     * GET /api/vehicles/suggest-for-product/123
     */
    @GetMapping("/suggest-for-product/{productId}")
    public ResponseEntity<List<VehicleModel>> suggestVehiclesForProduct(@PathVariable Long productId) {
        try {
            List<VehicleModel> vehicles = vehicleModelService.suggestVehiclesForProduct(productId);
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
