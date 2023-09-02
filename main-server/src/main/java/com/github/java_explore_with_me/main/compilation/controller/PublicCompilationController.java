package com.github.java_explore_with_me.main.compilation.controller;

import com.github.java_explore_with_me.main.compilation.dto.CompilationDto;
import com.github.java_explore_with_me.main.compilation.service.interfaces.CompilationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Поступил запрос на контроллер PublicCompilationController" +
                " на получение подборки с id: {}", compId);
        return compilationService.getCompilationById(compId);
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Поступил запрос на контроллер PublicCategoryController" +
                " на получение подборки size: {}, from: {}", size, from);
        return compilationService.getCompilations(pinned, from, size);
    }
}
