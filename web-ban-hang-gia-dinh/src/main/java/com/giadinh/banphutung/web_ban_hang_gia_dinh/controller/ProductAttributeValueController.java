package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.ProductAttributeValueDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.ProductAttributeValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product-attribute-values")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Attribute Value", description = "APIs for product attribute values")
public class ProductAttributeValueController {
    private final ProductAttributeValueService productAttributeValueService;

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get product attribute values by product", description = "Retrieve product attribute values for a specific product")
    public ResponseEntity<List<ProductAttributeValueDto>> getAttributeValuesByProduct(@PathVariable Long productId) {
        log.info("GET /api/product-attribute-values/product/{} - Fetching product attribute values", productId);
        List<ProductAttributeValueDto> list = productAttributeValueService.getAttributeValuesByProduct(productId);
        return ResponseEntity.ok(list);
    }
} 