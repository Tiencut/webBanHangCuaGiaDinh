package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.SupplierDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Supplier Management", description = "APIs for managing suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    @Operation(summary = "Get all suppliers", description = "Retrieve a list of all active suppliers")
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        log.info("GET /api/suppliers - Fetching all suppliers");
        List<SupplierDto> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/page")
    @Operation(summary = "Get suppliers with pagination", description = "Retrieve suppliers with pagination support")
    public ResponseEntity<Page<SupplierDto>> getSuppliersWithPagination(Pageable pageable) {
        log.info("GET /api/suppliers/page - Fetching suppliers with pagination: {}", pageable);
        Page<SupplierDto> suppliers = supplierService.getSuppliersWithPagination(pageable);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID", description = "Retrieve a specific supplier by their ID")
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable Long id) {
        log.info("GET /api/suppliers/{} - Fetching supplier by id", id);
        SupplierDto supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get supplier by code", description = "Retrieve a specific supplier by their code")
    public ResponseEntity<SupplierDto> getSupplierByCode(@PathVariable String code) {
        log.info("GET /api/suppliers/code/{} - Fetching supplier by code", code);
        SupplierDto supplier = supplierService.getSupplierByCode(code);
        return ResponseEntity.ok(supplier);
    }

    @PostMapping
    @Operation(summary = "Create new supplier", description = "Create a new supplier")
    public ResponseEntity<SupplierDto> createSupplier(@Valid @RequestBody SupplierDto supplierDto) {
        log.info("POST /api/suppliers - Creating new supplier: {}", supplierDto.getName());
        SupplierDto supplier = supplierService.createSupplier(supplierDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(supplier);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update supplier", description = "Update an existing supplier")
    public ResponseEntity<SupplierDto> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierDto supplierDto) {
        log.info("PUT /api/suppliers/{} - Updating supplier", id);
        SupplierDto supplier = supplierService.updateSupplier(id, supplierDto);
        return ResponseEntity.ok(supplier);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete supplier", description = "Delete a supplier (soft delete)")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        log.info("DELETE /api/suppliers/{} - Deleting supplier", id);
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search suppliers", description = "Search suppliers by keyword")
    public ResponseEntity<List<SupplierDto>> searchSuppliers(@RequestParam String keyword) {
        log.info("GET /api/suppliers/search - Searching suppliers with keyword: {}", keyword);
        List<SupplierDto> suppliers = supplierService.searchSuppliers(keyword);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get suppliers by status", description = "Retrieve suppliers by status")
    public ResponseEntity<List<SupplierDto>> getSuppliersByStatus(@PathVariable Supplier.SupplierStatus status) {
        log.info("GET /api/suppliers/status/{} - Fetching suppliers by status", status);
        List<SupplierDto> suppliers = supplierService.getSuppliersByStatus(status);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/vehicle-brand/{vehicleBrand}")
    @Operation(summary = "Get suppliers by vehicle brand", description = "Retrieve suppliers that supply parts for a specific vehicle brand")
    public ResponseEntity<List<SupplierDto>> getSuppliersByVehicleBrand(@PathVariable String vehicleBrand) {
        log.info("GET /api/suppliers/vehicle-brand/{} - Fetching suppliers by vehicle brand", vehicleBrand);
        List<SupplierDto> suppliers = supplierService.getSuppliersByVehicleBrand(vehicleBrand);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/rating/{minRating}")
    @Operation(summary = "Get suppliers by minimum rating", description = "Retrieve suppliers with rating greater than or equal to the specified value")
    public ResponseEntity<List<SupplierDto>> getSuppliersByRating(@PathVariable Double minRating) {
        log.info("GET /api/suppliers/rating/{} - Fetching suppliers by minimum rating", minRating);
        List<SupplierDto> suppliers = supplierService.getSuppliersByRating(minRating);
        return ResponseEntity.ok(suppliers);
    }

    @PutMapping("/{id}/rating")
    @Operation(summary = "Update supplier rating", description = "Update the rating of a supplier")
    public ResponseEntity<Void> updateSupplierRating(@PathVariable Long id, @RequestParam Double newRating) {
        log.info("PUT /api/suppliers/{}/rating - Updating supplier rating to {}", id, newRating);
        supplierService.updateSupplierRating(id, newRating);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/top-rated")
    @Operation(summary = "Get top rated suppliers", description = "Retrieve top rated suppliers")
    public ResponseEntity<List<SupplierDto>> getTopRatedSuppliers(@RequestParam(defaultValue = "10") int limit) {
        log.info("GET /api/suppliers/top-rated - Fetching top {} rated suppliers", limit);
        List<SupplierDto> suppliers = supplierService.getTopRatedSuppliers(limit);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/payment-terms/{paymentTerms}")
    @Operation(summary = "Get suppliers by payment terms", description = "Retrieve suppliers by payment terms")
    public ResponseEntity<List<SupplierDto>> getSuppliersByPaymentTerms(@PathVariable Supplier.PaymentTerms paymentTerms) {
        log.info("GET /api/suppliers/payment-terms/{} - Fetching suppliers by payment terms", paymentTerms);
        List<SupplierDto> suppliers = supplierService.getSuppliersByPaymentTerms(paymentTerms);
        return ResponseEntity.ok(suppliers);
    }
}
