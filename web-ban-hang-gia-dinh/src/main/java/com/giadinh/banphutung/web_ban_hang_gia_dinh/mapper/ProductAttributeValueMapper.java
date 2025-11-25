package com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.ProductAttributeValueDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductAttributeValue;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductAttributeValueMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "attributeId", source = "attribute.id")
    @Mapping(target = "attributeName", source = "attribute.name")
    @Mapping(target = "unit", source = "attribute.unit")
    @Mapping(target = "type", source = "attribute.type")
    ProductAttributeValueDto toDto(ProductAttributeValue entity);
    List<ProductAttributeValueDto> toDtoList(List<ProductAttributeValue> entities);
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @InheritInverseConfiguration
    ProductAttributeValue toEntity(ProductAttributeValueDto dto);
}