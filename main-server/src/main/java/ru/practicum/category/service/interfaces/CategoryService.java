package ru.practicum.category.service.interfaces;

import ru.practicum.category.dto.CategoryInputDto;
import ru.practicum.category.dto.CategoryOutDto;

import java.util.List;

public interface CategoryService {
    CategoryOutDto saveCategory(CategoryInputDto categoryInputDto);

    CategoryOutDto updateCategory(Long categoryId, CategoryInputDto categoryInputDto);

    void deleteCategory(Long categoryId);

    CategoryOutDto getCategoryById(Long categoryId);

    List<CategoryOutDto> getCategories(int from, int size);
}
