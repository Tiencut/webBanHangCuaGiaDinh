package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/purchase-orders")
@Slf4j
@Tag(name = "Purchase Orders", description = "Stub endpoints for purchase orders")
public class PurchaseOrderController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        log.info("GET /api/purchase-orders - returning empty stub list");
        Map<String, Object> body = new HashMap<>();
        body.put("content", Collections.emptyList());
        body.put("totalElements", 0);
        body.put("page", 0);
        body.put("size", 0);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        log.info("GET /api/purchase-orders/{} - stub returning 404", id);
        return ResponseEntity.notFound().build();
    }
}
