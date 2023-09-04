package ru.practicum.requests.controller;

import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.service.interfaces.RequestService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto saveRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        log.info("Поступил запрос на контроллер RequestController на создание запроса для события с id: {}," +
                " от пользователя с id: {}", eventId, userId);
        return requestService.saveRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Поступил запрос на контроллер RequestController на изменение запроса с id: {}," +
                " от пользователя с id: {}", requestId, userId);
        return requestService.cancelParticipationRequest(userId, requestId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        log.info("Поступил запрос на контроллер RequestController" +
                " на получение списка запросов для пользователя с id: {}", userId);
        return requestService.getUserRequests(userId);
    }
}
