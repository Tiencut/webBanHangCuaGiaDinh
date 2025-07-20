package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.InventoryTransactionDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.InventoryTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory-transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventory Transaction", description = "APIs for inventory transaction history")
public class InventoryTransactionController {
    private final InventoryTransactionService inventoryTransactionService;

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get inventory transaction history by product", description = "Retrieve inventory transaction history for a specific product")
    public ResponseEntity<List<InventoryTransactionDto>> getTransactionsByProduct(@PathVariable Long productId) {
        log.info("GET /api/inventory-transactions/product/{} - Fetching inventory transaction history", productId);
        List<InventoryTransactionDto> list = inventoryTransactionService.getTransactionsByProduct(productId);
        return ResponseEntity.ok(list);
    }
} 