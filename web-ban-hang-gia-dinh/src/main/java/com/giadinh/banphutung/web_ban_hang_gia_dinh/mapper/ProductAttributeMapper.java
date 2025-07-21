package com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.ProductAttributeDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductAttribute;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "type", source = "type")
    ProductAttributeDto toDto(ProductAttribute entity);
    List<ProductAttributeDto> toDtoList(List<ProductAttribute> entities);
    @InheritInverseConfiguration
    ProductAttribute toEntity(ProductAttributeDto dto);
} 