package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier.SupplierStatus;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SupplierService {
    
    private final SupplierRepository supplierRepository;
    
    // Tạo supplier mới
    public Supplier createSupplier(Supplier supplier) {
        log.info("Creating new supplier: {}", supplier.getName());
        
        // Kiểm tra code đã tồn tại
        if (supplier.getCode() != null && supplierRepository.existsByCode(supplier.getCode())) {
            throw new RuntimeException("Supplier code đã tồn tại: " + supplier.getCode());
        }
        
        // Kiểm tra phone đã tồn tại
        if (supplier.getPhone() != null && supplierRepository.existsByPhone(supplier.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại: " + supplier.getPhone());
        }
        
        // Kiểm tra email đã tồn tại
        if (supplier.getEmail() != null && supplierRepository.existsByEmail(supplier.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + supplier.getEmail());
        }
        
        // Tự động tạo code nếu chưa có
        if (supplier.getCode() == null || supplier.getCode().trim().isEmpty()) {
            supplier.setCode(generateSupplierCode(supplier.getName()));
        }
        
        return supplierRepository.save(supplier);
    }
    
    // Tìm supplier theo ID
    @Transactional(readOnly = true)
    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }
    
    // Tìm supplier theo code
    @Transactional(readOnly = true)
    public Optional<Supplier> findByCode(String code) {
        return supplierRepository.findByCode(code);
    }
    
    // Tìm supplier theo phone
    @Transactional(readOnly = true)
    public Optional<Supplier> findByPhone(String phone) {
        return supplierRepository.findByPhone(phone);
    }
    
    // Tìm tất cả supplier active
    @Transactional(readOnly = true)
    public List<Supplier> findAllActiveSuppliers() {
        return supplierRepository.findByStatusOrderByNameAsc(SupplierStatus.ACTIVE);
    }
    
    // Tìm supplier theo rating tối thiểu
    @Transactional(readOnly = true)
    public List<Supplier> findByMinRating(Double minRating) {
        return supplierRepository.findByRatingGreaterThanEqual(minRating);
    }
    
    // Tìm supplier theo thời gian giao hàng
    @Transactional(readOnly = true)
    public List<Supplier> findByMaxDeliveryTime(Integer maxDays) {
        return supplierRepository.findByDeliveryTimeDaysLessThanEqualOrderByDeliveryTimeDaysAsc(maxDays);
    }
    
    // Tìm kiếm supplier theo tên
    @Transactional(readOnly = true)
    public List<Supplier> searchSuppliers(String searchTerm) {
        return supplierRepository.findByNameOrCodeContainingIgnoreCase(searchTerm);
    }
    
    // Cập nhật supplier
    public Supplier updateSupplier(Long id, Supplier supplierUpdate) {
        log.info("Updating supplier with id: {}", id);
        
        Supplier existingSupplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        
        // Cập nhật thông tin cơ bản
        existingSupplier.setName(supplierUpdate.getName());
        existingSupplier.setAddress(supplierUpdate.getAddress());
        existingSupplier.setContactPerson(supplierUpdate.getContactPerson());
        existingSupplier.setTaxCode(supplierUpdate.getTaxCode());
        existingSupplier.setBankAccount(supplierUpdate.getBankAccount());
        existingSupplier.setBankName(supplierUpdate.getBankName());
        existingSupplier.setPaymentTerms(supplierUpdate.getPaymentTerms());
        existingSupplier.setCreditLimit(supplierUpdate.getCreditLimit());
        existingSupplier.setDeliveryTimeDays(supplierUpdate.getDeliveryTimeDays());
        existingSupplier.setNotes(supplierUpdate.getNotes());
        
        // Cập nhật code nếu khác
        if (!existingSupplier.getCode().equals(supplierUpdate.getCode())) {
            if (supplierRepository.existsByCode(supplierUpdate.getCode())) {
                throw new RuntimeException("Supplier code đã tồn tại: " + supplierUpdate.getCode());
            }
            existingSupplier.setCode(supplierUpdate.getCode());
        }
        
        // Cập nhật phone nếu khác
        if (supplierUpdate.getPhone() != null && 
            !supplierUpdate.getPhone().equals(existingSupplier.getPhone())) {
            if (supplierRepository.existsByPhone(supplierUpdate.getPhone())) {
                throw new RuntimeException("Số điện thoại đã tồn tại: " + supplierUpdate.getPhone());
            }
            existingSupplier.setPhone(supplierUpdate.getPhone());
        }
        
        // Cập nhật email nếu khác
        if (supplierUpdate.getEmail() != null && 
            !supplierUpdate.getEmail().equals(existingSupplier.getEmail())) {
            if (supplierRepository.existsByEmail(supplierUpdate.getEmail())) {
                throw new RuntimeException("Email đã tồn tại: " + supplierUpdate.getEmail());
            }
            existingSupplier.setEmail(supplierUpdate.getEmail());
        }
        
        return supplierRepository.save(existingSupplier);
    }
    
    // Cập nhật rating supplier
    public Supplier updateSupplierRating(Long id, Double rating) {
        log.info("Updating rating for supplier id: {} to {}", id, rating);
        
        if (rating < 0.0 || rating > 5.0) {
            throw new RuntimeException("Rating phải trong khoảng 0.0 - 5.0");
        }
        
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        
        supplier.setRating(rating);
        return supplierRepository.save(supplier);
    }
    
    // Khóa/mở khóa supplier
    public void toggleSupplierStatus(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        
        if (supplier.getStatus() == SupplierStatus.ACTIVE) {
            supplier.setStatus(SupplierStatus.INACTIVE);
        } else if (supplier.getStatus() == SupplierStatus.INACTIVE) {
            supplier.setStatus(SupplierStatus.ACTIVE);
        }
        
        supplierRepository.save(supplier);
        log.info("Supplier {} status changed to: {}", supplier.getName(), supplier.getStatus());
    }
    
    // Blacklist supplier
    public void blacklistSupplier(Long id, String reason) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        
        supplier.setStatus(SupplierStatus.BLACKLISTED);
        supplier.setNotes(supplier.getNotes() + "\n[BLACKLISTED] " + reason);
        
        supplierRepository.save(supplier);
        log.warn("Supplier {} has been blacklisted: {}", supplier.getName(), reason);
    }
    
    // Lấy top supplier theo rating
    @Transactional(readOnly = true)
    public List<Supplier> getTopSuppliersByRating(int limit) {
        return supplierRepository.findTopSuppliersByRating(
            org.springframework.data.domain.PageRequest.of(0, limit));
    }
    
    // Lấy thống kê supplier
    @Transactional(readOnly = true)
    public SupplierStats getSupplierStats() {
        Long totalSuppliers = supplierRepository.count();
        Long activeSuppliers = supplierRepository.countByStatus(SupplierStatus.ACTIVE);
        Long inactiveSuppliers = supplierRepository.countByStatus(SupplierStatus.INACTIVE);
        Long blacklistedSuppliers = supplierRepository.countByStatus(SupplierStatus.BLACKLISTED);
        
        return new SupplierStats(totalSuppliers, activeSuppliers, inactiveSuppliers, blacklistedSuppliers);
    }
    
    // ========== CRUD Methods cơ bản ==========
    
    // Lấy tất cả suppliers
    @Transactional(readOnly = true)
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }
    
    // Lấy suppliers với phân trang
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<Supplier> findAll(org.springframework.data.domain.Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }
    
    // Lưu supplier (create/update)
    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }
    
    // Kiểm tra supplier tồn tại
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return supplierRepository.existsById(id);
    }
    
    // Xóa supplier theo ID
    public void deleteById(Long id) {
        supplierRepository.deleteById(id);
    }
    
    // Đếm số lượng supplier
    @Transactional(readOnly = true)
    public long count() {
        return supplierRepository.count();
    }
    
    // Tìm kiếm supplier theo tên
    @Transactional(readOnly = true)
    public List<Supplier> findByNameContaining(String name) {
        return supplierRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Tìm kiếm supplier theo code
    @Transactional(readOnly = true)
    public List<Supplier> findByCodeContaining(String code) {
        return supplierRepository.findByCodeContainingIgnoreCase(code);
    }
    
    // Tìm supplier theo vehicle brands
    @Transactional(readOnly = true)
    public List<Supplier> findByVehicleBrandsContaining(String brand) {
        return supplierRepository.findByVehicleBrandsContainingIgnoreCase(brand);
    }
    
    // Tìm supplier active
    @Transactional(readOnly = true)
    public List<Supplier> findActiveSuppliers() {
        return supplierRepository.findByStatusOrderByNameAsc(SupplierStatus.ACTIVE);
    }
    
    // Tìm top suppliers
    @Transactional(readOnly = true)
    public List<Supplier> findTopSuppliers(int limit) {
        return supplierRepository.findTopSuppliers(org.springframework.data.domain.PageRequest.of(0, limit));
    }
    
    // ========== End CRUD Methods ==========
    
    // Helper methods
    private String generateSupplierCode(String name) {
        // Tạo code từ tên (lấy 3 ký tự đầu + số)
        String prefix = name.toLowerCase()
                           .replaceAll("[^a-z0-9]", "")
                           .substring(0, Math.min(3, name.length()));
        
        String code = "NCC" + prefix.toUpperCase() + String.format("%04d", 
            supplierRepository.count() + 1);
        
        // Đảm bảo unique
        int counter = 1;
        String originalCode = code;
        while (supplierRepository.existsByCode(code)) {
            code = originalCode + "_" + counter++;
        }
        
        return code;
    }
    
    // Inner class for stats
    public static class SupplierStats {
        public final Long totalSuppliers;
        public final Long activeSuppliers;
        public final Long inactiveSuppliers;
        public final Long blacklistedSuppliers;
        
        public SupplierStats(Long totalSuppliers, Long activeSuppliers, 
                           Long inactiveSuppliers, Long blacklistedSuppliers) {
            this.totalSuppliers = totalSuppliers;
            this.activeSuppliers = activeSuppliers;
            this.inactiveSuppliers = inactiveSuppliers;
            this.blacklistedSuppliers = blacklistedSuppliers;
        }
    }
}
