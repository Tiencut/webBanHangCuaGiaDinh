package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.CreateCustomerRequest;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.CustomerDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Customer;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.CustomerService;
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
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve a list of all active customers")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        log.info("GET /api/customers - Fetching all customers");
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/page")
    @Operation(summary = "Get customers with pagination", description = "Retrieve customers with pagination support")
    public ResponseEntity<Page<CustomerDto>> getCustomersWithPagination(Pageable pageable) {
        log.info("GET /api/customers/page - Fetching customers with pagination: {}", pageable);
        Page<CustomerDto> customers = customerService.getCustomersWithPagination(pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by their ID")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        log.info("GET /api/customers/{} - Fetching customer by id", id);
        CustomerDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get customer by code", description = "Retrieve a specific customer by their code")
    public ResponseEntity<CustomerDto> getCustomerByCode(@PathVariable String code) {
        log.info("GET /api/customers/code/{} - Fetching customer by code", code);
        CustomerDto customer = customerService.getCustomerByCode(code);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    @Operation(summary = "Create new customer", description = "Create a new customer")
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        log.info("POST /api/customers - Creating new customer: {}", request.getName());
        CustomerDto customer = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Update an existing customer")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @Valid @RequestBody CreateCustomerRequest request) {
        log.info("PUT /api/customers/{} - Updating customer", id);
        CustomerDto customer = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Delete a customer (soft delete)")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.info("DELETE /api/customers/{} - Deleting customer", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers", description = "Search customers by keyword")
    public ResponseEntity<List<CustomerDto>> searchCustomers(@RequestParam String keyword) {
        log.info("GET /api/customers/search - Searching customers with keyword: {}", keyword);
        List<CustomerDto> customers = customerService.searchCustomers(keyword);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/type/{customerType}")
    @Operation(summary = "Get customers by type", description = "Retrieve customers by customer type")
    public ResponseEntity<List<CustomerDto>> getCustomersByType(@PathVariable Customer.CustomerType customerType) {
        log.info("GET /api/customers/type/{} - Fetching customers by type", customerType);
        List<CustomerDto> customers = customerService.getCustomersByType(customerType);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get customers by status", description = "Retrieve customers by status")
    public ResponseEntity<List<CustomerDto>> getCustomersByStatus(@PathVariable Customer.CustomerStatus status) {
        log.info("GET /api/customers/status/{} - Fetching customers by status", status);
        List<CustomerDto> customers = customerService.getCustomersByStatus(status);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{parentId}/children")
    @Operation(summary = "Get child customers", description = "Retrieve child customers for a parent customer")
    public ResponseEntity<List<CustomerDto>> getChildCustomers(@PathVariable Long parentId) {
        log.info("GET /api/customers/{}/children - Fetching child customers", parentId);
        List<CustomerDto> children = customerService.getChildCustomers(parentId);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/{childId}/parent")
    @Operation(summary = "Get parent customer", description = "Retrieve parent customer for a child customer")
    public ResponseEntity<CustomerDto> getParentCustomer(@PathVariable Long childId) {
        log.info("GET /api/customers/{}/parent - Fetching parent customer", childId);
        CustomerDto parent = customerService.getParentCustomer(childId);
        return ResponseEntity.ok(parent);
    }

    @PutMapping("/{id}/debt")
    @Operation(summary = "Update customer debt", description = "Update customer's current debt")
    public ResponseEntity<Void> updateCustomerDebt(@PathVariable Long id, @RequestParam Double amount) {
        log.info("PUT /api/customers/{}/debt - Updating customer debt by amount: {}", id, amount);
        customerService.updateCustomerDebt(id, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/loyalty-points")
    @Operation(summary = "Update customer loyalty points", description = "Update customer's loyalty points")
    public ResponseEntity<Void> updateCustomerLoyaltyPoints(@PathVariable Long id, @RequestParam Integer points) {
        log.info("PUT /api/customers/{}/loyalty-points - Updating customer loyalty points by: {}", id, points);
        customerService.updateCustomerLoyaltyPoints(id, points);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/purchase-history")
    @Operation(summary = "Update customer purchase history", description = "Update customer's purchase history")
    public ResponseEntity<Void> updateCustomerPurchaseHistory(@PathVariable Long id) {
        log.info("PUT /api/customers/{}/purchase-history - Updating customer purchase history", id);
        customerService.updateCustomerPurchaseHistory(id);
        return ResponseEntity.ok().build();
    }
}
