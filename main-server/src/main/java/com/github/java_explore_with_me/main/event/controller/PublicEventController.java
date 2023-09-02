package com.github.java_explore_with_me.main.event.controller;

import com.github.java_explore_with_me.main.event.dto.EventOutDto;
import com.github.java_explore_with_me.main.event.dto.EventShortDto;
import com.github.java_explore_with_me.main.event.enumerated.Sorting;
import com.github.java_explore_with_me.main.event.service.interfaces.EventService;
import com.github.java_explore_with_me.stats.client.StatsClient;
import com.github.java_explore_with_me.stats.input_dto.InputDTO;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final StatsClient statsClient;
    private final EventService eventService;

    @GetMapping("/{id}")
    public EventOutDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Поступил запрос на контроллер PublicEventController на получение события с id: {}", id);
        saveHit(request.getRequestURI(), request.getRemoteAddr());
        return eventService.getEvent(id, new String[]{request.getRequestURI()});
    }

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false) Sorting sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        log.info("Поступил запрос на контроллер PublicEventController" +
                        " на получение всех событий по данным: text: {}, paid: {}, categories: {}," +
                        " rangeStart: {}, rangeEnd: {}, from: {}, size: {}, onlyAvailable: {}, sort: {} ",
                text, paid, categories, rangeStart, rangeEnd, from, size, onlyAvailable, sort);
        saveHit(request.getRequestURI(), request.getRemoteAddr());
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    private void saveHit(String requestURI, String remoteAddr) {
        statsClient.saveHit(new InputDTO("java_explore_with_me_main", requestURI,
                remoteAddr,
                LocalDateTime.now()));
    }
}
