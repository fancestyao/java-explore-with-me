package com.github.java_explore_with_me.main.event.mapper;

import com.github.java_explore_with_me.main.event.dto.EventOutDto;
import com.github.java_explore_with_me.main.event.dto.EventShortDto;
import com.github.java_explore_with_me.main.event.model.Event;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapstructMapper {
    EventOutDto eventToEventOutDto(Event event);

    List<EventShortDto> eventsToEventShortDtoList(List<Event> events);
}
