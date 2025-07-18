package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.BusinessException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.CustomerService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * CustomerController - API quản lý khách hàng
 * 
 * Đây là controller xử lý các API liên quan đến khách hàng:
 * - CRUD khách hàng (Create, Read, Update, Delete)
 * - Tìm kiếm khách hàng theo tên, số điện thoại
 * - Phân trang danh sách khách hàng
 * - Thống kê khách hàng VIP
 * 
 * Các endpoint chính:
 * - GET /api/customers - Lấy danh sách khách hàng
 * - GET /api/customers/{id} - Lấy thông tin 1 khách hàng
 * - POST /api/customers - Tạo khách hàng mới
 * - PUT /api/customers/{id} - Cập nhật khách hàng
 * - DELETE /api/customers/{id} - Xóa khách hàng
 * - GET /api/customers/search - Tìm kiếm khách hàng
 */
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*") // Cho phép frontend gọi API từ domain khác
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Lấy danh sách tất cả khách hàng với phân trang
     * GET /api/customers?page=0&size=10&sort=name
     */
    @GetMapping
    public ResponseEntity<Page<Customer>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
            Page<Customer> customers = customerService.findAll(pageable);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            log.error("Error getting customers: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy thông tin 1 khách hàng theo ID
     * GET /api/customers/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        try {
            Optional<Customer> customer = customerService.findById(id);
            return customer.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (ResourceNotFoundException e) {
            log.error("Customer not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting customer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tạo khách hàng mới
     * POST /api/customers
     * Body: JSON của Customer
     */
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerService.createCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
        } catch (BusinessException e) {
            log.error("Business error creating customer: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            log.error("Error creating customer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cập nhật thông tin khách hàng
     * PUT /api/customers/1
     * Body: JSON của Customer
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id, 
            @Valid @RequestBody Customer customer
    ) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (BusinessException e) {
            log.error("Business error updating customer: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (ResourceNotFoundException e) {
            log.error("Customer not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating customer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Xóa khách hàng
     * DELETE /api/customers/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Customer not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting customer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tìm kiếm khách hàng theo tên
     * GET /api/customers/search?name=Nguyen
     */
    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone
    ) {
        try {
            List<Customer> customers;
            
            if (name != null && !name.isEmpty()) {
                customers = customerService.findByNameContaining(name);
            } else if (phone != null && !phone.isEmpty()) {
                customers = customerService.findByPhoneContaining(phone);
            } else {
                customers = customerService.findAll();
            }
            
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy danh sách khách hàng VIP (theo tổng tiền mua)
     * GET /api/customers/vip
     */
    @GetMapping("/vip")
    public ResponseEntity<List<Customer>> getVipCustomers() {
        try {
            List<Customer> vipCustomers = customerService.findVipCustomers();
            return ResponseEntity.ok(vipCustomers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy số lượng khách hàng
     * GET /api/customers/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCustomerCount() {
        try {
            long count = customerService.count();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy tất cả khách cha (parent_id = null)
     * GET /api/customers/parents
     */
    @GetMapping("/parents")
    public ResponseEntity<List<Customer>> getAllParents() {
        try {
            List<Customer> parents = customerService.findAllParents();
            return ResponseEntity.ok(parents);
        } catch (Exception e) {
            log.error("Error getting parent customers: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy danh sách khách con của một khách cha
     * GET /api/customers/{parentId}/children
     */
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<Customer>> getChildren(@PathVariable Long parentId) {
        try {
            List<Customer> children = customerService.findChildren(parentId);
            return ResponseEntity.ok(children);
        } catch (Exception e) {
            log.error("Error getting children for parent {}: {}", parentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
