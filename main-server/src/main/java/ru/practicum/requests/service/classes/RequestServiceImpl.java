package ru.practicum.requests.service.classes;

import ru.practicum.event.model.Event;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.event.enumerated.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.requests.service.interfaces.RequestService;
import ru.practicum.requests.status.Status;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto saveRequest(Long userId, Long eventId) {
        log.info("Запрос на создание запроса на участие в событии успешно передан в сервис RequestServiceImpl");
        Request request = new Request();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Участия с id: " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не существует"));
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Вы уже участвуете в этом событии");
        }
        if (event.getInitiator().getId().equals(userId)) {
            log.warn("Нельзя участвовать в собственном событии");
            throw new ConflictException("Нельзя участвовать в собственном событии");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            log.warn("Достигнут лимит запросов на участие");
            throw new ConflictException("Достигнут лимит запросов на участие");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            log.warn("Событие еще не было опубликовано");
            throw new ConflictException("Событие еще не было опубликовано");
        }
        request.setEvent(event);
        request.setRequester(user);
        if (event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(event.isRequestModeration() ? Status.PENDING : Status.CONFIRMED);
        }
        request.setCreated(LocalDateTime.now());
        request = requestRepository.save(request);
        if (request.getStatus() == Status.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        log.info("Пользователь с id: {} успешно создал запрос на участие в событии с id: {}", userId, eventId);
        return requestMapper.requestToParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        log.info("Запрос на обновление запроса на участие в событии успешно передан в сервис RequestServiceImpl");
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException
                        ("Пользователь еще не оставлял запрос на участие в событии с id: " + requestId));
        request.setStatus(Status.CANCELED);
        requestRepository.save(request);
        log.info("Запрос успешно отменен");
        return requestMapper.requestToParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        log.info("Запрос на получение списка запросов пользователя успешно передан в сервис RequestServiceImpl");
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id: " + userId + " не существует");
        }
        List<Request> userRequests = requestRepository.findAllByRequesterId(userId);
        List<ParticipationRequestDto> userRequestDtoList = requestMapper.requestListToParticipationRequestDtoList(
                userRequests);
        log.info("Список запросов: {} на участие для пользователя с id: {} успешно получен",
                userRequestDtoList, userId);
        return userRequestDtoList;
    }
}
