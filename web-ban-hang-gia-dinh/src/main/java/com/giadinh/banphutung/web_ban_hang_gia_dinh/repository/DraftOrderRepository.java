package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.DraftOrder;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftOrderRepository extends JpaRepository<DraftOrder, Long> {
    List<DraftOrder> findByUser(User user);
}
