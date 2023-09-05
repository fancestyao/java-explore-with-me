package ru.practicum.event.controller;

import org.springframework.validation.annotation.Validated;
import ru.practicum.event.dto.EventOutDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserDto;
import ru.practicum.event.service.interfaces.EventService;
import ru.practicum.exception.BadRequestException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventOutDto createEvent(@RequestBody @Valid NewEventDto newEventDto,
                                   @PathVariable Long userId) {
        log.info("Поступил запрос на контроллер PrivateEventController" +
                " на создание события: {} от пользователя с id: {}", newEventDto, userId);
        dateTimeValidate(newEventDto.getEventDate());
        return eventService.saveEvent(newEventDto, userId);
    }

    @PatchMapping("/{eventId}")
    public EventOutDto patchEvent(@PathVariable Long userId,
                                  @PathVariable Long eventId,
                                  @RequestBody @Valid UpdateEventUserDto updateEventUserDto) {
        log.info("Поступил запрос на контроллер PrivateEventController" +
                " на обновление события с id: {} от пользователя с id: {} на updateEventUserDto: {}",
                eventId, userId, updateEventUserDto);
        if (updateEventUserDto.getEventDate() != null) {
            dateTimeValidate(updateEventUserDto.getEventDate());
        }
        return eventService.patchEvent(userId, eventId, updateEventUserDto);
    }

    @GetMapping
    public List<EventShortDto> getUsersEvents(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Поступил запрос на контроллер PrivateEventController" +
                " на получение всех событий по параметрам: from: {}, size: {} для" +
                " пользователя с id: {}", from, size, userId);
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventOutDto getUserEventById(@PathVariable Long userId,
                                        @PathVariable Long eventId) {
        log.info("Поступил запрос на контроллер PrivateEventController " +
                "на получение события с id: {} для пользователя с id: {}", eventId, userId);
        return eventService.getUserEvent(userId, eventId);
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        log.info("Поступил запрос на контроллер PrivateEventController" +
                " на получение списка запросов на событие с id: {} " +
                "для пользователя с id: {}", eventId, userId);
        return eventService.getEventRequests(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest
                                                                           statusUpdateRequest) {
        log.info("Поступил запрос на контроллер PrivateEventController " +
                "на изменение статуса statusUpdateRequest: {} запросов на событие с id: {} " +
                "для пользователя с id: {}", statusUpdateRequest, eventId, userId);
        return eventService.changeRequestsStatus(userId, eventId, statusUpdateRequest);
    }

    private void dateTimeValidate(LocalDateTime localDateTime) {
        log.info("Валидируем время создания");
        if (localDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            log.warn("Нельзя создать событие раньше чем за два часа от текущей даты");
            throw new BadRequestException("Вы не можете добавить событие," +
                    " которое проходит раньше чем за два часа от текущей даты");
        }
    }
}
