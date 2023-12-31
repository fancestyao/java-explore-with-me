package ru.practicum.service.classes;

import ru.practicum.input_dto.InputDTO;
import ru.practicum.model.Hit;
import ru.practicum.repository.HitRepository;
import ru.practicum.service.interfaces.StatsService;
import ru.practicum.output_dto.OutputDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final HitRepository hitRepository;

    @Transactional
    @Override
    public void saveHit(InputDTO inputDTO) {
        log.info("Запрос на сохранение хита успешно поступил в сервис StatsServiceImpl");
        Hit hit = Hit.builder()
                .uri(inputDTO.getUri())
                .ip(inputDTO.getIp())
                .app(inputDTO.getApp())
                .timestamp(inputDTO.getTimestamp())
                .build();
        hitRepository.save(hit);
        log.info("Хит: {} успешно сохранен", hit);
    }

    @Transactional
    @Override
    public List<OutputDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.info("Запрос на получение статистики успешно поступил в сервис StatsServiceImpl");
        List<OutputDTO> stats = new ArrayList<>();

        if (!unique && uris != null) {
            stats = hitRepository.getStatsByUrisAndTimestamps(start, end, uris);
        }
        if (unique && uris != null) {
            stats = hitRepository.getUniqueStatsByUrisAndTimestamps(start, end, uris);
        }
        if (unique && uris == null) {
            stats = hitRepository.getAllUniqueStats(start, end);
        }
        if (!unique && uris == null) {
            stats = hitRepository.getAllStats(start, end);
        }
        log.info("Статистика успешно получена: {}", stats);
        return stats;
    }
}
