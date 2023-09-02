package com.github.java_explore_with_me.main.category.mapper;

import com.github.java_explore_with_me.main.category.dto.CategoryInputDto;
import com.github.java_explore_with_me.main.category.dto.CategoryOutDto;
import com.github.java_explore_with_me.main.category.model.Category;
import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category categoryInputDtoToCategory(CategoryInputDto categoryInputDto);

    CategoryOutDto categoryToCategoryOutDto(Category category);

    List<CategoryOutDto> categoriesToCategoriesOutDto(List<Category> paginatedCategories);
}
