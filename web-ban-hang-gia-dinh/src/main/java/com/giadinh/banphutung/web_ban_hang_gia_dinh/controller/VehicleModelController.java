package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.VehicleModelDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.VehicleModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle-models")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Model Management", description = "APIs for managing vehicle models")
public class VehicleModelController {

    private final VehicleModelService vehicleModelService;

    @GetMapping
    @Operation(summary = "Get all vehicle models", description = "Retrieve a list of all active vehicle models")
    public ResponseEntity<List<VehicleModelDto>> getAllVehicleModels() {
        log.info("GET /api/vehicle-models - Fetching all vehicle models");
        List<VehicleModelDto> vehicleModels = vehicleModelService.getAllVehicleModels();
        return ResponseEntity.ok(vehicleModels);
    }

    @GetMapping("/page")
    @Operation(summary = "Get vehicle models with pagination", description = "Retrieve vehicle models with pagination support")
    public ResponseEntity<Page<VehicleModelDto>> getVehicleModelsWithPagination(Pageable pageable) {
        log.info("GET /api/vehicle-models/page - Fetching vehicle models with pagination: {}", pageable);
        Page<VehicleModelDto> vehicleModels = vehicleModelService.getVehicleModelsWithPagination(pageable);
        return ResponseEntity.ok(vehicleModels);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle model by ID", description = "Retrieve a specific vehicle model by its ID")
    public ResponseEntity<VehicleModelDto> getVehicleModelById(@PathVariable Long id) {
        log.info("GET /api/vehicle-models/{} - Fetching vehicle model by id", id);
        VehicleModelDto vehicleModel = vehicleModelService.getVehicleModelById(id);
        return ResponseEntity.ok(vehicleModel);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get vehicle model by code", description = "Retrieve a specific vehicle model by its code")
    public ResponseEntity<VehicleModelDto> getVehicleModelByCode(@PathVariable String code) {
        log.info("GET /api/vehicle-models/code/{} - Fetching vehicle model by code", code);
        VehicleModelDto vehicleModel = vehicleModelService.getVehicleModelByCode(code);
        return ResponseEntity.ok(vehicleModel);
    }

    @PostMapping
    @Operation(summary = "Create new vehicle model", description = "Create a new vehicle model")
    public ResponseEntity<VehicleModelDto> createVehicleModel(@Valid @RequestBody VehicleModelDto vehicleModelDto) {
        log.info("POST /api/vehicle-models - Creating new vehicle model: {} {}", vehicleModelDto.getBrand(), vehicleModelDto.getModel());
        VehicleModelDto vehicleModel = vehicleModelService.createVehicleModel(vehicleModelDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle model", description = "Update an existing vehicle model")
    public ResponseEntity<VehicleModelDto> updateVehicleModel(@PathVariable Long id, @Valid @RequestBody VehicleModelDto vehicleModelDto) {
        log.info("PUT /api/vehicle-models/{} - Updating vehicle model", id);
        VehicleModelDto vehicleModel = vehicleModelService.updateVehicleModel(id, vehicleModelDto);
        return ResponseEntity.ok(vehicleModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle model", description = "Delete a vehicle model (soft delete)")
    public ResponseEntity<Void> deleteVehicleModel(@PathVariable Long id) {
        log.info("DELETE /api/vehicle-models/{} - Deleting vehicle model", id);
        vehicleModelService.deleteVehicleModel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/brand/{brand}")
    @Operation(summary = "Get vehicle models by brand", description = "Retrieve vehicle models by brand")
    public ResponseEntity<List<VehicleModelDto>> getVehicleModelsByBrand(@PathVariable String brand) {
        log.info("GET /api/vehicle-models/brand/{} - Fetching vehicle models by brand", brand);
        List<VehicleModelDto> vehicleModels = vehicleModelService.getVehicleModelsByBrand(brand);
        return ResponseEntity.ok(vehicleModels);
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Get vehicle models by year", description = "Retrieve vehicle models by year")
    public ResponseEntity<List<VehicleModelDto>> getVehicleModelsByYear(@PathVariable String year) {
        log.info("GET /api/vehicle-models/year/{} - Fetching vehicle models by year", year);
        List<VehicleModelDto> vehicleModels = vehicleModelService.getVehicleModelsByYear(year);
        return ResponseEntity.ok(vehicleModels);
    }

    @GetMapping("/engine/{engine}")
    @Operation(summary = "Get vehicle models by engine", description = "Retrieve vehicle models by engine")
    public ResponseEntity<List<VehicleModelDto>> getVehicleModelsByEngine(@PathVariable String engine) {
        log.info("GET /api/vehicle-models/engine/{} - Fetching vehicle models by engine", engine);
        List<VehicleModelDto> vehicleModels = vehicleModelService.getVehicleModelsByEngine(engine);
        return ResponseEntity.ok(vehicleModels);
    }

    @GetMapping("/fuel-type/{fuelType}")
    @Operation(summary = "Get vehicle models by fuel type", description = "Retrieve vehicle models by fuel type")
    public ResponseEntity<List<VehicleModelDto>> getVehicleModelsByFuelType(@PathVariable String fuelType) {
        log.info("GET /api/vehicle-models/fuel-type/{} - Fetching vehicle models by fuel type", fuelType);
        List<VehicleModelDto> vehicleModels = vehicleModelService.getVehicleModelsByFuelType(fuelType);
        return ResponseEntity.ok(vehicleModels);
    }

    @GetMapping("/body-type/{bodyType}")
    @Operation(summary = "Get vehicle models by body type", description = "Retrieve vehicle models by body type")
    public ResponseEntity<List<VehicleModelDto>> getVehicleModelsByBodyType(@PathVariable String bodyType) {
        log.info("GET /api/vehicle-models/body-type/{} - Fetching vehicle models by body type", bodyType);
        List<VehicleModelDto> vehicleModels = vehicleModelService.getVehicleModelsByBodyType(bodyType);
        return ResponseEntity.ok(vehicleModels);
    }

    @GetMapping("/search")
    @Operation(summary = "Search vehicle models", description = "Search vehicle models by keyword")
    public ResponseEntity<List<VehicleModelDto>> searchVehicleModels(@RequestParam String keyword) {
        log.info("GET /api/vehicle-models/search - Searching vehicle models with keyword: {}", keyword);
        List<VehicleModelDto> vehicleModels = vehicleModelService.searchVehicleModels(keyword);
        return ResponseEntity.ok(vehicleModels);
    }

    @GetMapping("/brands")
    @Operation(summary = "Get all brands", description = "Retrieve all unique vehicle brands")
    public ResponseEntity<List<String>> getAllBrands() {
        log.info("GET /api/vehicle-models/brands - Fetching all vehicle brands");
        List<String> brands = vehicleModelService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/years")
    @Operation(summary = "Get all years", description = "Retrieve all unique vehicle years")
    public ResponseEntity<List<String>> getAllYears() {
        log.info("GET /api/vehicle-models/years - Fetching all vehicle years");
        List<String> years = vehicleModelService.getAllYears();
        return ResponseEntity.ok(years);
    }

    @GetMapping("/fuel-types")
    @Operation(summary = "Get all fuel types", description = "Retrieve all unique fuel types")
    public ResponseEntity<List<String>> getAllFuelTypes() {
        log.info("GET /api/vehicle-models/fuel-types - Fetching all fuel types");
        List<String> fuelTypes = vehicleModelService.getAllFuelTypes();
        return ResponseEntity.ok(fuelTypes);
    }

    @GetMapping("/body-types")
    @Operation(summary = "Get all body types", description = "Retrieve all unique body types")
    public ResponseEntity<List<String>> getAllBodyTypes() {
        log.info("GET /api/vehicle-models/body-types - Fetching all body types");
        List<String> bodyTypes = vehicleModelService.getAllBodyTypes();
        return ResponseEntity.ok(bodyTypes);
    }

    @GetMapping("/sorted")
    @Operation(summary = "Get vehicle models by sort order", description = "Retrieve vehicle models ordered by their sort order")
    public ResponseEntity<List<VehicleModelDto>> getVehicleModelsBySortOrder() {
        log.info("GET /api/vehicle-models/sorted - Fetching vehicle models by sort order");
        List<VehicleModelDto> vehicleModels = vehicleModelService.getVehicleModelsBySortOrder();
        return ResponseEntity.ok(vehicleModels);
    }

    @PutMapping("/{id}/sort-order")
    @Operation(summary = "Update vehicle model sort order", description = "Update the sort order of a vehicle model")
    public ResponseEntity<Void> updateVehicleModelSortOrder(@PathVariable Long id, @RequestParam Integer newSortOrder) {
        log.info("PUT /api/vehicle-models/{}/sort-order - Updating vehicle model sort order to {}", id, newSortOrder);
        vehicleModelService.updateVehicleModelSortOrder(id, newSortOrder);
        return ResponseEntity.ok().build();
    }
}
