package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Inventory;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    // GET /api/inventory/product/{productId}/total - Lấy tổng số lượng có sẵn
    @GetMapping("/product/{productId}/total")
    public ResponseEntity<Map<String, Integer>> getTotalQuantityByProduct(@PathVariable Long productId) {
        log.info("Getting total quantity for product id: {}", productId);
        
        Integer totalQuantity = inventoryService.getTotalQuantity(productId);
        Integer totalAvailable = inventoryService.getTotalAvailableQuantity(productId);
        Integer totalReserved = inventoryService.getTotalReservedQuantity(productId);
        
        Map<String, Integer> result = Map.of(
            "totalQuantity", totalQuantity,
            "totalAvailable", totalAvailable,
            "totalReserved", totalReserved
        );
        
        return ResponseEntity.ok(result);
    }
    
    // GET /api/inventory/product/{productId} - Lấy chi tiết inventory theo product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Inventory>> getInventoryByProduct(@PathVariable Long productId) {
        log.info("Getting inventory details for product id: {}", productId);
        List<Inventory> inventories = inventoryService.getInventoryByProduct(productId);
        return ResponseEntity.ok(inventories);
    }
    
    // GET /api/inventory/product/{productId}/available - Lấy inventory có sẵn
    @GetMapping("/product/{productId}/available")
    public ResponseEntity<List<Inventory>> getAvailableInventoryByProduct(@PathVariable Long productId) {
        log.info("Getting available inventory for product id: {}", productId);
        List<Inventory> inventories = inventoryService.getAvailableInventoryByProduct(productId);
        return ResponseEntity.ok(inventories);
    }
    
    // GET /api/inventory/product/{productId}/statistics - Lấy thống kê inventory
    @GetMapping("/product/{productId}/statistics")
    public ResponseEntity<Map<String, Object>> getInventoryStatistics(@PathVariable Long productId) {
        log.info("Getting inventory statistics for product id: {}", productId);
        Map<String, Object> statistics = inventoryService.getInventoryStatistics(productId);
        return ResponseEntity.ok(statistics);
    }
    
    // POST /api/inventory/add - Nhập hàng
    @PostMapping("/add")
    public ResponseEntity<Inventory> addInventory(@RequestBody AddInventoryRequest request) {
        log.info("Adding inventory for product {}", request.getProductId());
        try {
            Inventory inventory = inventoryService.createOrUpdateInventory(
                request.getProductId(),
                request.getSupplierId(),
                request.getQuantity(),
                request.getCostPrice(),
                request.getBatchNumber(),
                request.getLocation()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
        } catch (RuntimeException e) {
            log.error("Error adding inventory: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // POST /api/inventory/release - Xuất hàng
    @PostMapping("/release")
    public ResponseEntity<String> releaseInventory(@RequestBody ReleaseInventoryRequest request) {
        log.info("Releasing {} units of product {}", request.getQuantity(), request.getProductId());
        try {
            inventoryService.releaseInventory(request.getProductId(), request.getQuantity());
            return ResponseEntity.ok("Xuất hàng thành công");
        } catch (RuntimeException e) {
            log.error("Error releasing inventory: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // POST /api/inventory/reserve - Đặt trước hàng
    @PostMapping("/reserve")
    public ResponseEntity<String> reserveInventory(@RequestBody ReserveInventoryRequest request) {
        log.info("Reserving {} units of product {}", request.getQuantity(), request.getProductId());
        try {
            inventoryService.reserveInventory(request.getProductId(), request.getQuantity());
            return ResponseEntity.ok("Đặt trước hàng thành công");
        } catch (RuntimeException e) {
            log.error("Error reserving inventory: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // GET /api/inventory/supplier/{supplierId} - Lấy inventory theo supplier
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<Inventory>> getInventoryBySupplier(@PathVariable Long supplierId) {
        log.info("Getting inventory for supplier id: {}", supplierId);
        List<Inventory> inventories = inventoryService.getInventoryBySupplier(supplierId);
        return ResponseEntity.ok(inventories);
    }
    
    // PUT /api/inventory/{inventoryId}/location - Cập nhật vị trí
    @PutMapping("/{inventoryId}/location")
    public ResponseEntity<String> updateInventoryLocation(
            @PathVariable Long inventoryId,
            @RequestParam String location) {
        log.info("Updating location for inventory id: {} to {}", inventoryId, location);
        try {
            inventoryService.updateInventoryLocation(inventoryId, location);
            return ResponseEntity.ok("Cập nhật vị trí thành công");
        } catch (RuntimeException e) {
            log.error("Error updating inventory location: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // PUT /api/inventory/{inventoryId}/damaged - Đánh dấu hỏng
    @PutMapping("/{inventoryId}/damaged")
    public ResponseEntity<String> markInventoryAsDamaged(
            @PathVariable Long inventoryId,
            @RequestParam String notes) {
        log.info("Marking inventory {} as damaged", inventoryId);
        try {
            inventoryService.markInventoryAsDamaged(inventoryId, notes);
            return ResponseEntity.ok("Đánh dấu hàng hỏng thành công");
        } catch (RuntimeException e) {
            log.error("Error marking inventory as damaged: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // DTO Classes
    public static class AddInventoryRequest {
        private Long productId;
        private Long supplierId;
        private Integer quantity;
        private BigDecimal costPrice;
        private String batchNumber;
        private String location;
        
        // Getters and setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        
        public Long getSupplierId() { return supplierId; }
        public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public BigDecimal getCostPrice() { return costPrice; }
        public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }
        
        public String getBatchNumber() { return batchNumber; }
        public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
    }
    
    public static class ReleaseInventoryRequest {
        private Long productId;
        private Integer quantity;
        
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
    
    public static class ReserveInventoryRequest {
        private Long productId;
        private Integer quantity;
        
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}
