package com.ecommerce.project.controllers;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;
    
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
        @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstants.ORDER_DIRECTION, required = false) String sortOrder
    ) {
        CategoryResponse categories = categoryService.getAllCategories(pageNumber, pageSize,sortBy, sortOrder);
        return new ResponseEntity<>(categories ,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long categoryId) {
        CategoryDTO categoryDTO = categoryService.getCategory(categoryId);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(
        @PathVariable Long categoryId,
        @Valid @RequestBody CategoryDTO categoryDTO
    ) {
        CategoryDTO updatedCategory =categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
}
