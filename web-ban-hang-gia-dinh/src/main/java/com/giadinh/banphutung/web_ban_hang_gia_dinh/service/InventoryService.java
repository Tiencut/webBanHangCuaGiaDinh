package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Inventory;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.InventoryRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    
    // Tạo hoặc cập nhật inventory
    public Inventory createOrUpdateInventory(Long productId, Long supplierId, Integer quantity, 
                                           BigDecimal costPrice, String batchNumber, String location) {
        log.info("Creating or updating inventory for product {} from supplier {}", productId, supplierId);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        Supplier supplier = null;
        if (supplierId != null) {
            supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + supplierId));
        }
        
        // Tìm inventory hiện tại
        Optional<Inventory> existingInventory = inventoryRepository.findByProductAndSupplier(product, supplier);
        
        if (existingInventory.isPresent()) {
            // Cập nhật inventory hiện tại
            Inventory inventory = existingInventory.get();
            inventory.addQuantity(quantity);
            if (costPrice != null) {
                inventory.setCostPrice(costPrice);
            }
            if (batchNumber != null) {
                inventory.setBatchNumber(batchNumber);
            }
            if (location != null) {
                inventory.setLocation(location);
            }
            return inventoryRepository.save(inventory);
        } else {
            // Tạo inventory mới
            Inventory newInventory = new Inventory();
            newInventory.setProduct(product);
            newInventory.setSupplier(supplier);
            newInventory.setQuantity(quantity);
            newInventory.setCostPrice(costPrice);
            newInventory.setBatchNumber(batchNumber);
            newInventory.setLocation(location);
            newInventory.setStatus(Inventory.InventoryStatus.AVAILABLE);
            return inventoryRepository.save(newInventory);
        }
    }
    
    // Lấy tổng số lượng có sẵn của một sản phẩm từ tất cả nguồn
    @Transactional(readOnly = true)
    public Integer getTotalAvailableQuantity(Long productId) {
        Integer total = inventoryRepository.getTotalAvailableQuantityByProductId(productId);
        return total != null ? total : 0;
    }
    
    // Lấy tổng số lượng trong kho của một sản phẩm (bao gồm cả reserved)
    @Transactional(readOnly = true)
    public Integer getTotalQuantity(Long productId) {
        Integer total = inventoryRepository.getTotalQuantityByProductId(productId);
        return total != null ? total : 0;
    }
    
    // Lấy tổng số lượng đã đặt trước
    @Transactional(readOnly = true)
    public Integer getTotalReservedQuantity(Long productId) {
        Integer total = inventoryRepository.getTotalReservedQuantityByProductId(productId);
        return total != null ? total : 0;
    }
    
    // Lấy danh sách inventory theo product với thông tin chi tiết
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByProduct(Long productId) {
        return inventoryRepository.findByProductIdWithSupplier(productId);
    }
    
    // Lấy inventory có sẵn của một sản phẩm
    @Transactional(readOnly = true)
    public List<Inventory> getAvailableInventoryByProduct(Long productId) {
        return inventoryRepository.findAvailableInventoryByProductId(productId);
    }
    
    // Xuất hàng (giảm số lượng)
    public void releaseInventory(Long productId, Integer quantity) {
        log.info("Releasing {} units of product {}", quantity, productId);
        
        List<Inventory> availableInventories = inventoryRepository.findAvailableInventoryByProductId(productId);
        
        if (availableInventories.isEmpty()) {
            throw new RuntimeException("Không có hàng trong kho cho sản phẩm ID: " + productId);
        }
        
        int remainingQuantity = quantity;
        
        // Xuất hàng theo thứ tự FIFO (First In, First Out)
        availableInventories.sort((i1, i2) -> i1.getCreatedAt().compareTo(i2.getCreatedAt()));
        
        for (Inventory inventory : availableInventories) {
            if (remainingQuantity <= 0) break;
            
            int availableInThisInventory = inventory.getAvailableQuantity();
            int toRelease = Math.min(remainingQuantity, availableInThisInventory);
            
            inventory.removeQuantity(toRelease);
            inventoryRepository.save(inventory);
            
            remainingQuantity -= toRelease;
        }
        
        if (remainingQuantity > 0) {
            throw new RuntimeException("Không đủ hàng trong kho. Còn thiếu: " + remainingQuantity);
        }
    }
    
    // Đặt trước hàng
    public void reserveInventory(Long productId, Integer quantity) {
        log.info("Reserving {} units of product {}", quantity, productId);
        
        List<Inventory> availableInventories = inventoryRepository.findAvailableInventoryByProductId(productId);
        
        if (availableInventories.isEmpty()) {
            throw new RuntimeException("Không có hàng trong kho cho sản phẩm ID: " + productId);
        }
        
        int totalAvailable = availableInventories.stream()
            .mapToInt(Inventory::getAvailableQuantity)
            .sum();
        
        if (totalAvailable < quantity) {
            throw new RuntimeException("Không đủ hàng có sẵn để đặt trước. Yêu cầu: " + quantity + ", Có sẵn: " + totalAvailable);
        }
        
        int remainingQuantity = quantity;
        
        // Đặt trước theo thứ tự FIFO
        availableInventories.sort((i1, i2) -> i1.getCreatedAt().compareTo(i2.getCreatedAt()));
        
        for (Inventory inventory : availableInventories) {
            if (remainingQuantity <= 0) break;
            
            int availableInThisInventory = inventory.getAvailableQuantity();
            int toReserve = Math.min(remainingQuantity, availableInThisInventory);
            
            inventory.reserveQuantity(toReserve);
            inventoryRepository.save(inventory);
            
            remainingQuantity -= toReserve;
        }
    }
    
    // Lấy thống kê inventory theo product
    @Transactional(readOnly = true)
    public Map<String, Object> getInventoryStatistics(Long productId) {
        Integer totalQuantity = getTotalQuantity(productId);
        Integer totalAvailable = getTotalAvailableQuantity(productId);
        Integer totalReserved = getTotalReservedQuantity(productId);
        Long supplierCount = inventoryRepository.countDistinctSuppliersByProductId(productId);
        Long inventoryEntries = inventoryRepository.countByProductId(productId);
        
        return Map.of(
            "totalQuantity", totalQuantity,
            "totalAvailable", totalAvailable,
            "totalReserved", totalReserved,
            "supplierCount", supplierCount,
            "inventoryEntries", inventoryEntries
        );
    }
    
    // Cập nhật vị trí lưu trữ
    public void updateInventoryLocation(Long inventoryId, String newLocation) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
            .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));
        
        inventory.setLocation(newLocation);
        inventoryRepository.save(inventory);
    }
    
    // Đánh dấu inventory bị hỏng
    public void markInventoryAsDamaged(Long inventoryId, String notes) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
            .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));
        
        inventory.setStatus(Inventory.InventoryStatus.DAMAGED);
        inventory.setNotes(notes);
        inventoryRepository.save(inventory);
    }
    
    // Lấy danh sách inventory theo supplier
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryBySupplier(Long supplierId) {
        return inventoryRepository.findBySupplierId(supplierId);
    }
}
