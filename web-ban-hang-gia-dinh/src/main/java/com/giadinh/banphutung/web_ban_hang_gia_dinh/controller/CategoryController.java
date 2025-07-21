package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import java.util.List;

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

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.CategoryDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.CategorySearchRequest;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Category Management", description = "APIs for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve a list of all active categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        log.info("GET /api/categories - Fetching all categories");
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        log.info("GET /api/categories/{} - Fetching category by id", id);
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get category by code", description = "Retrieve a specific category by its code")
    public ResponseEntity<CategoryDto> getCategoryByCode(@PathVariable String code) {
        log.info("GET /api/categories/code/{} - Fetching category by code", code);
        CategoryDto category = categoryService.getCategoryByCode(code);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @Operation(summary = "Create new category", description = "Create a new category")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("POST /api/categories - Creating new category: {}", categoryDto.getName());
        CategoryDto category = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update an existing category")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto) {
        log.info("PUT /api/categories/{} - Updating category", id);
        CategoryDto category = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete a category (soft delete)")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("DELETE /api/categories/{} - Deleting category", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/root")
    @Operation(summary = "Get root categories", description = "Retrieve all root categories (categories without parent)")
    public ResponseEntity<List<CategoryDto>> getRootCategories() {
        log.info("GET /api/categories/root - Fetching root categories");
        List<CategoryDto> categories = categoryService.getRootCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{parentId}/children")
    @Operation(summary = "Get child categories", description = "Retrieve child categories for a parent category")
    public ResponseEntity<List<CategoryDto>> getChildCategories(@PathVariable Long parentId) {
        log.info("GET /api/categories/{}/children - Fetching child categories", parentId);
        List<CategoryDto> children = categoryService.getChildCategories(parentId);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/{childId}/parent")
    @Operation(summary = "Get parent category", description = "Retrieve parent category for a child category")
    public ResponseEntity<CategoryDto> getParentCategory(@PathVariable Long childId) {
        log.info("GET /api/categories/{}/parent - Fetching parent category", childId);
        CategoryDto parent = categoryService.getParentCategory(childId);
        return ResponseEntity.ok(parent);
    }

    @GetMapping("/level/{level}")
    @Operation(summary = "Get categories by level", description = "Retrieve categories by their level in the hierarchy")
    public ResponseEntity<List<CategoryDto>> getCategoriesByLevel(@PathVariable Integer level) {
        log.info("GET /api/categories/level/{} - Fetching categories by level", level);
        List<CategoryDto> categories = categoryService.getCategoriesByLevel(level);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/search")
    @Operation(summary = "Search categories", description = "Search categories by keyword")
    public ResponseEntity<List<CategoryDto>> searchCategories(@RequestParam String keyword) {
        log.info("GET /api/categories/search - Searching categories with keyword: {}", keyword);
        List<CategoryDto> categories = categoryService.searchCategories(keyword);
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/search/advanced")
    @Operation(summary = "Advanced search categories", description = "Search categories with multiple criteria")
    public ResponseEntity<List<CategoryDto>> advancedSearch(@Valid @RequestBody CategorySearchRequest request) {
        log.info("POST /api/categories/search/advanced - Advanced search categories with request: {}", request);
        List<CategoryDto> categories = categoryService.advancedSearch(request);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/sorted")
    @Operation(summary = "Get categories by sort order", description = "Retrieve categories ordered by their sort order")
    public ResponseEntity<List<CategoryDto>> getCategoriesBySortOrder() {
        log.info("GET /api/categories/sorted - Fetching categories by sort order");
        List<CategoryDto> categories = categoryService.getCategoriesBySortOrder();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}/sort-order")
    @Operation(summary = "Update category sort order", description = "Update the sort order of a category")
    public ResponseEntity<Void> updateCategorySortOrder(@PathVariable Long id, @RequestParam Integer newSortOrder) {
        log.info("PUT /api/categories/{}/sort-order - Updating category sort order to {}", id, newSortOrder);
        categoryService.updateCategorySortOrder(id, newSortOrder);
        return ResponseEntity.ok().build();
    }
}
