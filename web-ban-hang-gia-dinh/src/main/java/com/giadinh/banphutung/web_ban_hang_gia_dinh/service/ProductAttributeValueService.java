package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.ProductAttributeValueDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductAttributeValue;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper.ProductAttributeValueMapper;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductAttributeValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductAttributeValueService {
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final ProductAttributeValueMapper productAttributeValueMapper;

    public List<ProductAttributeValueDto> getAttributeValuesByProduct(Long productId) {
        List<ProductAttributeValue> list = productAttributeValueRepository.findByProductId(productId);
        return productAttributeValueMapper.toDtoList(list);
    }
} 