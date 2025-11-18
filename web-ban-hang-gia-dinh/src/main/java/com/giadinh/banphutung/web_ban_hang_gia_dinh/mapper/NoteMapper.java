package com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.NoteDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Note;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoteMapper {

    @Mappings({
            @Mapping(source = "user.id", target = "userId")
    })
    NoteDto toDto(Note note);

    List<NoteDto> toDtoList(List<Note> notes);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "isActive", ignore = true),
            @Mapping(target = "user", ignore = true)
    })
    void updateEntityFromDto(NoteDto dto, @MappingTarget Note note);
}

