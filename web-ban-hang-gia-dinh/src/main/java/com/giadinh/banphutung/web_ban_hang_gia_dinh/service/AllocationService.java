package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.AllocationItemDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.AllocationResponseDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Inventory;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.InventoryTransaction;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Order;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.OrderDetail;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.InventoryRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.InventoryTransactionRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AllocationService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;
    private final InventoryTransactionRepository inventoryTransactionRepository;

    /**
     * Build allocation suggestions for an order (auto-pick by available quantity desc).
     */
    public AllocationResponseDto allocate(Long orderId) {
        Order order = orderRepository.findByIdAndIsDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        List<AllocationItemDto> items = new ArrayList<>();

        for (OrderDetail od : order.getOrderDetails()) {
            Long productId = od.getProduct().getId();
            Integer need = od.getQuantity() != null ? od.getQuantity() : 0;

            AllocationItemDto itemDto = new AllocationItemDto();
            itemDto.setOrderItemId(od.getId());
            itemDto.setProductId(productId);
            itemDto.setRequestedQuantity(need);

            List<com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.InventoryAllocationDto> allocs = new ArrayList<>();

            // fetch inventories sorted by current quantity desc
            List<Inventory> invs = inventoryRepository.findByProductIdAndIsActiveTrueOrderByCurrentQuantityDesc(productId);

            int remaining = need;
            for (Inventory inv : invs) {
                if (remaining <= 0) break;
                int avail = inv.getAvailableQuantity() != null ? inv.getAvailableQuantity() : Math.max(0, inv.getCurrentQuantity() - inv.getCommittedQuantity() - inv.getReservedQuantity());
                if (avail <= 0) continue;

                int take = Math.min(avail, remaining);
                com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.InventoryAllocationDto ia =
                        new com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.InventoryAllocationDto(
                                inv.getId(), inv.getSupplier().getId(), inv.getSupplier().getName(), avail, take
                        );
                allocs.add(ia);
                remaining -= take;
            }

            itemDto.setAllocations(allocs);
            items.add(itemDto);
        }

        return new AllocationResponseDto(items);
    }

    /**
     * Commit allocation (reserve quantities) - transactional.
     */
    @Transactional
    public void commitAllocation(Long orderId, List<AllocationItemDto> allocations) {
        // For each allocation, call inventoryService.reserveStock and write transaction
        for (AllocationItemDto item : allocations) {
            if (item.getAllocations() == null) continue;
            for (com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.InventoryAllocationDto a : item.getAllocations()) {
                if (a.getSuggestedQuantity() == null || a.getSuggestedQuantity() <= 0) continue;
                // reserve via InventoryService
                inventoryService.reserveStock(a.getInventoryId(), a.getSuggestedQuantity());

                // load inventory to capture product/supplier and ending stocks
                Inventory inv = inventoryRepository.findByIdAndIsActiveTrue(a.getInventoryId()).orElse(null);

                // create transaction record
                InventoryTransaction tx = new InventoryTransaction();
                if (inv != null) {
                    tx.setProduct(inv.getProduct());
                    tx.setSupplier(inv.getSupplier());
                    Integer av = inv.getAvailableQuantity();
                    if (av == null) {
                        int calc = Math.max(0, (inv.getCurrentQuantity() != null ? inv.getCurrentQuantity() : 0)
                                - (inv.getCommittedQuantity() != null ? inv.getCommittedQuantity() : 0)
                                - (inv.getReservedQuantity() != null ? inv.getReservedQuantity() : 0));
                        tx.setEndingStocks(calc);
                    } else {
                        tx.setEndingStocks(av);
                    }
                }
                tx.setTransactionType(InventoryTransaction.TransactionType.ADJUST);
                tx.setDocumentCode("ORDER-" + orderId);
                tx.setDocumentType("ORDER_ALLOCATION");
                tx.setPartnerName(null);
                tx.setTransDate(LocalDateTime.now());
                tx.setQuantity(a.getSuggestedQuantity());
                inventoryTransactionRepository.save(tx);
            }
        }
    }
}
