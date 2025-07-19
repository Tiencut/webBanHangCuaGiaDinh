package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.VehicleModelDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper.VehicleModelMapper;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.VehicleModelRepository;
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
public class VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleModelMapper vehicleModelMapper;

    public List<VehicleModelDto> getAllVehicleModels() {
        log.info("Fetching all vehicle models");
        List<VehicleModel> vehicleModels = vehicleModelRepository.findByIsActiveTrue();
        return vehicleModelMapper.toDtoList(vehicleModels);
    }

    public Page<VehicleModelDto> getVehicleModelsWithPagination(Pageable pageable) {
        log.info("Fetching vehicle models with pagination: {}", pageable);
        Page<VehicleModel> vehicleModels = vehicleModelRepository.findByIsActiveTrue(pageable);
        return vehicleModels.map(vehicleModelMapper::toDto);
    }

    public VehicleModelDto getVehicleModelById(Long id) {
        log.info("Fetching vehicle model by id: {}", id);
        VehicleModel vehicleModel = vehicleModelRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle model not found with id: " + id));
        return vehicleModelMapper.toDto(vehicleModel);
    }

    public VehicleModelDto getVehicleModelByCode(String code) {
        log.info("Fetching vehicle model by code: {}", code);
        VehicleModel vehicleModel = vehicleModelRepository.findByCodeAndIsActiveTrue(code)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle model not found with code: " + code));
        return vehicleModelMapper.toDto(vehicleModel);
    }

    public VehicleModelDto createVehicleModel(VehicleModelDto vehicleModelDto) {
        log.info("Creating new vehicle model: {} {}", vehicleModelDto.getBrand(), vehicleModelDto.getModel());
        
        // Check if vehicle model code already exists
        if (vehicleModelDto.getCode() != null && !vehicleModelDto.getCode().trim().isEmpty()) {
            Optional<VehicleModel> existingVehicleModel = vehicleModelRepository.findByCodeAndIsActiveTrue(vehicleModelDto.getCode());
            if (existingVehicleModel.isPresent()) {
                throw new BusinessException("Vehicle model code already exists: " + vehicleModelDto.getCode());
            }
        }

        VehicleModel vehicleModel = new VehicleModel();
        vehicleModelMapper.updateEntityFromDto(vehicleModelDto, vehicleModel);
        vehicleModel.setIsActive(true);

        VehicleModel savedVehicleModel = vehicleModelRepository.save(vehicleModel);
        log.info("Vehicle model created successfully with id: {}", savedVehicleModel.getId());
        return vehicleModelMapper.toDto(savedVehicleModel);
    }

    public VehicleModelDto updateVehicleModel(Long id, VehicleModelDto vehicleModelDto) {
        log.info("Updating vehicle model with id: {}", id);
        VehicleModel vehicleModel = vehicleModelRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle model not found with id: " + id));

        // Check if new code conflicts with existing vehicle model
        if (vehicleModelDto.getCode() != null && !vehicleModelDto.getCode().trim().isEmpty() && 
            !vehicleModelDto.getCode().equals(vehicleModel.getCode())) {
            Optional<VehicleModel> existingVehicleModel = vehicleModelRepository.findByCodeAndIsActiveTrue(vehicleModelDto.getCode());
            if (existingVehicleModel.isPresent()) {
                throw new BusinessException("Vehicle model code already exists: " + vehicleModelDto.getCode());
            }
        }

        vehicleModelMapper.updateEntityFromDto(vehicleModelDto, vehicleModel);
        VehicleModel updatedVehicleModel = vehicleModelRepository.save(vehicleModel);
        log.info("Vehicle model updated successfully with id: {}", updatedVehicleModel.getId());
        return vehicleModelMapper.toDto(updatedVehicleModel);
    }

    public void deleteVehicleModel(Long id) {
        log.info("Deleting vehicle model with id: {}", id);
        VehicleModel vehicleModel = vehicleModelRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle model not found with id: " + id));
        
        vehicleModel.setIsActive(false);
        vehicleModelRepository.save(vehicleModel);
        log.info("Vehicle model deleted successfully with id: {}", id);
    }

    public List<VehicleModelDto> getVehicleModelsByBrand(String brand) {
        log.info("Fetching vehicle models by brand: {}", brand);
        List<VehicleModel> vehicleModels = vehicleModelRepository.findByBrandContainingIgnoreCaseAndIsActiveTrue(brand);
        return vehicleModelMapper.toDtoList(vehicleModels);
    }

    public List<VehicleModelDto> getVehicleModelsByYear(String year) {
        log.info("Fetching vehicle models by year: {}", year);
        List<VehicleModel> vehicleModels = vehicleModelRepository.findByYearAndIsActiveTrue(year);
        return vehicleModelMapper.toDtoList(vehicleModels);
    }

    public List<VehicleModelDto> getVehicleModelsByEngine(String engine) {
        log.info("Fetching vehicle models by engine: {}", engine);
        List<VehicleModel> vehicleModels = vehicleModelRepository.findByEngineContainingIgnoreCaseAndIsActiveTrue(engine);
        return vehicleModelMapper.toDtoList(vehicleModels);
    }

    public List<VehicleModelDto> getVehicleModelsByFuelType(String fuelType) {
        log.info("Fetching vehicle models by fuel type: {}", fuelType);
        List<VehicleModel> vehicleModels = vehicleModelRepository.findByFuelTypeAndIsActiveTrue(fuelType);
        return vehicleModelMapper.toDtoList(vehicleModels);
    }

    public List<VehicleModelDto> getVehicleModelsByBodyType(String bodyType) {
        log.info("Fetching vehicle models by body type: {}", bodyType);
        List<VehicleModel> vehicleModels = vehicleModelRepository.findByBodyTypeAndIsActiveTrue(bodyType);
        return vehicleModelMapper.toDtoList(vehicleModels);
    }

    public List<VehicleModelDto> searchVehicleModels(String keyword) {
        log.info("Searching vehicle models with keyword: {}", keyword);
        List<VehicleModel> vehicleModels = vehicleModelRepository.searchVehicleModels(keyword);
        return vehicleModelMapper.toDtoList(vehicleModels);
    }

    public List<String> getAllBrands() {
        log.info("Fetching all vehicle brands");
        return vehicleModelRepository.findAllBrands();
    }

    public List<String> getAllYears() {
        log.info("Fetching all vehicle years");
        return vehicleModelRepository.findAllYears();
    }

    public List<String> getAllFuelTypes() {
        log.info("Fetching all fuel types");
        return vehicleModelRepository.findAllFuelTypes();
    }

    public List<String> getAllBodyTypes() {
        log.info("Fetching all body types");
        return vehicleModelRepository.findAllBodyTypes();
    }

    public List<VehicleModelDto> getVehicleModelsBySortOrder() {
        log.info("Fetching vehicle models ordered by sort order");
        List<VehicleModel> vehicleModels = vehicleModelRepository.findByIsActiveTrueOrderBySortOrderAsc();
        return vehicleModelMapper.toDtoList(vehicleModels);
    }

    public void updateVehicleModelSortOrder(Long vehicleModelId, Integer newSortOrder) {
        log.info("Updating sort order for vehicle model id: {} to {}", vehicleModelId, newSortOrder);
        VehicleModel vehicleModel = vehicleModelRepository.findByIdAndIsActiveTrue(vehicleModelId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle model not found with id: " + vehicleModelId));
        
        vehicleModel.setSortOrder(newSortOrder);
        vehicleModelRepository.save(vehicleModel);
        log.info("Vehicle model sort order updated successfully");
    }
}
