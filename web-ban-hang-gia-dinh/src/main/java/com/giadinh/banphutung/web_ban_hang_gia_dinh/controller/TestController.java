package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Web Ban Hang Gia Dinh API is running!");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "Web Ban Hang Gia Dinh");
        info.put("version", "1.0.0");
        info.put("description", "Hệ thống quản lý bán hàng đa nhà cung cấp");
        info.put("features", new String[]{
            "Quản lý sản phẩm",
            "Quản lý khách hàng", 
            "Quản lý đơn hàng",
            "Quản lý nhà cung cấp",
            "Quản lý kho",
            "Tương thích xe"
        });
        return info;
    }
} 