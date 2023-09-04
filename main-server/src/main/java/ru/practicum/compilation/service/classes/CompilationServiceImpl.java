package ru.practicum.compilation.service.classes;

import ru.practicum.event.mapper.EventMapstructMapper;
import ru.practicum.event.model.Event;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdatedCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.service.interfaces.CompilationService;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final EventMapstructMapper eventMapstructMapper;
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        log.info("Запрос на создание подборки успешно передан в сервис CompilationServiceImpl");
        List<Event> selectedEvents = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.dtoToCompilation(newCompilationDto);
        compilationMapper.setEventsToCompilation(compilation, selectedEvents);
        compilation = compilationRepository.save(compilation);
        CompilationDto compilationDto = compilationMapper.compilationtoCompilationDto(compilation);
        compilationDto.setEvents(eventMapstructMapper.eventsToEventShortDtoList(selectedEvents));
        log.info("Подборка: {} успешно сохранена", compilationDto);
        return compilationDto;
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdatedCompilationRequest updatedCompilationRequest) {
        log.info("Запрос на обновление подборки успешно передан в сервис CompilationServiceImpl");
        Compilation compilationToUpdate = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id: " + compId + " не найдена"));
        if (updatedCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updatedCompilationRequest.getEvents());
            compilationToUpdate.setEvents(events);
        }
        if (updatedCompilationRequest.getPinned() != null) {
            compilationToUpdate.setPinned(updatedCompilationRequest.getPinned());
        }
        if (updatedCompilationRequest.getTitle() != null) {
            compilationToUpdate.setTitle(updatedCompilationRequest.getTitle());
        }
        CompilationDto compilationDto = compilationMapper.compilationtoCompilationDto(
                compilationRepository.save(compilationToUpdate));
        compilationDto.setEvents(eventMapstructMapper.eventsToEventShortDtoList(compilationToUpdate.getEvents()));
        log.info("Подборка: {} успешно обновлена на: {}", compilationToUpdate, updatedCompilationRequest);
        return compilationDto;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.info("Запрос на удаление подборки успешно передан в сервис CompilationServiceImpl");
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id: " + compId + " не найдена"));
        compilationRepository.delete(compilation);
        log.info("Подборка с id: {} успешно удалена", compId);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        log.info("Запрос на получение подборки по id успешно передан в сервис CompilationServiceImpl");
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id: " + compId + " не найдена"));
        CompilationDto compilationDto = compilationMapper.compilationtoCompilationDto(compilation);
        compilationDto.setEvents(eventMapstructMapper.eventsToEventShortDtoList(compilation.getEvents()));
        log.info("Подборка: {} успешно получена", compilationDto);
        return compilationDto;
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        log.info("Запрос на получение списка подборок успешно передан в сервис CompilationServiceImpl");
        List<CompilationDto> compilationDtoList;
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (pinned == null) {
            compilationDtoList = compilationRepository.findAll(pageRequest)
                    .stream()
                    .map(compilationMapper::compilationtoCompilationDto)
                    .collect(Collectors.toList());
        } else {
            compilationDtoList = compilationRepository.findAllByPinned(pinned, pageRequest)
                    .stream()
                    .map(compilationMapper::compilationtoCompilationDto)
                    .collect(Collectors.toList());
        }
        log.info("Список подборок: {} успешно получен", compilationDtoList);
        return compilationDtoList;
    }
}
