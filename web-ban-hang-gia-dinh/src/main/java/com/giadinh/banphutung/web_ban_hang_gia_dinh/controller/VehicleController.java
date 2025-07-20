package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.VehicleModelDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.VehicleModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Management", description = "APIs for managing vehicles")
public class VehicleController {

    private final VehicleModelService vehicleModelService;

    @GetMapping
    @Operation(summary = "Get all vehicles", description = "Retrieve a list of all active vehicles")
    public ResponseEntity<List<VehicleModelDto>> getAllVehicles() {
        log.info("GET /api/vehicles - Fetching all vehicles");
        List<VehicleModelDto> vehicles = vehicleModelService.getAllVehicleModels();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/page")
    @Operation(summary = "Get vehicles with pagination", description = "Retrieve vehicles with pagination support")
    public ResponseEntity<Page<VehicleModelDto>> getVehiclesWithPagination(Pageable pageable) {
        log.info("GET /api/vehicles/page - Fetching vehicles with pagination: {}", pageable);
        Page<VehicleModelDto> vehicles = vehicleModelService.getVehicleModelsWithPagination(pageable);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID", description = "Retrieve a specific vehicle by its ID")
    public ResponseEntity<VehicleModelDto> getVehicleById(@PathVariable Long id) {
        log.info("GET /api/vehicles/{} - Fetching vehicle by id", id);
        VehicleModelDto vehicle = vehicleModelService.getVehicleModelById(id);
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get vehicle by code", description = "Retrieve a specific vehicle by its code")
    public ResponseEntity<VehicleModelDto> getVehicleByCode(@PathVariable String code) {
        log.info("GET /api/vehicles/code/{} - Fetching vehicle by code", code);
        VehicleModelDto vehicle = vehicleModelService.getVehicleModelByCode(code);
        return ResponseEntity.ok(vehicle);
    }

    @PostMapping
    @Operation(summary = "Create new vehicle", description = "Create a new vehicle")
    public ResponseEntity<VehicleModelDto> createVehicle(@Valid @RequestBody VehicleModelDto vehicleDto) {
        log.info("POST /api/vehicles - Creating new vehicle: {} {}", vehicleDto.getBrand(), vehicleDto.getModel());
        VehicleModelDto vehicle = vehicleModelService.createVehicleModel(vehicleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle", description = "Update an existing vehicle")
    public ResponseEntity<VehicleModelDto> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleModelDto vehicleDto) {
        log.info("PUT /api/vehicles/{} - Updating vehicle", id);
        VehicleModelDto vehicle = vehicleModelService.updateVehicleModel(id, vehicleDto);
        return ResponseEntity.ok(vehicle);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle", description = "Delete a vehicle (soft delete)")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        log.info("DELETE /api/vehicles/{} - Deleting vehicle", id);
        vehicleModelService.deleteVehicleModel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/brand/{brand}")
    @Operation(summary = "Get vehicles by brand", description = "Retrieve vehicles by brand")
    public ResponseEntity<List<VehicleModelDto>> getVehiclesByBrand(@PathVariable String brand) {
        log.info("GET /api/vehicles/brand/{} - Fetching vehicles by brand", brand);
        List<VehicleModelDto> vehicles = vehicleModelService.getVehicleModelsByBrand(brand);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Get vehicles by year", description = "Retrieve vehicles by year")
    public ResponseEntity<List<VehicleModelDto>> getVehiclesByYear(@PathVariable String year) {
        log.info("GET /api/vehicles/year/{} - Fetching vehicles by year", year);
        List<VehicleModelDto> vehicles = vehicleModelService.getVehicleModelsByYear(year);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/engine/{engine}")
    @Operation(summary = "Get vehicles by engine", description = "Retrieve vehicles by engine")
    public ResponseEntity<List<VehicleModelDto>> getVehiclesByEngine(@PathVariable String engine) {
        log.info("GET /api/vehicles/engine/{} - Fetching vehicles by engine", engine);
        List<VehicleModelDto> vehicles = vehicleModelService.getVehicleModelsByEngine(engine);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/fuel-type/{fuelType}")
    @Operation(summary = "Get vehicles by fuel type", description = "Retrieve vehicles by fuel type")
    public ResponseEntity<List<VehicleModelDto>> getVehiclesByFuelType(@PathVariable String fuelType) {
        log.info("GET /api/vehicles/fuel-type/{} - Fetching vehicles by fuel type", fuelType);
        List<VehicleModelDto> vehicles = vehicleModelService.getVehicleModelsByFuelType(fuelType);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/body-type/{bodyType}")
    @Operation(summary = "Get vehicles by body type", description = "Retrieve vehicles by body type")
    public ResponseEntity<List<VehicleModelDto>> getVehiclesByBodyType(@PathVariable String bodyType) {
        log.info("GET /api/vehicles/body-type/{} - Fetching vehicles by body type", bodyType);
        List<VehicleModelDto> vehicles = vehicleModelService.getVehicleModelsByBodyType(bodyType);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/search")
    @Operation(summary = "Search vehicles", description = "Search vehicles by keyword")
    public ResponseEntity<List<VehicleModelDto>> searchVehicles(@RequestParam String keyword) {
        log.info("GET /api/vehicles/search - Searching vehicles with keyword: {}", keyword);
        List<VehicleModelDto> vehicles = vehicleModelService.searchVehicleModels(keyword);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/brands")
    @Operation(summary = "Get all brands", description = "Retrieve all unique vehicle brands")
    public ResponseEntity<List<String>> getAllBrands() {
        log.info("GET /api/vehicles/brands - Fetching all vehicle brands");
        List<String> brands = vehicleModelService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/years")
    @Operation(summary = "Get all years", description = "Retrieve all unique vehicle years")
    public ResponseEntity<List<String>> getAllYears() {
        log.info("GET /api/vehicles/years - Fetching all vehicle years");
        List<String> years = vehicleModelService.getAllYears();
        return ResponseEntity.ok(years);
    }

    @GetMapping("/fuel-types")
    @Operation(summary = "Get all fuel types", description = "Retrieve all unique fuel types")
    public ResponseEntity<List<String>> getAllFuelTypes() {
        log.info("GET /api/vehicles/fuel-types - Fetching all fuel types");
        List<String> fuelTypes = vehicleModelService.getAllFuelTypes();
        return ResponseEntity.ok(fuelTypes);
    }

    @GetMapping("/body-types")
    @Operation(summary = "Get all body types", description = "Retrieve all unique body types")
    public ResponseEntity<List<String>> getAllBodyTypes() {
        log.info("GET /api/vehicles/body-types - Fetching all body types");
        List<String> bodyTypes = vehicleModelService.getAllBodyTypes();
        return ResponseEntity.ok(bodyTypes);
    }

    @GetMapping("/sorted")
    @Operation(summary = "Get vehicles by sort order", description = "Retrieve vehicles ordered by their sort order")
    public ResponseEntity<List<VehicleModelDto>> getVehiclesBySortOrder() {
        log.info("GET /api/vehicles/sorted - Fetching vehicles by sort order");
        List<VehicleModelDto> vehicles = vehicleModelService.getVehicleModelsBySortOrder();
        return ResponseEntity.ok(vehicles);
    }

    @PutMapping("/{id}/sort-order")
    @Operation(summary = "Update vehicle sort order", description = "Update the sort order of a vehicle")
    public ResponseEntity<Void> updateVehicleSortOrder(@PathVariable Long id, @RequestParam Integer newSortOrder) {
        log.info("PUT /api/vehicles/{}/sort-order - Updating vehicle sort order to {}", id, newSortOrder);
        vehicleModelService.updateVehicleModelSortOrder(id, newSortOrder);
        return ResponseEntity.ok().build();
    }
} 