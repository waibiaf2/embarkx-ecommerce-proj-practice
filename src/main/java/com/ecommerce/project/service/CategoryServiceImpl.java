package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    List<Category> categories = new ArrayList<>();
    Long nextId = 1L;

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public String createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
        return "Category created successfully";
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categories.stream()
            .filter(c -> c.getCategoryId().equals(categoryId))
            .findFirst().orElseThrow();

        categories.remove(category);

        return "Category deleted successfully";
    }
}
