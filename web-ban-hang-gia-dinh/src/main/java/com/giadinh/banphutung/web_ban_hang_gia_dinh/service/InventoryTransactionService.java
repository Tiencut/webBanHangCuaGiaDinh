package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.InventoryTransactionDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.InventoryTransaction;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper.InventoryTransactionMapper;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryTransactionService {
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final InventoryTransactionMapper inventoryTransactionMapper;

    public List<InventoryTransactionDto> getTransactionsByProduct(Long productId) {
        List<InventoryTransaction> list = inventoryTransactionRepository.findByProductIdOrderByTransDateDesc(productId);
        return inventoryTransactionMapper.toDtoList(list);
    }
} 