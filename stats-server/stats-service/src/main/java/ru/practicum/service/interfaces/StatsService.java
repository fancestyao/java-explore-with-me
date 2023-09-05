package ru.practicum.service.interfaces;

import ru.practicum.input_dto.InputDTO;
import ru.practicum.output_dto.OutputDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void saveHit(InputDTO inputHitDto);

    List<OutputDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean isUnique);
}
