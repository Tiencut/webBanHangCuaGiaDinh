package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllocationItemDto {
    private Long orderItemId;
    private Long productId;
    private Integer requestedQuantity;
    private List<InventoryAllocationDto> allocations;
}
