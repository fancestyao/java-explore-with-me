package com.github.java_explore_with_me.main.category.service.classes;

import com.github.java_explore_with_me.main.category.dto.CategoryInputDto;
import com.github.java_explore_with_me.main.category.dto.CategoryOutDto;
import com.github.java_explore_with_me.main.category.mapper.CategoryMapper;
import com.github.java_explore_with_me.main.category.model.Category;
import com.github.java_explore_with_me.main.category.repository.CategoryRepository;
import com.github.java_explore_with_me.main.category.service.interfaces.CategoryService;
import com.github.java_explore_with_me.main.exception.ConflictException;
import com.github.java_explore_with_me.main.exception.NotFoundException;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryOutDto saveCategory(CategoryInputDto newCategoryDto) {
        log.info("Запрос на создание категории успешно передан в сервис CategoryServiceImpl");
        Category category = categoryMapper.categoryInputDtoToCategory(newCategoryDto);
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            log.warn("Произошел конфликт при сохранении категории");
            handleConflictException(e);
        }
        log.info("Категория: {} успешно сохранена", category);
        return categoryMapper.categoryToCategoryOutDto(category);
    }

    @Override
    public CategoryOutDto updateCategory(Long categoryId, CategoryInputDto newCategoryDto) {
        log.info("Запрос на обновление категории успешно передан в сервис CategoryServiceImpl");
        Optional<Category> oldCategory = categoryRepository.findById(categoryId);
        if (oldCategory.isEmpty()) {
            log.warn("Категории с id: {} не существует", categoryId);
            throw new NotFoundException("Категории с id: " + categoryId + " не существует");
        }
        Category updatedCategory = categoryMapper.categoryInputDtoToCategory(newCategoryDto);
        updatedCategory.setId(categoryId);
        try {
            categoryRepository.save(updatedCategory);
        } catch (Exception e) {
            log.warn("Произошел конфликт при обновлении категории");
            handleConflictException(e);
        }
        log.info("Категория: {} успешно обновлена на: {}", oldCategory, updatedCategory);
        return categoryMapper.categoryToCategoryOutDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        log.info("Запрос на удаление категории успешно передан в сервис CategoryServiceImpl");
        if (!categoryRepository.existsById(categoryId)) {
            log.warn("Категории с id: {} не существует", categoryId);
            throw new NotFoundException("Категории с id: " + categoryId + " не существует");
        }
        try {
            categoryRepository.deleteById(categoryId);
        } catch (Exception e) {
            log.warn("Произошел конфликт при удалении категории");
            handleConflictException(e);
        }
        log.info("Категория с id: {} успешно удалена", categoryId);
    }

    @Override
    public CategoryOutDto getCategoryById(Long categoryId) {
        log.info("Запрос на получение категории по id успешно передан в сервис CategoryServiceImpl");
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            log.warn("Категории с id: {} не существует", categoryId);
            throw new NotFoundException("Категории с id: " + categoryId + " не существует");
        }
        CategoryOutDto categoryDto = categoryMapper.categoryToCategoryOutDto(category.get());
        log.info("Категория: {} успешно получена", categoryDto);
        return categoryDto;
    }

    @Override
    public List<CategoryOutDto> getCategories(int from, int size) {
        log.info("Запрос на получение списка категорий успешно передан в сервис CategoryServiceImpl");
        PageRequest pagination = PageRequest.of(from / size, size);
        List<Category> paginatedCategories = categoryRepository.findAll(pagination).getContent();
        log.info("Список категорий: {} успешно получен", paginatedCategories);
        return categoryMapper.categoriesToCategoriesOutDto(paginatedCategories);
    }

    private void handleConflictException(Exception e) {
        StringBuilder stringBuilder = new StringBuilder(e.getCause().getCause().getMessage());
        int indexEqualsSign = stringBuilder.indexOf("=");
        stringBuilder.delete(0, indexEqualsSign + 1);
        throw new ConflictException(stringBuilder.toString().replace("\"", "").trim());
    }
}