package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        Category categoryToUpdate = getCategory(categoryId);
        categoryToUpdate.setCategoryName(category.getCategoryName());
        return categoryToUpdate;
    }
}
