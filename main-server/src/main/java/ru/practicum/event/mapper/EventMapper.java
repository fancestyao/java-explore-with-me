package ru.practicum.event.mapper;

import ru.practicum.category.model.Category;
import ru.practicum.event.dto.UpdateEventUserDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.enumerated.State;

public interface EventMapper {
    Event updateEvent(Event eventForUpdate, Category categoryForUpdate, State eventStateForUpdate,
            UpdateEventUserDto updateEventUserDto);
}