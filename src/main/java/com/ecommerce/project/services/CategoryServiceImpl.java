package com.ecommerce.project.services;

import com.ecommerce.project.models.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
    
    @Override
    public Category getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Category with ID %d not found", categoryId)
            )
        );
        
        return category;
    }
    
    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
    
    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
        );
        
        categoryRepository.delete(category);
        
        return "Category deleted successfully";
    }
    
    @Override
    public Category updateCategory(Long categoryId, Category category) {
        List<Category> categories = categoryRepository.findAll();
        Optional<Category> optionalCategory = categories.stream()
                                                  .filter(c -> c.getCategoryId().equals(categoryId))
                                                  .findFirst();
        
        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return categoryRepository.save(existingCategory);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found");
        }
    }
}
