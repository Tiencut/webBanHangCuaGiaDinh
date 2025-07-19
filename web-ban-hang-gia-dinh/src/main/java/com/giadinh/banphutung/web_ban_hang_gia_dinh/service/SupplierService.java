package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.SupplierDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Supplier;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper.SupplierMapper;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public List<SupplierDto> getAllSuppliers() {
        log.info("Fetching all suppliers");
        List<Supplier> suppliers = supplierRepository.findByIsActiveTrue();
        return supplierMapper.toDtoList(suppliers);
    }

    public Page<SupplierDto> getSuppliersWithPagination(Pageable pageable) {
        log.info("Fetching suppliers with pagination: {}", pageable);
        Page<Supplier> suppliers = supplierRepository.findByIsActiveTrue(pageable);
        return suppliers.map(supplierMapper::toDto);
    }

    public SupplierDto getSupplierById(Long id) {
        log.info("Fetching supplier by id: {}", id);
        Supplier supplier = supplierRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return supplierMapper.toDto(supplier);
    }

    public SupplierDto getSupplierByCode(String code) {
        log.info("Fetching supplier by code: {}", code);
        Supplier supplier = supplierRepository.findByCodeAndIsActiveTrue(code)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with code: " + code));
        return supplierMapper.toDto(supplier);
    }

    public SupplierDto createSupplier(SupplierDto supplierDto) {
        log.info("Creating new supplier: {}", supplierDto.getName());
        
        // Check if supplier code already exists
        if (supplierDto.getCode() != null && !supplierDto.getCode().trim().isEmpty()) {
            Optional<Supplier> existingSupplier = supplierRepository.findByCodeAndIsActiveTrue(supplierDto.getCode());
            if (existingSupplier.isPresent()) {
                throw new BusinessException("Supplier code already exists: " + supplierDto.getCode());
            }
        }

        Supplier supplier = new Supplier();
        supplierMapper.updateEntityFromDto(supplierDto, supplier);
        supplier.setIsActive(true);

        Supplier savedSupplier = supplierRepository.save(supplier);
        log.info("Supplier created successfully with id: {}", savedSupplier.getId());
        return supplierMapper.toDto(savedSupplier);
    }

    public SupplierDto updateSupplier(Long id, SupplierDto supplierDto) {
        log.info("Updating supplier with id: {}", id);
        Supplier supplier = supplierRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        // Check if new code conflicts with existing supplier
        if (supplierDto.getCode() != null && !supplierDto.getCode().trim().isEmpty() && 
            !supplierDto.getCode().equals(supplier.getCode())) {
            Optional<Supplier> existingSupplier = supplierRepository.findByCodeAndIsActiveTrue(supplierDto.getCode());
            if (existingSupplier.isPresent()) {
                throw new BusinessException("Supplier code already exists: " + supplierDto.getCode());
            }
        }

        supplierMapper.updateEntityFromDto(supplierDto, supplier);
        Supplier updatedSupplier = supplierRepository.save(supplier);
        log.info("Supplier updated successfully with id: {}", updatedSupplier.getId());
        return supplierMapper.toDto(updatedSupplier);
    }

    public void deleteSupplier(Long id) {
        log.info("Deleting supplier with id: {}", id);
        Supplier supplier = supplierRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        
        supplier.setIsActive(false);
        supplierRepository.save(supplier);
        log.info("Supplier deleted successfully with id: {}", id);
    }

    public List<SupplierDto> searchSuppliers(String keyword) {
        log.info("Searching suppliers with keyword: {}", keyword);
        List<Supplier> suppliers = supplierRepository.searchSuppliers(keyword);
        return supplierMapper.toDtoList(suppliers);
    }

    public List<SupplierDto> getSuppliersByStatus(Supplier.SupplierStatus status) {
        log.info("Fetching suppliers by status: {}", status);
        List<Supplier> suppliers = supplierRepository.findByStatusAndIsActiveTrue(status);
        return supplierMapper.toDtoList(suppliers);
    }

    public List<SupplierDto> getSuppliersByVehicleBrand(String vehicleBrand) {
        log.info("Fetching suppliers by vehicle brand: {}", vehicleBrand);
        List<Supplier> suppliers = supplierRepository.findByVehicleBrandsContainingAndIsActiveTrue(vehicleBrand);
        return supplierMapper.toDtoList(suppliers);
    }

    public List<SupplierDto> getSuppliersByRating(Double minRating) {
        log.info("Fetching suppliers with rating >= {}", minRating);
        List<Supplier> suppliers = supplierRepository.findByRatingGreaterThanEqualAndIsActiveTrue(minRating);
        return supplierMapper.toDtoList(suppliers);
    }

    public void updateSupplierRating(Long supplierId, Double newRating) {
        log.info("Updating rating for supplier id: {} to {}", supplierId, newRating);
        Supplier supplier = supplierRepository.findByIdAndIsActiveTrue(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + supplierId));
        
        supplier.setRating(newRating);
        supplierRepository.save(supplier);
        log.info("Supplier rating updated successfully");
    }

    public List<SupplierDto> getTopRatedSuppliers(int limit) {
        log.info("Fetching top {} rated suppliers", limit);
        List<Supplier> suppliers = supplierRepository.findTopRatedSuppliers(limit);
        return supplierMapper.toDtoList(suppliers);
    }

    public List<SupplierDto> getSuppliersByPaymentTerms(Supplier.PaymentTerms paymentTerms) {
        log.info("Fetching suppliers by payment terms: {}", paymentTerms);
        List<Supplier> suppliers = supplierRepository.findByPaymentTermsAndIsActiveTrue(paymentTerms);
        return supplierMapper.toDtoList(suppliers);
    }
}
