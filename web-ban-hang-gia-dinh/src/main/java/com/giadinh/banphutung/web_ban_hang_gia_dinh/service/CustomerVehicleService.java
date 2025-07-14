package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.CustomerVehicle;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.VehicleModel;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CustomerVehicleRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CustomerRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.VehicleModelRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

/**
 * CustomerVehicleService - Service cho quản lý xe khách hàng
 * 
 * Cung cấp các business logic để:
 * - Quản lý thông tin xe khách hàng
 * - Theo dõi bảo dưỡng và đăng kiểm
 * - Gợi ý phụ tùng phù hợp
 * - Phân tích lịch sử sử dụng
 */
@Service
@Transactional
public class CustomerVehicleService {
    
    @Autowired
    private CustomerVehicleRepository customerVehicleRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private VehicleModelRepository vehicleModelRepository;
    
    /**
     * Tạo xe mới cho khách hàng
     */
    public CustomerVehicle createCustomerVehicle(CustomerVehicle customerVehicle) {
        // Validate customer và vehicle model tồn tại
        Customer customer = customerRepository.findById(customerVehicle.getCustomer().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        
        VehicleModel vehicleModel = vehicleModelRepository.findById(customerVehicle.getVehicleModel().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle model not found"));
        
        // Kiểm tra biển số đã tồn tại
        Optional<CustomerVehicle> existing = customerVehicleRepository
                .findByLicensePlate(customerVehicle.getLicensePlate());
        
        if (existing.isPresent()) {
            throw new BusinessException("License plate already exists: " + customerVehicle.getLicensePlate());
        }
        
        // Set default values
        customerVehicle.setCustomer(customer);
        customerVehicle.setVehicleModel(vehicleModel);
        customerVehicle.setIsActive(true);
        customerVehicle.setCreatedAt(LocalDateTime.now());
        customerVehicle.setUpdatedAt(LocalDateTime.now());
        
        // Tính toán tuổi xe
        if (customerVehicle.getManufacturingYear() != null) {
            int currentYear = LocalDate.now().getYear();
            int vehicleAge = currentYear - customerVehicle.getManufacturingYear();
            customerVehicle.setVehicleAge(vehicleAge);
        }
        
        // Set default maintenance interval nếu chưa có
        if (customerVehicle.getMaintenanceInterval() == null) {
            customerVehicle.setMaintenanceInterval(12000); // 12,000 km mặc định
        }
        
        // Tính next maintenance mileage
        if (customerVehicle.getMileage() != null && customerVehicle.getMaintenanceInterval() != null) {
            int nextMaintenance = customerVehicle.getMileage() + customerVehicle.getMaintenanceInterval();
            customerVehicle.setNextMaintenanceMileage(nextMaintenance);
        }
        
        return customerVehicleRepository.save(customerVehicle);
    }
    
    /**
     * Cập nhật thông tin xe
     */
    public CustomerVehicle updateCustomerVehicle(Long id, CustomerVehicle updatedCustomerVehicle) {
        CustomerVehicle existing = customerVehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        // Update fields
        if (updatedCustomerVehicle.getLicensePlate() != null) {
            // Kiểm tra biển số mới không trùng
            Optional<CustomerVehicle> duplicate = customerVehicleRepository
                    .findByLicensePlate(updatedCustomerVehicle.getLicensePlate());
            if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
                throw new BusinessException("License plate already exists: " + updatedCustomerVehicle.getLicensePlate());
            }
            existing.setLicensePlate(updatedCustomerVehicle.getLicensePlate());
        }
        
        if (updatedCustomerVehicle.getVinNumber() != null) {
            existing.setVinNumber(updatedCustomerVehicle.getVinNumber());
        }
        if (updatedCustomerVehicle.getEngineNumber() != null) {
            existing.setEngineNumber(updatedCustomerVehicle.getEngineNumber());
        }
        if (updatedCustomerVehicle.getColor() != null) {
            existing.setColor(updatedCustomerVehicle.getColor());
        }
        if (updatedCustomerVehicle.getMileage() != null) {
            existing.setMileage(updatedCustomerVehicle.getMileage());
            // Cập nhật next maintenance mileage
            if (existing.getMaintenanceInterval() != null) {
                int nextMaintenance = updatedCustomerVehicle.getMileage() + existing.getMaintenanceInterval();
                existing.setNextMaintenanceMileage(nextMaintenance);
            }
        }
        if (updatedCustomerVehicle.getManufacturingYear() != null) {
            existing.setManufacturingYear(updatedCustomerVehicle.getManufacturingYear());
            // Cập nhật tuổi xe
            int currentYear = LocalDate.now().getYear();
            int vehicleAge = currentYear - updatedCustomerVehicle.getManufacturingYear();
            existing.setVehicleAge(vehicleAge);
        }
        if (updatedCustomerVehicle.getUsageType() != null) {
            existing.setUsageType(updatedCustomerVehicle.getUsageType());
        }
        if (updatedCustomerVehicle.getCondition() != null) {
            existing.setCondition(updatedCustomerVehicle.getCondition());
        }
        if (updatedCustomerVehicle.getInspectionExpiryDate() != null) {
            existing.setInspectionExpiryDate(updatedCustomerVehicle.getInspectionExpiryDate());
        }
        if (updatedCustomerVehicle.getInsuranceExpiryDate() != null) {
            existing.setInsuranceExpiryDate(updatedCustomerVehicle.getInsuranceExpiryDate());
        }
        if (updatedCustomerVehicle.getMaintenanceInterval() != null) {
            existing.setMaintenanceInterval(updatedCustomerVehicle.getMaintenanceInterval());
            // Cập nhật next maintenance mileage
            if (existing.getMileage() != null) {
                int nextMaintenance = existing.getMileage() + updatedCustomerVehicle.getMaintenanceInterval();
                existing.setNextMaintenanceMileage(nextMaintenance);
            }
        }
        if (updatedCustomerVehicle.getLastMaintenanceDate() != null) {
            existing.setLastMaintenanceDate(updatedCustomerVehicle.getLastMaintenanceDate());
        }
        if (updatedCustomerVehicle.getLastMaintenanceMileage() != null) {
            existing.setLastMaintenanceMileage(updatedCustomerVehicle.getLastMaintenanceMileage());
        }
        if (updatedCustomerVehicle.getNotes() != null) {
            existing.setNotes(updatedCustomerVehicle.getNotes());
        }
        if (updatedCustomerVehicle.getIsActive() != null) {
            existing.setIsActive(updatedCustomerVehicle.getIsActive());
        }
        
        existing.setUpdatedAt(LocalDateTime.now());
        
        return customerVehicleRepository.save(existing);
    }
    
    /**
     * Xóa xe (soft delete)
     */
    public void deleteCustomerVehicle(Long id) {
        CustomerVehicle customerVehicle = customerVehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        customerVehicle.setIsActive(false);
        customerVehicle.setUpdatedAt(LocalDateTime.now());
        
        customerVehicleRepository.save(customerVehicle);
    }
    
    /**
     * Lấy xe theo ID
     */
    @Transactional(readOnly = true)
    public CustomerVehicle getCustomerVehicleById(Long id) {
        return customerVehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
    }
    
    /**
     * Lấy xe theo biển số
     */
    @Transactional(readOnly = true)
    public CustomerVehicle getCustomerVehicleByLicensePlate(String licensePlate) {
        return customerVehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
    }
    
    /**
     * Lấy tất cả xe của một khách hàng
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesByCustomerId(Long customerId) {
        return customerVehicleRepository.findByCustomerIdAndIsActiveTrueOrderByManufacturingYearDesc(customerId);
    }
    
    /**
     * Lấy tất cả xe theo mẫu
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesByModelId(Long vehicleModelId) {
        return customerVehicleRepository.findByVehicleModelId(vehicleModelId);
    }
    
    /**
     * Lấy xe theo hãng
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesByBrand(String brand) {
        return customerVehicleRepository.findByBrand(brand);
    }
    
    /**
     * Lấy xe theo năm sản xuất
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesByManufacturingYear(Integer manufacturingYear) {
        return customerVehicleRepository.findByManufacturingYear(manufacturingYear);
    }
    
    /**
     * Lấy xe theo khoảng năm sản xuất
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesByManufacturingYearRange(Integer fromYear, Integer toYear) {
        return customerVehicleRepository.findByManufacturingYearBetween(fromYear, toYear);
    }
    
    /**
     * Lấy xe theo loại sử dụng
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesByUsageType(CustomerVehicle.VehicleUsageType usageType) {
        return customerVehicleRepository.findByUsageType(usageType);
    }
    
    /**
     * Lấy xe theo tình trạng
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesByCondition(CustomerVehicle.VehicleCondition condition) {
        return customerVehicleRepository.findByCondition(condition);
    }
    
    /**
     * Lấy xe cần bảo dưỡng
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesNeedingMaintenance() {
        return customerVehicleRepository.findVehiclesNeedingMaintenance();
    }
    
    /**
     * Lấy xe có đăng kiểm sắp hết hạn
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesWithExpiringInspection(LocalDate expiryDate) {
        return customerVehicleRepository.findVehiclesWithExpiringInspection(expiryDate);
    }
    
    /**
     * Lấy xe có bảo hiểm sắp hết hạn
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesWithExpiringInsurance(LocalDate expiryDate) {
        return customerVehicleRepository.findVehiclesWithExpiringInsurance(expiryDate);
    }
    
    /**
     * Lấy xe có đăng kiểm đã hết hạn
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesWithExpiredInspection() {
        return customerVehicleRepository.findVehiclesWithExpiredInspection();
    }
    
    /**
     * Lấy xe có bảo hiểm đã hết hạn
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesWithExpiredInsurance() {
        return customerVehicleRepository.findVehiclesWithExpiredInsurance();
    }
    
    /**
     * Lấy xe theo số km
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesByMileageGreaterThan(Integer minMileage) {
        return customerVehicleRepository.findByMileageGreaterThan(minMileage);
    }
    
    /**
     * Lấy xe theo khoảng km
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesByMileageRange(Integer minMileage, Integer maxMileage) {
        return customerVehicleRepository.findByMileageBetween(minMileage, maxMileage);
    }
    
    /**
     * Lấy xe theo tuổi xe
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesByAgeGreaterThan(Integer minAge) {
        return customerVehicleRepository.findByVehicleAgeGreaterThan(minAge);
    }
    
    /**
     * Tìm kiếm xe theo tên khách hàng
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> searchByCustomerName(String customerName) {
        return customerVehicleRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }
    
    /**
     * Tìm kiếm xe theo tên mẫu xe
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> searchByModelName(String modelName) {
        return customerVehicleRepository.findByModelNameContainingIgnoreCase(modelName);
    }
    
    /**
     * Tìm kiếm xe theo biển số
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> searchByLicensePlate(String licensePlate) {
        return customerVehicleRepository.findByLicensePlateContainingIgnoreCase(licensePlate);
    }
    
    /**
     * Lấy xe có km cao nhất
     */
    @Transactional(readOnly = true)
    public Page<CustomerVehicle> getHighestMileageVehicles(Pageable pageable) {
        return customerVehicleRepository.findHighestMileageVehicles(pageable);
    }
    
    /**
     * Lấy xe cũ nhất
     */
    @Transactional(readOnly = true)
    public Page<CustomerVehicle> getOldestVehicles(Pageable pageable) {
        return customerVehicleRepository.findOldestVehicles(pageable);
    }
    
    /**
     * Lấy xe theo customer type
     */
    @Transactional(readOnly = true)
    public List<CustomerVehicle> getVehiclesByCustomerType(String customerType) {
        return customerVehicleRepository.findByCustomerType(customerType);
    }
    
    /**
     * Cập nhật km xe
     */
    public void updateMileage(Long vehicleId, Integer newMileage) {
        CustomerVehicle vehicle = customerVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        vehicle.setMileage(newMileage);
        
        // Cập nhật next maintenance mileage
        if (vehicle.getMaintenanceInterval() != null) {
            int nextMaintenance = newMileage + vehicle.getMaintenanceInterval();
            vehicle.setNextMaintenanceMileage(nextMaintenance);
        }
        
        vehicle.setUpdatedAt(LocalDateTime.now());
        
        customerVehicleRepository.save(vehicle);
    }
    
    /**
     * Cập nhật thông tin bảo dưỡng
     */
    public void updateMaintenanceInfo(Long vehicleId, LocalDateTime maintenanceDate, Integer maintenanceMileage) {
        CustomerVehicle vehicle = customerVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        vehicle.setLastMaintenanceDate(maintenanceDate);
        vehicle.setLastMaintenanceMileage(maintenanceMileage);
        
        // Cập nhật next maintenance mileage
        if (vehicle.getMaintenanceInterval() != null && maintenanceMileage != null) {
            int nextMaintenance = maintenanceMileage + vehicle.getMaintenanceInterval();
            vehicle.setNextMaintenanceMileage(nextMaintenance);
        }
        
        vehicle.setUpdatedAt(LocalDateTime.now());
        
        customerVehicleRepository.save(vehicle);
    }
    
    /**
     * Cập nhật thông tin đăng kiểm
     */
    public void updateInspectionInfo(Long vehicleId, LocalDate inspectionExpiryDate) {
        CustomerVehicle vehicle = customerVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        vehicle.setInspectionExpiryDate(inspectionExpiryDate);
        vehicle.setUpdatedAt(LocalDateTime.now());
        
        customerVehicleRepository.save(vehicle);
    }
    
    /**
     * Cập nhật thông tin bảo hiểm
     */
    public void updateInsuranceInfo(Long vehicleId, LocalDate insuranceExpiryDate) {
        CustomerVehicle vehicle = customerVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        vehicle.setInsuranceExpiryDate(insuranceExpiryDate);
        vehicle.setUpdatedAt(LocalDateTime.now());
        
        customerVehicleRepository.save(vehicle);
    }
    
    /**
     * Đếm số xe của một khách hàng
     */
    @Transactional(readOnly = true)
    public long countVehiclesByCustomerId(Long customerId) {
        return customerVehicleRepository.countByCustomerIdAndIsActiveTrue(customerId);
    }
    
    /**
     * Đếm số xe theo mẫu
     */
    @Transactional(readOnly = true)
    public long countVehiclesByModelId(Long vehicleModelId) {
        return customerVehicleRepository.countByVehicleModelIdAndIsActiveTrue(vehicleModelId);
    }
    
    /**
     * Đếm số xe theo hãng
     */
    @Transactional(readOnly = true)
    public long countVehiclesByBrand(String brand) {
        return customerVehicleRepository.countByBrand(brand);
    }
    
    /**
     * Đếm số xe cần bảo dưỡng
     */
    @Transactional(readOnly = true)
    public long countVehiclesNeedingMaintenance() {
        return customerVehicleRepository.countVehiclesNeedingMaintenance();
    }
    
    /**
     * Đếm số xe có đăng kiểm sắp hết hạn
     */
    @Transactional(readOnly = true)
    public long countVehiclesWithExpiringInspection(LocalDate expiryDate) {
        return customerVehicleRepository.countVehiclesWithExpiringInspection(expiryDate);
    }
    
    /**
     * Kiểm tra xe có cần bảo dưỡng không
     */
    @Transactional(readOnly = true)
    public boolean needsMaintenance(Long vehicleId) {
        CustomerVehicle vehicle = customerVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        return vehicle.getMileage() != null && 
               vehicle.getNextMaintenanceMileage() != null &&
               vehicle.getMileage() >= vehicle.getNextMaintenanceMileage();
    }
    
    /**
     * Kiểm tra đăng kiểm có sắp hết hạn không
     */
    @Transactional(readOnly = true)
    public boolean hasExpiringInspection(Long vehicleId, int daysThreshold) {
        CustomerVehicle vehicle = customerVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        if (vehicle.getInspectionExpiryDate() == null) {
            return false;
        }
        
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        return vehicle.getInspectionExpiryDate().isBefore(thresholdDate);
    }
    
    /**
     * Kiểm tra bảo hiểm có sắp hết hạn không
     */
    @Transactional(readOnly = true)
    public boolean hasExpiringInsurance(Long vehicleId, int daysThreshold) {
        CustomerVehicle vehicle = customerVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        if (vehicle.getInsuranceExpiryDate() == null) {
            return false;
        }
        
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        return vehicle.getInsuranceExpiryDate().isBefore(thresholdDate);
    }
    
    /**
     * Tính tuổi xe
     */
    @Transactional(readOnly = true)
    public int calculateVehicleAge(Integer manufacturingYear) {
        if (manufacturingYear == null) {
            return 0;
        }
        
        int currentYear = LocalDate.now().getYear();
        return currentYear - manufacturingYear;
    }
    
    /**
     * Tính số km còn lại đến bảo dưỡng
     */
    @Transactional(readOnly = true)
    public Integer getMileageUntilMaintenance(Long vehicleId) {
        CustomerVehicle vehicle = customerVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        if (vehicle.getMileage() == null || vehicle.getNextMaintenanceMileage() == null) {
            return null;
        }
        
        int remaining = vehicle.getNextMaintenanceMileage() - vehicle.getMileage();
        return remaining > 0 ? remaining : 0;
    }
    
    /**
     * Tính số ngày còn lại đến hết hạn đăng kiểm
     */
    @Transactional(readOnly = true)
    public Long getDaysUntilInspectionExpiry(Long vehicleId) {
        CustomerVehicle vehicle = customerVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        if (vehicle.getInspectionExpiryDate() == null) {
            return null;
        }
        
        Period period = Period.between(LocalDate.now(), vehicle.getInspectionExpiryDate());
        return (long) period.getDays();
    }
    
    /**
     * Tính số ngày còn lại đến hết hạn bảo hiểm
     */
    @Transactional(readOnly = true)
    public Long getDaysUntilInsuranceExpiry(Long vehicleId) {
        CustomerVehicle vehicle = customerVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("CustomerVehicle not found"));
        
        if (vehicle.getInsuranceExpiryDate() == null) {
            return null;
        }
        
        Period period = Period.between(LocalDate.now(), vehicle.getInsuranceExpiryDate());
        return (long) period.getDays();
    }
} 