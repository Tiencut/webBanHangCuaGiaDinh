package com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.UserDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    
    @Mapping(target = "password", ignore = true)
    UserDto toDto(User user);
    
    List<UserDto> toDtoList(List<User> users);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDto(UserDto dto, @MappingTarget User user);
} 