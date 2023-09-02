package com.github.java_explore_with_me.main.compilation.service.interfaces;

import com.github.java_explore_with_me.main.compilation.dto.CompilationDto;
import com.github.java_explore_with_me.main.compilation.dto.NewCompilationDto;
import com.github.java_explore_with_me.main.compilation.dto.UpdatedCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(Long compId, UpdatedCompilationRequest updatedCompilationRequest);

    void deleteCompilation(Long compId);

    CompilationDto getCompilationById(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);
}
