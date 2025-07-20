package com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.InventoryTransactionDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.InventoryTransaction;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryTransactionMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "transactionType", source = "transactionType")
    InventoryTransactionDto toDto(InventoryTransaction entity);

    List<InventoryTransactionDto> toDtoList(List<InventoryTransaction> entities);

    @InheritInverseConfiguration
    InventoryTransaction toEntity(InventoryTransactionDto dto);
} 