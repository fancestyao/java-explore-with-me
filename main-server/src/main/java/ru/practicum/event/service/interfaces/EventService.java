package ru.practicum.event.service.interfaces;

import ru.practicum.event.dto.*;
import ru.practicum.event.enumerated.Sorting;
import ru.practicum.event.enumerated.State;
import ru.practicum.requests.dto.ParticipationRequestDto;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventOutDto saveEvent(NewEventDto newEventDto, Long userId);

    EventOutDto patchEvent(Long userId, Long eventId, UpdateEventUserDto updateEventUserDto);

    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventOutDto getUserEvent(Long userId, Long eventId);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestsStatus(Long userId, Long eventId,
            EventRequestStatusUpdateRequest statusUpdateRequest);

    EventOutDto publishOrCancelEvent(Long eventId, UpdateEventUserDto updateEventAdminDto);

    EventOutDto getEvent(Long eventId, String[] uris);

    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable, Sorting sort, int from, int size);

    List<EventOutDto> findEventsByAdmin(List<Long> users, List<State> states, List<Long> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Integer from, Integer size);
}
