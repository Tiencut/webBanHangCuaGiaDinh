package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.InventoryDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Inventory;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper.InventoryMapper;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.InventoryRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryMapper inventoryMapper;

    public List<InventoryDto> getAllInventories() {
        log.info("Fetching all inventories");
        List<Inventory> inventories = inventoryRepository.findByIsActiveTrue();
        return inventoryMapper.toDtoList(inventories);
    }

    public Page<InventoryDto> getInventoriesWithPagination(Pageable pageable) {
        log.info("Fetching inventories with pagination: {}", pageable);
        Page<Inventory> inventories = inventoryRepository.findByIsActiveTrue(pageable);
        return inventories.map(inventoryMapper::toDto);
    }

    public InventoryDto getInventoryById(Long id) {
        log.info("Fetching inventory by id: {}", id);
        Inventory inventory = inventoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));
        return inventoryMapper.toDto(inventory);
    }

    public InventoryDto getInventoryByProductAndSupplier(Long productId, Long supplierId) {
        log.info("Fetching inventory by product id: {} and supplier id: {}", productId, supplierId);
        Inventory inventory = inventoryRepository.findByProductIdAndSupplierIdAndIsActiveTrue(productId, supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + productId + " and supplier: " + supplierId));
        return inventoryMapper.toDto(inventory);
    }

    public InventoryDto createInventory(InventoryDto inventoryDto) {
        log.info("Creating new inventory for product: {} and supplier: {}", inventoryDto.getProductId(), inventoryDto.getSupplierId());
        
        // Validate product exists
        Product product = productRepository.findByIdAndIsActiveTrue(inventoryDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + inventoryDto.getProductId()));

        // Validate supplier exists
        Supplier supplier = supplierRepository.findByIdAndIsActiveTrue(inventoryDto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + inventoryDto.getSupplierId()));

        // Check if inventory already exists for this product-supplier combination
        Optional<Inventory> existingInventory = inventoryRepository.findByProductIdAndSupplierIdAndIsActiveTrue(
                inventoryDto.getProductId(), inventoryDto.getSupplierId());
        if (existingInventory.isPresent()) {
            throw new BusinessException("Inventory already exists for this product-supplier combination");
        }

        Inventory inventory = new Inventory();
        inventoryMapper.updateEntityFromDto(inventoryDto, inventory);
        inventory.setProduct(product);
        inventory.setSupplier(supplier);
        inventory.setIsActive(true);

        Inventory savedInventory = inventoryRepository.save(inventory);
        log.info("Inventory created successfully with id: {}", savedInventory.getId());
        return inventoryMapper.toDto(savedInventory);
    }

    public InventoryDto updateInventory(Long id, InventoryDto inventoryDto) {
        log.info("Updating inventory with id: {}", id);
        Inventory inventory = inventoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));

        // Validate product exists if changed
        if (inventoryDto.getProductId() != null && !inventoryDto.getProductId().equals(inventory.getProduct().getId())) {
            Product product = productRepository.findByIdAndIsActiveTrue(inventoryDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + inventoryDto.getProductId()));
            inventory.setProduct(product);
        }

        // Validate supplier exists if changed
        if (inventoryDto.getSupplierId() != null && !inventoryDto.getSupplierId().equals(inventory.getSupplier().getId())) {
            Supplier supplier = supplierRepository.findByIdAndIsActiveTrue(inventoryDto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + inventoryDto.getSupplierId()));
            inventory.setSupplier(supplier);
        }

        inventoryMapper.updateEntityFromDto(inventoryDto, inventory);
        Inventory updatedInventory = inventoryRepository.save(inventory);
        log.info("Inventory updated successfully with id: {}", updatedInventory.getId());
        return inventoryMapper.toDto(updatedInventory);
    }

    public void deleteInventory(Long id) {
        log.info("Deleting inventory with id: {}", id);
        Inventory inventory = inventoryRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));
        
        inventory.setIsActive(false);
        inventoryRepository.save(inventory);
        log.info("Inventory deleted successfully with id: {}", id);
    }

    public List<InventoryDto> getInventoriesByProduct(Long productId) {
        log.info("Fetching inventories by product: {}", productId);
        List<Inventory> inventories = inventoryRepository.findByProductIdAndIsActiveTrue(productId);
        return inventoryMapper.toDtoList(inventories);
    }

    public List<InventoryDto> getInventoriesBySupplier(Long supplierId) {
        log.info("Fetching inventories by supplier: {}", supplierId);
        List<Inventory> inventories = inventoryRepository.findBySupplierIdAndIsActiveTrue(supplierId);
        return inventoryMapper.toDtoList(inventories);
    }

    public List<InventoryDto> getLowStockInventories(Integer threshold) {
        log.info("Fetching low stock inventories with threshold: {}", threshold);
        List<Inventory> inventories = inventoryRepository.findByAvailableQuantityLessThanEqualAndIsActiveTrue(threshold);
        return inventoryMapper.toDtoList(inventories);
    }

    public List<InventoryDto> getOutOfStockInventories() {
        log.info("Fetching out of stock inventories");
        List<Inventory> inventories = inventoryRepository.findByAvailableQuantityLessThanEqualAndIsActiveTrue(0);
        return inventoryMapper.toDtoList(inventories);
    }

    public void stockIn(Long inventoryId, Integer quantity, BigDecimal unitCost, String batchNumber) {
        log.info("Stocking in inventory id: {} with quantity: {}", inventoryId, quantity);
        Inventory inventory = inventoryRepository.findByIdAndIsActiveTrue(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId));
        
        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        inventory.setLastStockIn(LocalDateTime.now());
        
        // Update average cost
        if (inventory.getAverageCost() == null) {
            inventory.setAverageCost(unitCost);
        } else {
            BigDecimal totalValue = inventory.getAverageCost().multiply(BigDecimal.valueOf(inventory.getQuantity() - quantity))
                    .add(unitCost.multiply(BigDecimal.valueOf(quantity)));
            inventory.setAverageCost(totalValue.divide(BigDecimal.valueOf(inventory.getQuantity()), 2, BigDecimal.ROUND_HALF_UP));
        }
        
        inventory.setLastPurchasePrice(unitCost);
        if (batchNumber != null) {
            inventory.setBatchNumber(batchNumber);
        }
        
        inventoryRepository.save(inventory);
        log.info("Stock in completed successfully");
    }

    public void stockOut(Long inventoryId, Integer quantity) {
        log.info("Stocking out inventory id: {} with quantity: {}", inventoryId, quantity);
        Inventory inventory = inventoryRepository.findByIdAndIsActiveTrue(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId));
        
        if (inventory.getAvailableQuantity() < quantity) {
            throw new BusinessException("Insufficient stock. Available: " + inventory.getAvailableQuantity() + ", Requested: " + quantity);
        }
        
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventory.setLastStockOut(LocalDateTime.now());
        
        inventoryRepository.save(inventory);
        log.info("Stock out completed successfully");
    }

    public void reserveStock(Long inventoryId, Integer quantity) {
        log.info("Reserving stock for inventory id: {} with quantity: {}", inventoryId, quantity);
        Inventory inventory = inventoryRepository.findByIdAndIsActiveTrue(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId));
        
        if (inventory.getAvailableQuantity() < quantity) {
            throw new BusinessException("Insufficient available stock. Available: " + inventory.getAvailableQuantity() + ", Requested: " + quantity);
        }
        
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        
        inventoryRepository.save(inventory);
        log.info("Stock reserved successfully");
    }

    public void releaseReservedStock(Long inventoryId, Integer quantity) {
        log.info("Releasing reserved stock for inventory id: {} with quantity: {}", inventoryId, quantity);
        Inventory inventory = inventoryRepository.findByIdAndIsActiveTrue(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId));
        
        if (inventory.getReservedQuantity() < quantity) {
            throw new BusinessException("Insufficient reserved stock. Reserved: " + inventory.getReservedQuantity() + ", Requested: " + quantity);
        }
        
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        
        inventoryRepository.save(inventory);
        log.info("Reserved stock released successfully");
    }

    public List<InventoryDto> searchInventories(String keyword) {
        log.info("Searching inventories with keyword: {}", keyword);
        List<Inventory> inventories = inventoryRepository.searchInventories(keyword);
        return inventoryMapper.toDtoList(inventories);
    }

    public List<InventoryDto> getInventoriesByLocation(String location) {
        log.info("Fetching inventories by location: {}", location);
        List<Inventory> inventories = inventoryRepository.findByLocationContainingIgnoreCaseAndIsActiveTrue(location);
        return inventoryMapper.toDtoList(inventories);
    }

    public List<InventoryDto> getExpiringInventories(int daysThreshold) {
        log.info("Fetching inventories expiring within {} days", daysThreshold);
        List<Inventory> inventories = inventoryRepository.findExpiringInventories(daysThreshold);
        return inventoryMapper.toDtoList(inventories);
    }
} 