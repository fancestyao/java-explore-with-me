package ru.practicum.category.service.classes;

import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.category.dto.CategoryInputDto;
import ru.practicum.category.dto.CategoryOutDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.interfaces.CategoryService;

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
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryOutDto saveCategory(CategoryInputDto newCategoryDto) {
        log.info("Запрос на создание категории успешно передан в сервис CategoryServiceImpl");
        Category category = categoryMapper.categoryInputDtoToCategory(newCategoryDto);
        categoryRepository.save(category);
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
        categoryRepository.save(updatedCategory);
        log.info("Категория: {} успешно обновлена на: {}", oldCategory, updatedCategory);
        return categoryMapper.categoryToCategoryOutDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        log.info("Запрос на удаление категории успешно передан в сервис CategoryServiceImpl");
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            log.warn("Категории с id: {} не существует", categoryId);
            throw new NotFoundException("Категории с id: " + categoryId + " не существует");
        }
        if (eventRepository.existsByCategory(category.get())) {
            log.warn("Невозможно удалить категорию с id: {}, так как есть связанные с ней ивенты", categoryId);
            throw new ConflictException("Невозможно удалить категорию с id: " + categoryId + "," +
                    " так как есть связанные с ней ивенты");
        }
        categoryRepository.deleteById(categoryId);
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
}