package com.github.java_explore_with_me.main.event.controller;

import com.github.java_explore_with_me.main.event.dto.EventOutDto;
import com.github.java_explore_with_me.main.event.dto.UpdateEventUserDto;
import com.github.java_explore_with_me.main.event.enumerated.State;
import com.github.java_explore_with_me.main.event.service.interfaces.EventService;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    List<EventOutDto> findEvents(@RequestParam(required = false) List<Long> users,
                                 @RequestParam(required = false) List<State> states,
                                 @RequestParam(required = false) List<Long> categories,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                 @RequestParam(defaultValue = "0") Integer from,
                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("Поступил запрос на контроллер AdminEventController" +
                " на получение всех событий по данным: users: {}, states: {}, categories: {}," +
                " rangeStart: {}, rangeEnd: {}, from: {}, size: {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.findEventsByAdmin(users, states, categories, rangeStart, rangeEnd,from,size);
    }

    @PatchMapping("/{eventId}")
    public EventOutDto updateEventState(@PathVariable Long eventId,
                                        @RequestBody @Valid UpdateEventUserDto updateEventUserDto) {
        log.info("Поступил запрос на контроллер AdminEventController" +
                " на обновление статуса события с id: {}, на updateEventUserDto: {}", eventId, updateEventUserDto);
        return eventService.publishOrCancelEvent(eventId, updateEventUserDto);
    }
}
