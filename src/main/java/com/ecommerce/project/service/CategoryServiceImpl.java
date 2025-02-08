package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    List<Category> categories = new ArrayList<>();
    Long nextId = 1L;

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public Category getCategory(Long categoryId) {
        return categories.stream()
            .filter(c -> c.getCategoryId().equals(categoryId))
            .findFirst().orElseThrow();
    }

    @Override
    public Category createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
        return category;
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categories.stream()
            .filter(c -> c.getCategoryId().equals(categoryId))
            .findFirst().orElseThrow();

        categories.remove(category);

        return "Category deleted successfully";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {
        /*Category categoryToUpdate = getCategory(categoryId);
        categoryToUpdate.setCategoryName(category.getCategoryName());
        return categoryToUpdate;*/

        Optional<Category> optionalCategory = categories.stream()
            .filter(c -> c.getCategoryId().equals(categoryId))
            .findFirst();

        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return existingCategory;
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found");
        }
    }
}
