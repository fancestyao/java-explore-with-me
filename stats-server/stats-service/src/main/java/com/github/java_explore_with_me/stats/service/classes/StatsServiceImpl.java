package com.github.java_explore_with_me.stats.service.classes;

import com.github.java_explore_with_me.stats.input_dto.InputDTO;
import com.github.java_explore_with_me.stats.model.Hit;
import com.github.java_explore_with_me.stats.output_dto.OutputDTO;
import com.github.java_explore_with_me.stats.repository.HitRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.github.java_explore_with_me.stats.service.interfaces.StatsService;
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
        Hit hit = Hit.builder()
                .uri(inputDTO.getUri())
                .ip(inputDTO.getIp())
                .app(inputDTO.getApp())
                .timestamp(inputDTO.getTimestamp())
                .build();
        hitRepository.save(hit);
    }

    @Transactional
    @Override
    public List<OutputDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
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
        return stats;
    }
}
