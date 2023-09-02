package com.github.java_explore_with_me.main.category.controller;

import com.github.java_explore_with_me.main.category.dto.CategoryOutDto;
import com.github.java_explore_with_me.main.category.service.interfaces.CategoryService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public CategoryOutDto getCategoryById(@PathVariable Long id) {
        log.info("Поступил запрос на контроллер PublicCategoryController" +
                " на получение категории с id: {}", id);
        return categoryService.getCategoryById(id);
    }

    @GetMapping
    public List<CategoryOutDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Поступил запрос на контроллер PublicCategoryController" +
                " на получение категорий size: {}, from: {}", size, from);
        return categoryService.getCategories(from, size);
    }
}
