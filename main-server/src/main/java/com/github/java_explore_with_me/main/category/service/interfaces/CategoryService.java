package com.github.java_explore_with_me.main.category.service.interfaces;

import com.github.java_explore_with_me.main.category.dto.CategoryInputDto;
import com.github.java_explore_with_me.main.category.dto.CategoryOutDto;

import java.util.List;

public interface CategoryService {
    CategoryOutDto saveCategory(CategoryInputDto categoryInputDto);

    CategoryOutDto updateCategory(Long categoryId, CategoryInputDto categoryInputDto);

    void deleteCategory(Long categoryId);

    CategoryOutDto getCategoryById(Long categoryId);

    List<CategoryOutDto> getCategories(int from, int size);
}
