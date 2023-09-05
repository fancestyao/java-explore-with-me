package ru.practicum.event.service.classes;

import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.enumerated.Sorting;
import ru.practicum.event.enumerated.State;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.mapper.EventMapstructMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.event.service.interfaces.EventService;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.requests.status.Status;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.output_dto.OutputDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final EventMapstructMapper eventMapstructMapper;
    private final EventMapper eventMapperImpl;
    private final RequestMapper requestMapper;
    private final StatsClient statsClient;

    @Override
    @Transactional
    public EventOutDto saveEvent(NewEventDto newEventDto, Long userId) {
        log.info("Запрос на добавление события успешно передан в сервис EventServiceImpl");
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория: " + newEventDto.getCategory() + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не существует"));
        Location eventLocation = locationRepository.save(newEventDto.getLocation());
        Event newEvent = Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(user)
                .location(eventLocation)
                .paid(newEventDto.getPaid() != null && newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .confirmedRequests(0L)
                .views(0L)
                .build();
        newEvent = eventRepository.save(newEvent);
        log.info("Событие: {} успешно сохранено", newEvent.getId());
        return eventMapstructMapper.eventToEventOutDto(newEvent);
    }

    @Override
    @Transactional
    public EventOutDto patchEvent(Long userId, Long eventId, UpdateEventUserDto updateEventUserDto) {
        log.info("Запрос на обновление события успешно передан в сервис EventServiceImpl");
        Event event = eventRepository.findEventByIdWithCategoryAndLocation(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено"));
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие не должно быть опубликовано");
        }
        if (!event.getState().equals(State.PENDING) && !event.getState().equals(State.CANCELED)) {
            throw new ConflictException("Обновлять событие можно лишь в статусах: pending (на рассмотрении) и" +
                    " (canceled) отменено");
        }
        Category categoryForUpdate;
        if (updateEventUserDto.getCategory() != null) {
            categoryForUpdate = categoryRepository.findById(updateEventUserDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категории с id: " +
                            updateEventUserDto.getCategory() + " не существует"));
        } else {
            categoryForUpdate = event.getCategory();
        }
        State eventStateToUpdate = Optional.ofNullable(updateEventUserDto.getStateAction())
                .filter(state -> state.equals(State.REJECT_EVENT) || state.equals(State.CANCEL_REVIEW))
                .map(state -> State.CANCELED)
                .orElse(State.PENDING);
        event = eventMapperImpl.updateEvent(event, categoryForUpdate, eventStateToUpdate, updateEventUserDto);
        event = eventRepository.save(event);
        EventOutDto eventOutDto = eventMapstructMapper.eventToEventOutDto(event);
        log.info("Событие успешно изменено");
        return eventOutDto;
    }

    @Override
    @Transactional
    public EventOutDto publishOrCancelEvent(Long eventId, UpdateEventUserDto updateEventUserDto) {
        log.info("Запрос на публикацию или отмену события успешно передан в сервис EventServiceImpl");
        LocalDateTime publicationDate = LocalDateTime.now();
        if (updateEventUserDto.getEventDate() != null && updateEventUserDto.getEventDate()
                .isBefore(publicationDate.plusHours(1))
        ) {
            log.warn("Дата начала события должна быть позже даты публикации");
            throw new BadRequestException("Дата начала события должна быть позже даты публикации");
        }
        Event event = eventRepository.findEventByIdWithCategoryAndLocation(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не существует"));
        if (event.getState().equals(State.PUBLISHED) && updateEventUserDto.getStateAction()
                .equals(State.REJECT_EVENT)) {
            log.warn("Только неопубликованные события можно отклонять");
            throw new ConflictException("Только неопубликованные события можно отклонять");
        }
        if (!event.getState().equals(State.PENDING)) {
            log.warn("К публикации подходят события только с соответствующим статусом");
            throw new ConflictException("К публикации подходят события только с соответствующим статусом");
        }
        if (updateEventUserDto.getStateAction() == null) {
            updateEventUserDto.setStateAction(event.getState());
        }
        if (updateEventUserDto.getStateAction().equals(State.REJECT_EVENT)) {
            updateEventUserDto.setStateAction(State.CANCELED);
        }
        if (updateEventUserDto.getStateAction().equals(State.PUBLISH_EVENT)) {
            updateEventUserDto.setStateAction(State.PUBLISHED);
        }
        Category categoryForUpdate = null;
        if (updateEventUserDto.getCategory() != null) {
            categoryForUpdate = categoryRepository.findById(updateEventUserDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категории не существует"));
        }
        event = eventMapperImpl.updateEvent(event, categoryForUpdate, updateEventUserDto.getStateAction(),
                updateEventUserDto);
        event.setPublishedOn(publicationDate);
        event = eventRepository.save(event);
        EventOutDto eventOutDto = eventMapstructMapper.eventToEventOutDto(event);
        log.info("Событию: {} присвоен статус: {}", event, event.getState());
        return eventOutDto;
    }


    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        log.info("Запрос на получение событий пользователя успешно передан в сервис EventServiceImpl");
        PageRequest pagination = PageRequest.of(from / size, size);
        List<Event> allInitiatorEvents = eventRepository.findAllByInitiatorId(userId, pagination);
        List<EventShortDto> userEventsShortDtoList = eventMapstructMapper.eventsToEventShortDtoList(allInitiatorEvents);
        log.info("Пользователь с id: {} успешно получил список своих событий: {}", userId, userEventsShortDtoList);
        return userEventsShortDtoList;
    }

    @Override
    public EventOutDto getUserEvent(Long userId, Long eventId) {
        log.info("Запрос на получение события пользователя по id успешно передан в сервис EventServiceImpl");
        Event event = eventRepository.findEventByIdAndInitiatorId(eventId, userId);
        if (event == null) {
            log.warn("Событие с id: {} не существует", eventId);
            throw new NotFoundException("Событие с id: " + eventId + " не существует");
        }
        EventOutDto eventOutDto = eventMapstructMapper.eventToEventOutDto(event);
        log.info("Пользователь с id: {} успешно получил событие с id: {}", userId, eventId);
        return eventOutDto;
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        log.info("Запрос на получение запросов на участие в событиях успешно передан в сервис EventServiceImpl");
        List<ParticipationRequestDto> eventParticipationRequests = requestRepository.findAllRequestForEvent(userId,
                eventId);
        log.info("Успешно получен список запросов на участие в событии с id: {} для пользователя с id: {}",
                eventId, userId);
        return eventParticipationRequests;
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeRequestsStatus(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest statusUpdateRequest) {
        log.info("Запрос на изменение статуса события успешно передан в сервис EventServiceImpl");
        List<Request> requests = requestRepository.findAllByIdInAndEventInitiatorIdAndEventId(
                        statusUpdateRequest.getRequestIds(), userId,
                        eventId).stream()
                .peek(request -> {
                    if (request.getStatus() == Status.CONFIRMED && statusUpdateRequest.getStatus() == Status.REJECTED) {
                        log.warn("Нельзя отменить уже принятую заявку");
                        throw new ConflictException("Нельзя отменить уже принятую заявку");
                    }
                    request.setStatus(statusUpdateRequest.getStatus());
                })
                .collect(Collectors.toList());
        List<ParticipationRequestDto> participationRequests = requests.stream()
                .peek(request -> request.setStatus(statusUpdateRequest.getStatus()))
                .map(requestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());
        EventRequestStatusUpdateResult updatedRequestsStatuses;
        if (statusUpdateRequest.getStatus().equals(Status.CONFIRMED)) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException("Событие с id= " + eventId + " не найдено"));
            long confirmedRequests = event.getConfirmedRequests() + statusUpdateRequest.getRequestIds().size();
            if (event.getParticipantLimit() != 0 && confirmedRequests > event.getParticipantLimit()) {
                long cancelCount = confirmedRequests - event.getParticipantLimit();
                List<Request> pendingRequests = requestRepository.findByEventIdAndStatus(eventId, Status.PENDING);
                if (pendingRequests.size() < cancelCount) {
                    throw new ConflictException(
                            "Нельзя добавлять новых участников." + "\n" + event.getConfirmedRequests() + " из "
                                    + event.getParticipantLimit() + " запланированных");
                }
                for (int i = 0; i < cancelCount; i++) {
                    Request requestToCancel = pendingRequests.get(i);
                    requestToCancel.setStatus(Status.REJECTED);
                    requests.add(requestToCancel);
                }
            }
            event.setConfirmedRequests(confirmedRequests);
            eventRepository.save(event);
            requestRepository.saveAll(requests);
            updatedRequestsStatuses = new EventRequestStatusUpdateResult(participationRequests, null);
        } else {
            updatedRequestsStatuses = new EventRequestStatusUpdateResult(null, participationRequests);
            requestRepository.saveAll(requests);
        }
        log.info("Статус успешно изменен");
        return updatedRequestsStatuses;
    }

    @Override
    public List<EventOutDto> findEventsByAdmin(List<Long> users, List<State> states, List<Long> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("Запрос на поиск события от админа успешно передан в сервис EventServiceImpl");
        PageRequest pagination = PageRequest.of(from / size,
                size);
        LocalDateTime start;
        LocalDateTime end;
        if (rangeStart == null || rangeEnd == null) {
            start = LocalDateTime.now();
            end = start.plusYears(1);
        } else {
            start = rangeStart;
            end = rangeEnd;
        }
        List<EventOutDto> eventsByEventParamAndPaginationParams = eventRepository
                .findEventsByEventParamAndPaginationParams(
                        users,
                        states, categories, start,
                        end, pagination).stream()
                .map(eventMapstructMapper::eventToEventOutDto)
                .collect(Collectors.toList());
        log.info("Успешно получен список событий: {}", eventsByEventParamAndPaginationParams);
        return eventsByEventParamAndPaginationParams;
    }

    @Override
    @Transactional
    public EventOutDto getEvent(Long eventId, String[] uris) {
        log.info("Запрос на получение события успешно передан в сервис EventServiceImpl");
        Event event = eventRepository.findEventByIdWithCategoryAndLocation(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не существует"));
        if (event.getState() != State.PUBLISHED) {
            log.warn("Событие с id: {} не существует", eventId);
            throw new NotFoundException("Событие с id: " + eventId + " не существует");
        }
        Long eventStats = getViews(event.getPublishedOn(), LocalDateTime.now(), uris);
        event.setViews(eventStats);
        event = eventRepository.save(event);
        EventOutDto eventOut = eventMapstructMapper.eventToEventOutDto(event);
        log.info("Событие eventOut: {} успешно получено", eventOut);
        return eventOut;
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, boolean onlyAvailable, Sorting sort, int from, int size) {
        log.info("Запрос на получение событий успешно передан в сервис EventServiceImpl");
        PageRequest pageRequest;
        LocalDateTime start;
        LocalDateTime end;
        Sort sorting;
        if (rangeStart == null || rangeEnd == null) {
            start = LocalDateTime.now();
            end = start.plusYears(1);
        } else {
            start = rangeStart;
            end = rangeEnd;
        }
        if (sort == null || sort.equals(Sorting.EVENT_DATE)) {
            sorting = Sort.by("eventDate").descending();
        } else {
            sorting = Sort.by("views").descending();
        }
        pageRequest = PageRequest.of(from / size, size, sorting);
        List<Event> eventsList;
        if (onlyAvailable) {
            eventsList = eventRepository.getOnlyAvailableEvents(text,
                    categories, paid,
                    start, end,
                    pageRequest);
        } else {
            eventsList = eventRepository.getEvents(text,
                    categories, paid,
                    start, end,
                    pageRequest);
        }
        if (eventsList.isEmpty() || !eventRepository.existsByState(State.PUBLISHED)) {
            log.warn("Подходящих событий не существует");
            throw new BadRequestException("Подходящих событий не существует");
        }
        List<EventShortDto> listOfShortEventDto = eventMapstructMapper.eventsToEventShortDtoList(eventsList);
        log.info("Список событий успешно получен: {}", listOfShortEventDto);
        return listOfShortEventDto;
    }

    private long getViews(LocalDateTime start, LocalDateTime end, String[] uris) {
        log.info("Получаем просмотры");
        List<OutputDTO> eventStats = statsClient.getStats(start, end, uris, true);
        return eventStats.get(0).getHits();
    }
}
