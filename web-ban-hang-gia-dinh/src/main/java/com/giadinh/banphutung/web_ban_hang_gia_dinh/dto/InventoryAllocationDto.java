package com.giadinh.banphutung.web_ban_hang_gia_dinh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAllocationDto {
    private Long inventoryId;
    private Long supplierId;
    private String supplierName;
    private Integer quantityAvailable;
    private Integer suggestedQuantity;
}
