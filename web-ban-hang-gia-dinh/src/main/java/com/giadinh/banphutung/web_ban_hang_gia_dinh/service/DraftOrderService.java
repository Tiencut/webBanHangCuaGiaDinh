package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.DraftOrder;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.DraftOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DraftOrderService {

    private final DraftOrderRepository draftOrderRepository;

    public DraftOrder saveDraft(User user, String name, String payload) {
        DraftOrder d = new DraftOrder();
        d.setUser(user);
        d.setName(name);
        d.setPayload(payload);
        d.setLastSaved(LocalDateTime.now());
        return draftOrderRepository.save(d);
    }

    public List<DraftOrder> getDraftsForUser(User user) {
        return draftOrderRepository.findByUser(user);
    }

    public void deleteDraft(Long id) {
        draftOrderRepository.deleteById(id);
    }

    public DraftOrder updateDraft(Long id, String payload, String name) {
        var opt = draftOrderRepository.findById(id);
        if (opt.isEmpty()) return null;
        var d = opt.get();
        d.setPayload(payload);
        d.setName(name);
        d.setLastSaved(LocalDateTime.now());
        return draftOrderRepository.save(d);
    }
}
