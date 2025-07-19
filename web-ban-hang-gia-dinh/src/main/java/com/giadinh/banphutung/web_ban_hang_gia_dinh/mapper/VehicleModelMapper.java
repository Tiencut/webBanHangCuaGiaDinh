package com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.VehicleModelDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleModelMapper {
    
    VehicleModelDto toDto(VehicleModel vehicleModel);
    
    List<VehicleModelDto> toDtoList(List<VehicleModel> vehicleModels);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDto(VehicleModelDto dto, @MappingTarget VehicleModel vehicleModel);
} 