package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Long> {
    List<ProductAttributeValue> findByProductId(Long productId);
    List<ProductAttributeValue> findByProductIdAndAttributeId(Long productId, Long attributeId);
} 