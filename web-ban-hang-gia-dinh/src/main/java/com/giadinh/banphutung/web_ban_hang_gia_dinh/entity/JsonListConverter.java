package com.giadinh.banphutung.web_ban_hang_gia_dinh.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

@Converter(autoApply = false)
public class JsonListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            if (attribute == null) return "[]";
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.trim().isEmpty()) return Collections.emptyList();
            return objectMapper.readValue(dbData, new TypeReference<List<String>>(){});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
