package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.CreateCustomerRequest;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.CustomerDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper.CustomerMapper;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<CustomerDto> getAllCustomers() {
        log.info("Fetching all customers");
        List<Customer> customers = customerRepository.findByIsActiveTrue();
        return customerMapper.toDtoList(customers);
    }

    public Page<CustomerDto> getCustomersWithPagination(Pageable pageable) {
        log.info("Fetching customers with pagination: {}", pageable);
        Page<Customer> customers = customerRepository.findByIsActiveTrue(pageable);
        return customers.map(customerMapper::toDto);
    }

    public CustomerDto getCustomerById(Long id) {
        log.info("Fetching customer by id: {}", id);
        Customer customer = customerRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return customerMapper.toDto(customer);
    }

    public CustomerDto getCustomerByCode(String code) {
        log.info("Fetching customer by code: {}", code);
        Customer customer = customerRepository.findByCodeAndIsActiveTrue(code)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with code: " + code));
        return customerMapper.toDto(customer);
    }

    public CustomerDto createCustomer(CreateCustomerRequest request) {
        log.info("Creating new customer: {}", request.getName());
        
        // Check if customer code already exists
        if (request.getCode() != null && !request.getCode().trim().isEmpty()) {
            Optional<Customer> existingCustomer = customerRepository.findByCodeAndIsActiveTrue(request.getCode());
            if (existingCustomer.isPresent()) {
                throw new BusinessException("Customer code already exists: " + request.getCode());
            }
        }

        Customer customer = customerMapper.toEntity(request);
        
        // Set parent if provided
        if (request.getParentId() != null) {
            Customer parent = customerRepository.findByIdAndIsActiveTrue(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent customer not found with id: " + request.getParentId()));
            customer.setParent(parent);
        }

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with id: {}", savedCustomer.getId());
        return customerMapper.toDto(savedCustomer);
    }

    public CustomerDto updateCustomer(Long id, CreateCustomerRequest request) {
        log.info("Updating customer with id: {}", id);
        Customer customer = customerRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        // Check if new code conflicts with existing customer
        if (request.getCode() != null && !request.getCode().trim().isEmpty() && 
            !request.getCode().equals(customer.getCode())) {
            Optional<Customer> existingCustomer = customerRepository.findByCodeAndIsActiveTrue(request.getCode());
            if (existingCustomer.isPresent()) {
                throw new BusinessException("Customer code already exists: " + request.getCode());
            }
        }

        customerMapper.updateEntityFromRequest(request, customer);
        
        // Update parent if provided
        if (request.getParentId() != null) {
            Customer parent = customerRepository.findByIdAndIsActiveTrue(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent customer not found with id: " + request.getParentId()));
            customer.setParent(parent);
        } else {
            customer.setParent(null);
        }

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully with id: {}", updatedCustomer.getId());
        return customerMapper.toDto(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        log.info("Deleting customer with id: {}", id);
        Customer customer = customerRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
        customer.setIsActive(false);
        customerRepository.save(customer);
        log.info("Customer deleted successfully with id: {}", id);
    }

    public List<CustomerDto> searchCustomers(String keyword) {
        log.info("Searching customers with keyword: {}", keyword);
        List<Customer> customers = customerRepository.searchCustomers(keyword);
        return customerMapper.toDtoList(customers);
    }

    public List<CustomerDto> getCustomersByType(Customer.CustomerType customerType) {
        log.info("Fetching customers by type: {}", customerType);
        List<Customer> customers = customerRepository.findByCustomerTypeAndIsActiveTrue(customerType);
        return customerMapper.toDtoList(customers);
    }

    public List<CustomerDto> getCustomersByStatus(Customer.CustomerStatus status) {
        log.info("Fetching customers by status: {}", status);
        List<Customer> customers = customerRepository.findByStatusAndIsActiveTrue(status);
        return customerMapper.toDtoList(customers);
    }

    public List<CustomerDto> getChildCustomers(Long parentId) {
        log.info("Fetching child customers for parent id: {}", parentId);
        List<Customer> children = customerRepository.findByParentIdAndIsActiveTrue(parentId);
        return customerMapper.toDtoList(children);
    }

    public CustomerDto getParentCustomer(Long childId) {
        log.info("Fetching parent customer for child id: {}", childId);
        Customer child = customerRepository.findByIdAndIsActiveTrue(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + childId));
        
        if (child.getParent() == null) {
            throw new ResourceNotFoundException("Customer has no parent");
        }
        
        return customerMapper.toDto(child.getParent());
    }

    public void updateCustomerDebt(Long customerId, Double amount) {
        log.info("Updating debt for customer id: {} by amount: {}", customerId, amount);
        Customer customer = customerRepository.findByIdAndIsActiveTrue(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        
        customer.setCurrentDebt(customer.getCurrentDebt().add(java.math.BigDecimal.valueOf(amount)));
        customerRepository.save(customer);
        log.info("Customer debt updated successfully");
    }

    public void updateCustomerLoyaltyPoints(Long customerId, Integer points) {
        log.info("Updating loyalty points for customer id: {} by points: {}", customerId, points);
        Customer customer = customerRepository.findByIdAndIsActiveTrue(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        customerRepository.save(customer);
        log.info("Customer loyalty points updated successfully");
    }

    public void updateCustomerPurchaseHistory(Long customerId) {
        log.info("Updating purchase history for customer id: {}", customerId);
        Customer customer = customerRepository.findByIdAndIsActiveTrue(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        
        // Update last purchase date
        customer.setLastPurchaseDate(LocalDate.now());
        
        // Update total orders count (this would typically be calculated from orders table)
        // For now, we'll just increment it
        customer.setTotalOrders(customer.getTotalOrders() + 1);
        
        customerRepository.save(customer);
        log.info("Customer purchase history updated successfully");
    }
}
