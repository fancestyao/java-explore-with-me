package com.github.java_explore_with_me.main.event.mapper;

import com.github.java_explore_with_me.main.category.model.Category;
import com.github.java_explore_with_me.main.event.dto.UpdateEventUserDto;
import com.github.java_explore_with_me.main.event.model.Event;
import com.github.java_explore_with_me.main.event.enumerated.State;

public interface EventMapper {
    Event updateEvent(Event eventForUpdate, Category categoryForUpdate, State eventStateForUpdate,
            UpdateEventUserDto updateEventUserDto);
}