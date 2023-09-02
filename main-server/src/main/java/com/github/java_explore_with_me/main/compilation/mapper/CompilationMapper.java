package com.github.java_explore_with_me.main.compilation.mapper;

import com.github.java_explore_with_me.main.compilation.dto.CompilationDto;
import com.github.java_explore_with_me.main.compilation.dto.NewCompilationDto;
import com.github.java_explore_with_me.main.compilation.model.Compilation;
import com.github.java_explore_with_me.main.event.model.Event;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class CompilationMapper {
    @Mapping(target = "events", ignore = true)
    public abstract Compilation dtoToCompilation(NewCompilationDto newCompilationDto);

    @AfterMapping
    public void setEventsToCompilation(Compilation compilation, @MappingTarget List<Event> events) {
        compilation.setEvents(events);
    }

    public abstract CompilationDto compilationtoCompilationDto(Compilation compilation);

    @AfterMapping
    protected void checkEventsIsNull(Compilation compilation, @MappingTarget CompilationDto dto) {
        if (compilation.getEvents() == null || compilation.getEvents().isEmpty()) {
            dto.setEvents(new ArrayList<>());
        }
    }
}
