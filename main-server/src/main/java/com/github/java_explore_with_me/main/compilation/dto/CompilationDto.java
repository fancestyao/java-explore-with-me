package com.github.java_explore_with_me.main.compilation.dto;

import com.github.java_explore_with_me.main.event.dto.EventShortDto;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CompilationDto {
    private final Long id;
    private List<EventShortDto> events;
    private final boolean pinned;
    private final String title;
}