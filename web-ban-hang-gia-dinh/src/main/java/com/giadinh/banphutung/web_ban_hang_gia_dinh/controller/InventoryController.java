package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.InventoryDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.InventoryService;
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
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventory Management", description = "APIs for managing inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @Operation(summary = "Get all inventories", description = "Retrieve a list of all active inventories")
    public ResponseEntity<List<InventoryDto>> getAllInventories() {
        log.info("GET /api/inventory - Fetching all inventories");
        List<InventoryDto> inventories = inventoryService.getAllInventories();
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/page")
    @Operation(summary = "Get inventories with pagination", description = "Retrieve inventories with pagination support")
    public ResponseEntity<Page<InventoryDto>> getInventoriesWithPagination(Pageable pageable) {
        log.info("GET /api/inventory/page - Fetching inventories with pagination: {}", pageable);
        Page<InventoryDto> inventories = inventoryService.getInventoriesWithPagination(pageable);
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory by ID", description = "Retrieve a specific inventory by its ID")
    public ResponseEntity<InventoryDto> getInventoryById(@PathVariable Long id) {
        log.info("GET /api/inventory/{} - Fetching inventory by id", id);
        InventoryDto inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/product/{productId}/supplier/{supplierId}")
    @Operation(summary = "Get inventory by product and supplier", description = "Retrieve inventory for a specific product-supplier combination")
    public ResponseEntity<InventoryDto> getInventoryByProductAndSupplier(@PathVariable Long productId, @PathVariable Long supplierId) {
        log.info("GET /api/inventory/product/{}/supplier/{} - Fetching inventory by product and supplier", productId, supplierId);
        InventoryDto inventory = inventoryService.getInventoryByProductAndSupplier(productId, supplierId);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping
    @Operation(summary = "Create new inventory", description = "Create a new inventory record")
    public ResponseEntity<InventoryDto> createInventory(@Valid @RequestBody InventoryDto inventoryDto) {
        log.info("POST /api/inventory - Creating new inventory for product: {} and supplier: {}", inventoryDto.getProductId(), inventoryDto.getSupplierId());
        InventoryDto inventory = inventoryService.createInventory(inventoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update inventory", description = "Update an existing inventory")
    public ResponseEntity<InventoryDto> updateInventory(@PathVariable Long id, @Valid @RequestBody InventoryDto inventoryDto) {
        log.info("PUT /api/inventory/{} - Updating inventory", id);
        InventoryDto inventory = inventoryService.updateInventory(id, inventoryDto);
        return ResponseEntity.ok(inventory);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete inventory", description = "Delete an inventory (soft delete)")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        log.info("DELETE /api/inventory/{} - Deleting inventory", id);
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get inventories by product", description = "Retrieve all inventories for a specific product")
    public ResponseEntity<List<InventoryDto>> getInventoriesByProduct(@PathVariable Long productId) {
        log.info("GET /api/inventory/product/{} - Fetching inventories by product", productId);
        List<InventoryDto> inventories = inventoryService.getInventoriesByProduct(productId);
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/supplier/{supplierId}")
    @Operation(summary = "Get inventories by supplier", description = "Retrieve all inventories for a specific supplier")
    public ResponseEntity<List<InventoryDto>> getInventoriesBySupplier(@PathVariable Long supplierId) {
        log.info("GET /api/inventory/supplier/{} - Fetching inventories by supplier", supplierId);
        List<InventoryDto> inventories = inventoryService.getInventoriesBySupplier(supplierId);
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock inventories", description = "Retrieve inventories with stock below threshold")
    public ResponseEntity<List<InventoryDto>> getLowStockInventories(@RequestParam(defaultValue = "10") Integer threshold) {
        log.info("GET /api/inventory/low-stock - Fetching low stock inventories with threshold: {}", threshold);
        List<InventoryDto> inventories = inventoryService.getLowStockInventories(threshold);
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/out-of-stock")
    @Operation(summary = "Get out of stock inventories", description = "Retrieve inventories with zero available stock")
    public ResponseEntity<List<InventoryDto>> getOutOfStockInventories() {
        log.info("GET /api/inventory/out-of-stock - Fetching out of stock inventories");
        List<InventoryDto> inventories = inventoryService.getOutOfStockInventories();
        return ResponseEntity.ok(inventories);
    }

    @PostMapping("/{id}/stock-in")
    @Operation(summary = "Stock in", description = "Add stock to inventory")
    public ResponseEntity<Void> stockIn(@PathVariable Long id, @RequestParam Integer quantity, 
                                       @RequestParam BigDecimal unitCost, @RequestParam(required = false) String batchNumber) {
        log.info("POST /api/inventory/{}/stock-in - Stocking in with quantity: {}, unit cost: {}", id, quantity, unitCost);
        inventoryService.stockIn(id, quantity, unitCost, batchNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/stock-out")
    @Operation(summary = "Stock out", description = "Remove stock from inventory")
    public ResponseEntity<Void> stockOut(@PathVariable Long id, @RequestParam Integer quantity) {
        log.info("POST /api/inventory/{}/stock-out - Stocking out with quantity: {}", id, quantity);
        inventoryService.stockOut(id, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reserve")
    @Operation(summary = "Reserve stock", description = "Reserve stock in inventory")
    public ResponseEntity<Void> reserveStock(@PathVariable Long id, @RequestParam Integer quantity) {
        log.info("POST /api/inventory/{}/reserve - Reserving stock with quantity: {}", id, quantity);
        inventoryService.reserveStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/release")
    @Operation(summary = "Release reserved stock", description = "Release reserved stock in inventory")
    public ResponseEntity<Void> releaseReservedStock(@PathVariable Long id, @RequestParam Integer quantity) {
        log.info("POST /api/inventory/{}/release - Releasing reserved stock with quantity: {}", id, quantity);
        inventoryService.releaseReservedStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search inventories", description = "Search inventories by keyword")
    public ResponseEntity<List<InventoryDto>> searchInventories(@RequestParam String keyword) {
        log.info("GET /api/inventory/search - Searching inventories with keyword: {}", keyword);
        List<InventoryDto> inventories = inventoryService.searchInventories(keyword);
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/location/{location}")
    @Operation(summary = "Get inventories by location", description = "Retrieve inventories by location")
    public ResponseEntity<List<InventoryDto>> getInventoriesByLocation(@PathVariable String location) {
        log.info("GET /api/inventory/location/{} - Fetching inventories by location", location);
        List<InventoryDto> inventories = inventoryService.getInventoriesByLocation(location);
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/expiring")
    @Operation(summary = "Get expiring inventories", description = "Retrieve inventories expiring within specified days")
    public ResponseEntity<List<InventoryDto>> getExpiringInventories(@RequestParam(defaultValue = "30") int daysThreshold) {
        log.info("GET /api/inventory/expiring - Fetching inventories expiring within {} days", daysThreshold);
        List<InventoryDto> inventories = inventoryService.getExpiringInventories(daysThreshold);
        return ResponseEntity.ok(inventories);
    }
} 