package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.ProductAttributeDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.ProductAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product-attributes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Attribute", description = "APIs for product attributes")
public class ProductAttributeController {
    private final ProductAttributeService productAttributeService;

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get product attributes by category", description = "Retrieve product attributes for a specific category")
    public ResponseEntity<List<ProductAttributeDto>> getAttributesByCategory(@PathVariable Long categoryId) {
        log.info("GET /api/product-attributes/category/{} - Fetching product attributes", categoryId);
        List<ProductAttributeDto> list = productAttributeService.getAttributesByCategory(categoryId);
        return ResponseEntity.ok(list);
    }
} 