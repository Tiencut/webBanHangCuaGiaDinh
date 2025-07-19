package com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.CreateProductRequest;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.ProductDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "compatibleVehicles", target = "compatibleVehicleIds", 
            qualifiedByName = "extractVehicleIds")
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
    
    @Named("extractVehicleIds")
    default List<Long> extractVehicleIds(List<Object> vehicles) {
        if (vehicles == null) return null;
        return vehicles.stream()
                .map(vehicle -> {
                    try {
                        return (Long) vehicle.getClass().getMethod("getId").invoke(vehicle);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(id -> id != null)
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