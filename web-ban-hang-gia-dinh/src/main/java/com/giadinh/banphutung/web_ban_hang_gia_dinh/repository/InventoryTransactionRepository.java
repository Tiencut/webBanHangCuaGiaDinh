package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    List<InventoryTransaction> findByProductIdOrderByTransDateDesc(Long productId);
} 