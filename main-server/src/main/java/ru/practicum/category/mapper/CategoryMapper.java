package ru.practicum.category.mapper;

import ru.practicum.category.dto.CategoryInputDto;
import ru.practicum.category.dto.CategoryOutDto;
import ru.practicum.category.model.Category;
import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category categoryInputDtoToCategory(CategoryInputDto categoryInputDto);

    CategoryOutDto categoryToCategoryOutDto(Category category);

    List<CategoryOutDto> categoriesToCategoriesOutDto(List<Category> paginatedCategories);
}
