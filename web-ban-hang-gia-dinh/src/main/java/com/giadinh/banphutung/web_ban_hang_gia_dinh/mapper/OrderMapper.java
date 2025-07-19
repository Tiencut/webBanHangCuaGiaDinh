package com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.OrderDetailDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.OrderDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.OrderDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {OrderDetailMapper.class})
public interface OrderMapper {
    
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "staff.id", target = "staffId")
    @Mapping(source = "staff.fullName", target = "staffName")
    @Mapping(source = "orderDetails", target = "orderDetails")
    OrderDto toDto(Order order);
    
    List<OrderDto> toDtoList(List<Order> orders);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDto(OrderDto dto, @MappingTarget Order order);
} 