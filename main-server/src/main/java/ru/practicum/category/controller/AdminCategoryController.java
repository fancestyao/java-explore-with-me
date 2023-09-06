package ru.practicum.category.controller;

import ru.practicum.category.service.interfaces.CategoryService;
import ru.practicum.category.dto.CategoryOutDto;
import ru.practicum.category.dto.CategoryInputDto;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryOutDto saveCategory(@RequestBody @Valid CategoryInputDto categoryInputDto) {
        log.info("Поступил запрос на контроллер AdminCategoryController" +
                " на создание категории newCategoryDto: {}", categoryInputDto);
        return categoryService.saveCategory(categoryInputDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryOutDto updateCategory(@PathVariable Long id,
                                         @RequestBody @Valid CategoryInputDto categoryInputDto) {
        log.info("Поступил запрос на контроллер AdminCategoryController на обновление категории с id: {} на" +
                " newCategoryDto: {}", id, categoryInputDto);
        return categoryService.updateCategory(id, categoryInputDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        log.info("Поступил запрос на контроллер AdminCategoryController на удаление категории с id: {}", id);
        categoryService.deleteCategory(id);
    }
}
