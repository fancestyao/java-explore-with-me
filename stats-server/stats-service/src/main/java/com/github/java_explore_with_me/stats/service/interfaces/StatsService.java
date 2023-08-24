package com.github.java_explore_with_me.stats.service.interfaces;

import com.github.java_explore_with_me.stats.input_dto.InputDTO;
import com.github.java_explore_with_me.stats.output_dto.OutputDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void saveHit(InputDTO inputHitDto);

    List<OutputDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean isUnique);
}
