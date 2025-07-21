package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.ProductAttributeDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductAttribute;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper.ProductAttributeMapper;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.ProductAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductAttributeService {
    private final ProductAttributeRepository productAttributeRepository;
    private final ProductAttributeMapper productAttributeMapper;

    public List<ProductAttributeDto> getAttributesByCategory(Long categoryId) {
        List<ProductAttribute> list = productAttributeRepository.findByCategoryId(categoryId);
        return productAttributeMapper.toDtoList(list);
    }
} 