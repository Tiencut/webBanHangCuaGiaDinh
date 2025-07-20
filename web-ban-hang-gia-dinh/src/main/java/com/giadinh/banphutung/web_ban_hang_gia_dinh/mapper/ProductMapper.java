package com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.CreateProductRequest;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.ProductDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "compatibleVehicleIds", expression = "java(mapVehicleIds(product.getCompatibleVehicles()))")
    ProductDto toDto(Product product);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "compatibleVehicles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Product toEntity(CreateProductRequest request);
    
    List<ProductDto> toDtoList(List<Product> products);
    
    default List<Long> mapVehicleIds(List<VehicleModel> vehicles) {
        if (vehicles == null) return null;
        return vehicles.stream()
                .map(VehicleModel::getId)
                .toList();
    }
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "compatibleVehicles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromRequest(CreateProductRequest request, @MappingTarget Product product);
} 