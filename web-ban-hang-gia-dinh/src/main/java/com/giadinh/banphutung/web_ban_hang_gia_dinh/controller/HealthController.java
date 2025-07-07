package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "OK",
            "message", "Backend is running",
            "timestamp", System.currentTimeMillis()
        );
    }
    
    @GetMapping("/test")
    public Map<String, Object> test() {
        return Map.of(
            "message", "Test endpoint works",
            "backend", "Spring Boot",
            "status", "success"
        );
    }
}
