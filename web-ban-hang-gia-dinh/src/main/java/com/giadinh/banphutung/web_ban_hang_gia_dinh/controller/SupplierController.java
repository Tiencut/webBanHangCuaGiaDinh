package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

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

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.SupplierService;

import jakarta.validation.Valid;

/**
 * SupplierController - API quản lý nhà cung cấp
 * 
 * Đây là controller xử lý các API liên quan đến nhà cung cấp:
 * - CRUD nhà cung cấp (Create, Read, Update, Delete)
 * - Tìm kiếm nhà cung cấp theo tên, mã
 * - Phân trang danh sách nhà cung cấp
 * - Thống kê nhà cung cấp
 * 
 * Các endpoint chính:
 * - GET /api/suppliers - Lấy danh sách nhà cung cấp
 * - GET /api/suppliers/{id} - Lấy thông tin 1 nhà cung cấp
 * - POST /api/suppliers - Tạo nhà cung cấp mới
 * - PUT /api/suppliers/{id} - Cập nhật nhà cung cấp
 * - DELETE /api/suppliers/{id} - Xóa nhà cung cấp
 * - GET /api/suppliers/search - Tìm kiếm nhà cung cấp
 */
@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*") // Cho phép frontend gọi API từ domain khác
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * Lấy danh sách tất cả nhà cung cấp với phân trang
     * GET /api/suppliers?page=0&size=10&sort=name
     */
    @GetMapping
    public ResponseEntity<Page<Supplier>> getAllSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
            Page<Supplier> suppliers = supplierService.findAll(pageable);
            return ResponseEntity.ok(suppliers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy thông tin 1 nhà cung cấp theo ID
     * GET /api/suppliers/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        try {
            Optional<Supplier> supplier = supplierService.findById(id);
            return supplier.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tạo nhà cung cấp mới
     * POST /api/suppliers
     * Body: JSON của Supplier
     */
    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@Valid @RequestBody Supplier supplier) {
        try {
            Supplier savedSupplier = supplierService.save(supplier);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Cập nhật thông tin nhà cung cấp
     * PUT /api/suppliers/1
     * Body: JSON của Supplier
     */
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(
            @PathVariable Long id, 
            @Valid @RequestBody Supplier supplier
    ) {
        try {
            if (!supplierService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            supplier.setId(id);
            Supplier updatedSupplier = supplierService.save(supplier);
            return ResponseEntity.ok(updatedSupplier);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Xóa nhà cung cấp
     * DELETE /api/suppliers/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        try {
            if (!supplierService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            supplierService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tìm kiếm nhà cung cấp theo tên hoặc mã
     * GET /api/suppliers/search?name=ABC&code=SUP001
     */
    @GetMapping("/search")
    public ResponseEntity<List<Supplier>> searchSuppliers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code
    ) {
        try {
            List<Supplier> suppliers;
            
            if (name != null && !name.isEmpty()) {
                suppliers = supplierService.findByNameContaining(name);
            } else if (code != null && !code.isEmpty()) {
                suppliers = supplierService.findByCodeContaining(code);
            } else {
                suppliers = supplierService.findAll();
            }
            
            return ResponseEntity.ok(suppliers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy nhà cung cấp theo mã code
     * GET /api/suppliers/code/SUP001
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Supplier> getSupplierByCode(@PathVariable String code) {
        try {
            Optional<Supplier> supplier = supplierService.findByCode(code);
            return supplier.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    /**
     * Lấy nhà cung cấp active
     * GET /api/suppliers/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<Supplier>> getActiveSuppliers() {
        try {
            List<Supplier> suppliers = supplierService.findActiveSuppliers();
            return ResponseEntity.ok(suppliers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy số lượng nhà cung cấp
     * GET /api/suppliers/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getSupplierCount() {
        try {
            long count = supplierService.count();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy top nhà cung cấp theo số lượng sản phẩm
     * GET /api/suppliers/top
     */
    @GetMapping("/top")
    public ResponseEntity<List<Supplier>> getTopSuppliers(
            @RequestParam(defaultValue = "10") int limit
    ) {
        try {
            List<Supplier> suppliers = supplierService.findTopSuppliers(limit);
            return ResponseEntity.ok(suppliers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
