package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.CustomerVehicle;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.CustomerVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CustomerVehicleController - REST API cho quản lý xe khách hàng
 */
@RestController
@RequestMapping("/api/customer-vehicles")
@CrossOrigin(origins = "*")
public class CustomerVehicleController {
    
    @Autowired
    private CustomerVehicleService customerVehicleService;
    
    /**
     * Tạo xe mới cho khách hàng
     */
    @PostMapping
    public ResponseEntity<CustomerVehicle> createCustomerVehicle(@RequestBody CustomerVehicle customerVehicle) {
        try {
            CustomerVehicle created = customerVehicleService.createCustomerVehicle(customerVehicle);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Cập nhật thông tin xe
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerVehicle> updateCustomerVehicle(
            @PathVariable Long id, 
            @RequestBody CustomerVehicle customerVehicle) {
        try {
            CustomerVehicle updated = customerVehicleService.updateCustomerVehicle(id, customerVehicle);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Xóa xe (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerVehicle(@PathVariable Long id) {
        try {
            customerVehicleService.deleteCustomerVehicle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Lấy xe theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerVehicle> getCustomerVehicleById(@PathVariable Long id) {
        try {
            CustomerVehicle vehicle = customerVehicleService.getCustomerVehicleById(id);
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Lấy xe theo biển số
     */
    @GetMapping("/license-plate/{licensePlate}")
    public ResponseEntity<CustomerVehicle> getCustomerVehicleByLicensePlate(@PathVariable String licensePlate) {
        try {
            CustomerVehicle vehicle = customerVehicleService.getCustomerVehicleByLicensePlate(licensePlate);
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Lấy tất cả xe của một khách hàng
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerVehicle>> getVehiclesByCustomerId(@PathVariable Long customerId) {
        List<CustomerVehicle> vehicles = customerVehicleService.getVehiclesByCustomerId(customerId);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy xe theo mẫu
     */
    @GetMapping("/model/{vehicleModelId}")
    public ResponseEntity<List<CustomerVehicle>> getVehiclesByModelId(@PathVariable Long vehicleModelId) {
        List<CustomerVehicle> vehicles = customerVehicleService.getVehiclesByModelId(vehicleModelId);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy xe theo hãng
     */
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<CustomerVehicle>> getVehiclesByBrand(@PathVariable String brand) {
        List<CustomerVehicle> vehicles = customerVehicleService.getVehiclesByBrand(brand);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy xe theo năm sản xuất
     */
    @GetMapping("/year/{manufacturingYear}")
    public ResponseEntity<List<CustomerVehicle>> getVehiclesByManufacturingYear(@PathVariable Integer manufacturingYear) {
        List<CustomerVehicle> vehicles = customerVehicleService.getVehiclesByManufacturingYear(manufacturingYear);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy xe cần bảo dưỡng
     */
    @GetMapping("/needing-maintenance")
    public ResponseEntity<List<CustomerVehicle>> getVehiclesNeedingMaintenance() {
        List<CustomerVehicle> vehicles = customerVehicleService.getVehiclesNeedingMaintenance();
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy xe có đăng kiểm sắp hết hạn
     */
    @GetMapping("/expiring-inspection")
    public ResponseEntity<List<CustomerVehicle>> getVehiclesWithExpiringInspection(
            @RequestParam(defaultValue = "30") int days) {
        LocalDate expiryDate = LocalDate.now().plusDays(days);
        List<CustomerVehicle> vehicles = customerVehicleService.getVehiclesWithExpiringInspection(expiryDate);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy xe có bảo hiểm sắp hết hạn
     */
    @GetMapping("/expiring-insurance")
    public ResponseEntity<List<CustomerVehicle>> getVehiclesWithExpiringInsurance(
            @RequestParam(defaultValue = "30") int days) {
        LocalDate expiryDate = LocalDate.now().plusDays(days);
        List<CustomerVehicle> vehicles = customerVehicleService.getVehiclesWithExpiringInsurance(expiryDate);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Cập nhật km xe
     */
    @PutMapping("/{id}/mileage")
    public ResponseEntity<Void> updateMileage(
            @PathVariable Long id,
            @RequestParam Integer mileage) {
        try {
            customerVehicleService.updateMileage(id, mileage);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Cập nhật thông tin bảo dưỡng
     */
    @PutMapping("/{id}/maintenance")
    public ResponseEntity<Void> updateMaintenanceInfo(
            @PathVariable Long id,
            @RequestParam String maintenanceDate,
            @RequestParam Integer maintenanceMileage) {
        try {
            LocalDateTime maintenanceDateTime = LocalDateTime.parse(maintenanceDate);
            customerVehicleService.updateMaintenanceInfo(id, maintenanceDateTime, maintenanceMileage);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Cập nhật thông tin đăng kiểm
     */
    @PutMapping("/{id}/inspection")
    public ResponseEntity<Void> updateInspectionInfo(
            @PathVariable Long id,
            @RequestParam String inspectionExpiryDate) {
        try {
            LocalDate expiryDate = LocalDate.parse(inspectionExpiryDate);
            customerVehicleService.updateInspectionInfo(id, expiryDate);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Cập nhật thông tin bảo hiểm
     */
    @PutMapping("/{id}/insurance")
    public ResponseEntity<Void> updateInsuranceInfo(
            @PathVariable Long id,
            @RequestParam String insuranceExpiryDate) {
        try {
            LocalDate expiryDate = LocalDate.parse(insuranceExpiryDate);
            customerVehicleService.updateInsuranceInfo(id, expiryDate);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Tìm kiếm theo tên khách hàng
     */
    @GetMapping("/search/customer")
    public ResponseEntity<List<CustomerVehicle>> searchByCustomerName(@RequestParam String customerName) {
        List<CustomerVehicle> vehicles = customerVehicleService.searchByCustomerName(customerName);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Tìm kiếm theo tên mẫu xe
     */
    @GetMapping("/search/model")
    public ResponseEntity<List<CustomerVehicle>> searchByModelName(@RequestParam String modelName) {
        List<CustomerVehicle> vehicles = customerVehicleService.searchByModelName(modelName);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Tìm kiếm theo biển số
     */
    @GetMapping("/search/license-plate")
    public ResponseEntity<List<CustomerVehicle>> searchByLicensePlate(@RequestParam String licensePlate) {
        List<CustomerVehicle> vehicles = customerVehicleService.searchByLicensePlate(licensePlate);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy xe có km cao nhất
     */
    @GetMapping("/highest-mileage")
    public ResponseEntity<Page<CustomerVehicle>> getHighestMileageVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("mileage").descending());
        Page<CustomerVehicle> vehicles = customerVehicleService.getHighestMileageVehicles(pageable);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy xe cũ nhất
     */
    @GetMapping("/oldest")
    public ResponseEntity<Page<CustomerVehicle>> getOldestVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("manufacturingYear").ascending());
        Page<CustomerVehicle> vehicles = customerVehicleService.getOldestVehicles(pageable);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Đếm số xe của một khách hàng
     */
    @GetMapping("/customer/{customerId}/count")
    public ResponseEntity<Long> countVehiclesByCustomerId(@PathVariable Long customerId) {
        long count = customerVehicleService.countVehiclesByCustomerId(customerId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Kiểm tra xe có cần bảo dưỡng không
     */
    @GetMapping("/{id}/needs-maintenance")
    public ResponseEntity<Boolean> needsMaintenance(@PathVariable Long id) {
        try {
            boolean needsMaintenance = customerVehicleService.needsMaintenance(id);
            return ResponseEntity.ok(needsMaintenance);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Tính số km còn lại đến bảo dưỡng
     */
    @GetMapping("/{id}/mileage-until-maintenance")
    public ResponseEntity<Integer> getMileageUntilMaintenance(@PathVariable Long id) {
        try {
            Integer remaining = customerVehicleService.getMileageUntilMaintenance(id);
            return ResponseEntity.ok(remaining);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("CustomerVehicle API is running");
    }
} 