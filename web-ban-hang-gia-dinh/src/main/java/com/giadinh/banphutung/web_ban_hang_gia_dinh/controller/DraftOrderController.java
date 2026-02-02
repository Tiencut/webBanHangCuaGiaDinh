package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.DraftOrder;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.UserRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.DraftOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/drafts")
@RequiredArgsConstructor
@Slf4j
public class DraftOrderController {

    private final DraftOrderService draftOrderService;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        String username = (String) auth.getPrincipal();
        return userRepository.findByUsernameAndIsActiveTrue(username).orElse(null);
    }

    @PostMapping
    public ResponseEntity<DraftOrder> saveDraft(@RequestBody DraftOrder payload) {
        User u = getCurrentUser();
        DraftOrder saved = draftOrderService.saveDraft(u, payload.getName(), payload.getPayload());
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<DraftOrder>> getDrafts() {
        User u = getCurrentUser();
        if (u == null) return ResponseEntity.ok(List.of());
        var list = draftOrderService.getDraftsForUser(u);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDraft(@PathVariable Long id) {
        draftOrderService.deleteDraft(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DraftOrder> updateDraft(@PathVariable Long id, @RequestBody DraftOrder payload) {
        var updated = draftOrderService.updateDraft(id, payload.getPayload(), payload.getName());
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }
}
