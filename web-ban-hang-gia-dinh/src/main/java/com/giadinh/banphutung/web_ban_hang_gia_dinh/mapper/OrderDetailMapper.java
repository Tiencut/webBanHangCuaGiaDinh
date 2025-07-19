package com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.OrderDetailDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.OrderDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDetailMapper {
    
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.code", target = "productCode")
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    OrderDetailDto toDto(OrderDetail orderDetail);
    
    List<OrderDetailDto> toDtoList(List<OrderDetail> orderDetails);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDto(OrderDetailDto dto, @MappingTarget OrderDetail orderDetail);
} 