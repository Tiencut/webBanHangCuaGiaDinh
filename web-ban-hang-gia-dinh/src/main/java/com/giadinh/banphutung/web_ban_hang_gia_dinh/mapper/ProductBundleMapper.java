package com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.ProductBundleDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.ProductBundle;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductBundleMapper {
    
    @Mapping(source = "parentProduct.id", target = "parentProductId")
    @Mapping(source = "parentProduct.name", target = "parentProductName")
    @Mapping(source = "childProduct.id", target = "childProductId")
    @Mapping(source = "childProduct.name", target = "childProductName")
    @Mapping(source = "childProduct.code", target = "childProductCode")
    @Mapping(source = "defaultSubstitute.id", target = "defaultSubstituteId")
    @Mapping(source = "defaultSubstitute.name", target = "defaultSubstituteName")
    ProductBundleDto toDto(ProductBundle productBundle);
    
    List<ProductBundleDto> toDtoList(List<ProductBundle> productBundles);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentProduct", ignore = true)
    @Mapping(target = "childProduct", ignore = true)
    @Mapping(target = "defaultSubstitute", ignore = true)
    @Mapping(target = "substituteProducts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDto(ProductBundleDto dto, @MappingTarget ProductBundle productBundle);
} 