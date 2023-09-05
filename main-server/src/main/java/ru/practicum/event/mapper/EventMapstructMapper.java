package ru.practicum.event.mapper;

import ru.practicum.event.dto.EventOutDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapstructMapper {
    EventOutDto eventToEventOutDto(Event event);

    List<EventShortDto> eventsToEventShortDtoList(List<Event> events);
}
