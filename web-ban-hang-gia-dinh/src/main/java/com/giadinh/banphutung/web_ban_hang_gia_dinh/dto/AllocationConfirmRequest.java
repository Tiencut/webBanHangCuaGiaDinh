package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllocationConfirmRequest {
    private List<AllocationItemDto> allocations;
}
