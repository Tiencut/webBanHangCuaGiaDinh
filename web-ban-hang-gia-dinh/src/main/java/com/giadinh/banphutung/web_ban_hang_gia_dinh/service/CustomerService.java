package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer.CustomerStatus;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer.CustomerType;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    // Tạo customer mới
    public Customer createCustomer(Customer customer) {
        log.info("Creating new customer: {}", customer.getName());
        
        // Kiểm tra code đã tồn tại
        if (customer.getCode() != null && customerRepository.existsByCode(customer.getCode())) {
            throw new RuntimeException("Customer code đã tồn tại: " + customer.getCode());
        }
        
        // Kiểm tra phone đã tồn tại
        if (customer.getPhone() != null && customerRepository.existsByPhone(customer.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại: " + customer.getPhone());
        }
        
        // Kiểm tra email đã tồn tại
        if (customer.getEmail() != null && customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + customer.getEmail());
        }
        
        // Tự động tạo code nếu chưa có
        if (customer.getCode() == null || customer.getCode().trim().isEmpty()) {
            customer.setCode(generateCustomerCode(customer.getName()));
        }
        
        // Set default values
        if (customer.getCreditLimit() == null) {
            customer.setCreditLimit(BigDecimal.ZERO);
        }
        if (customer.getCurrentDebt() == null) {
            customer.setCurrentDebt(BigDecimal.ZERO);
        }
        
        return customerRepository.save(customer);
    }
    
    // Tìm customer theo ID
    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
    
    // Tìm customer theo code
    @Transactional(readOnly = true)
    public Optional<Customer> findByCode(String code) {
        return customerRepository.findByCode(code);
    }
    
    // Tìm customer theo phone
    @Transactional(readOnly = true)
    public Optional<Customer> findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }
    
    // Tìm tất cả customer active
    @Transactional(readOnly = true)
    public List<Customer> findAllActiveCustomers() {
        return customerRepository.findByStatusOrderByNameAsc(CustomerStatus.ACTIVE);
    }
    
    // Tìm customer theo type
    @Transactional(readOnly = true)
    public List<Customer> findByCustomerType(CustomerType type) {
        return customerRepository.findByCustomerType(type);
    }
    
    // Tìm customer VIP
    @Transactional(readOnly = true)
    public List<Customer> findVipCustomers() {
        return customerRepository.findByCustomerTypeOrTotalSpentGreaterThanEqual(
            CustomerType.VIP, new BigDecimal("50000000"));
    }
    
    // ========== CRUD Methods cơ bản ==========
    
    // Lấy tất cả customers
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
    
    // Lấy customers với phân trang
    @Transactional(readOnly = true)
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
    
    // Lưu customer (create/update)
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
    
    // Kiểm tra customer tồn tại
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return customerRepository.existsById(id);
    }
    
    // Xóa customer theo ID
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
    
    // Đếm số lượng customer
    @Transactional(readOnly = true)
    public long count() {
        return customerRepository.count();
    }
    
    // Tìm kiếm customer theo tên
    @Transactional(readOnly = true)
    public List<Customer> findByNameContaining(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Tìm kiếm customer theo phone
    @Transactional(readOnly = true)
    public List<Customer> findByPhoneContaining(String phone) {
        return customerRepository.findByPhoneContaining(phone);
    }
    
    // ========== End CRUD Methods ==========
    
    // Cập nhật customer
    public Customer updateCustomer(Long id, Customer customerUpdate) {
        log.info("Updating customer with id: {}", id);
        
        Customer existingCustomer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        // Cập nhật thông tin cơ bản
        existingCustomer.setName(customerUpdate.getName());
        existingCustomer.setAddress(customerUpdate.getAddress());
        existingCustomer.setContactPerson(customerUpdate.getContactPerson());
        existingCustomer.setTaxCode(customerUpdate.getTaxCode());
        existingCustomer.setCompanyName(customerUpdate.getCompanyName());
        existingCustomer.setCustomerType(customerUpdate.getCustomerType());
        existingCustomer.setCreditLimit(customerUpdate.getCreditLimit());
        existingCustomer.setDiscountPercentage(customerUpdate.getDiscountPercentage());
        existingCustomer.setPreferredVehicleBrands(customerUpdate.getPreferredVehicleBrands());
        existingCustomer.setNotes(customerUpdate.getNotes());
        
        // Cập nhật code nếu khác
        if (!existingCustomer.getCode().equals(customerUpdate.getCode())) {
            if (customerRepository.existsByCode(customerUpdate.getCode())) {
                throw new RuntimeException("Customer code đã tồn tại: " + customerUpdate.getCode());
            }
            existingCustomer.setCode(customerUpdate.getCode());
        }
        
        // Cập nhật phone nếu khác
        if (customerUpdate.getPhone() != null && 
            !customerUpdate.getPhone().equals(existingCustomer.getPhone())) {
            if (customerRepository.existsByPhone(customerUpdate.getPhone())) {
                throw new RuntimeException("Số điện thoại đã tồn tại: " + customerUpdate.getPhone());
            }
            existingCustomer.setPhone(customerUpdate.getPhone());
        }
        
        // Cập nhật email nếu khác
        if (customerUpdate.getEmail() != null && 
            !customerUpdate.getEmail().equals(existingCustomer.getEmail())) {
            if (customerRepository.existsByEmail(customerUpdate.getEmail())) {
                throw new RuntimeException("Email đã tồn tại: " + customerUpdate.getEmail());
            }
            existingCustomer.setEmail(customerUpdate.getEmail());
        }
        
        return customerRepository.save(existingCustomer);
    }
    
    // Cập nhật công nợ customer
    public Customer updateCustomerDebt(Long id, BigDecimal debtAmount) {
        log.info("Updating debt for customer id: {} to {}", id, debtAmount);
        
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        customer.setCurrentDebt(debtAmount);
        return customerRepository.save(customer);
    }
    
    // Thanh toán công nợ
    public Customer payDebt(Long id, BigDecimal paymentAmount) {
        log.info("Processing debt payment for customer id: {}, amount: {}", id, paymentAmount);
        
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        if (paymentAmount.compareTo(customer.getCurrentDebt()) > 0) {
            throw new RuntimeException("Số tiền thanh toán không thể lớn hơn công nợ hiện tại");
        }
        
        customer.setCurrentDebt(customer.getCurrentDebt().subtract(paymentAmount));
        return customerRepository.save(customer);
    }
    
    // Cập nhật loyalty points
    public Customer addLoyaltyPoints(Long id, Integer points) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        return customerRepository.save(customer);
    }
    
    // Cập nhật thống kê mua hàng
    public Customer updatePurchaseStats(Long id, BigDecimal orderAmount) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        // Cập nhật tổng chi tiêu
        customer.setTotalSpent(customer.getTotalSpent().add(orderAmount));
        
        // Cập nhật số đơn hàng
        customer.setTotalOrders(customer.getTotalOrders() + 1);
        
        // Cập nhật ngày mua hàng
        LocalDate today = LocalDate.now();
        customer.setLastPurchaseDate(today);
        
        if (customer.getFirstPurchaseDate() == null) {
            customer.setFirstPurchaseDate(today);
        }
        
        // Tự động nâng cấp lên VIP nếu đủ điều kiện
        if (customer.getTotalSpent().compareTo(new BigDecimal("50000000")) >= 0 && 
            customer.getCustomerType() != CustomerType.VIP) {
            customer.setCustomerType(CustomerType.VIP);
            log.info("Customer {} upgraded to VIP", customer.getName());
        }
        
        return customerRepository.save(customer);
    }
    
    // Khóa/mở khóa customer
    public void toggleCustomerStatus(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        if (customer.getStatus() == CustomerStatus.ACTIVE) {
            customer.setStatus(CustomerStatus.INACTIVE);
        } else if (customer.getStatus() == CustomerStatus.INACTIVE) {
            customer.setStatus(CustomerStatus.ACTIVE);
        }
        
        customerRepository.save(customer);
        log.info("Customer {} status changed to: {}", customer.getName(), customer.getStatus());
    }
    
    // Blacklist customer
    public void blacklistCustomer(Long id, String reason) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        customer.setStatus(CustomerStatus.BLACKLISTED);
        customer.setNotes(customer.getNotes() + "\n[BLACKLISTED] " + reason);
        
        customerRepository.save(customer);
        log.warn("Customer {} has been blacklisted: {}", customer.getName(), reason);
    }
    
    // Lấy thống kê customer
    @Transactional(readOnly = true)
    public CustomerStats getCustomerStats() {
        Long totalCustomers = customerRepository.count();
        Long activeCustomers = customerRepository.countByCustomerType(CustomerType.RETAIL) +
                              customerRepository.countByCustomerType(CustomerType.WHOLESALE) +
                              customerRepository.countByCustomerType(CustomerType.GARAGE) +
                              customerRepository.countByCustomerType(CustomerType.DEALER) +
                              customerRepository.countByCustomerType(CustomerType.VIP);
        BigDecimal totalDebt = customerRepository.getTotalDebt();
        
        return new CustomerStats(totalCustomers, activeCustomers, totalDebt);
    }
    
    // Helper methods
    private String generateCustomerCode(String name) {
        // Tạo code từ tên (lấy 3 ký tự đầu + số)
        String prefix = name.toLowerCase()
                           .replaceAll("[^a-z0-9]", "")
                           .substring(0, Math.min(3, name.length()));
        
        String code = "KH" + prefix.toUpperCase() + String.format("%04d", 
            customerRepository.count() + 1);
        
        // Đảm bảo unique
        int counter = 1;
        String originalCode = code;
        while (customerRepository.existsByCode(code)) {
            code = originalCode + "_" + counter++;
        }
        
        return code;
    }
    
    // Inner class for stats
    public static class CustomerStats {
        public final Long totalCustomers;
        public final Long activeCustomers;
        public final BigDecimal totalDebt;
        
        public CustomerStats(Long totalCustomers, Long activeCustomers, BigDecimal totalDebt) {
            this.totalCustomers = totalCustomers;
            this.activeCustomers = activeCustomers;
            this.totalDebt = totalDebt != null ? totalDebt : BigDecimal.ZERO;
        }
    }
}
