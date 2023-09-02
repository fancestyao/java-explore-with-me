package com.github.java_explore_with_me.stats.controller;


import com.github.java_explore_with_me.stats.input_dto.InputDTO;
import com.github.java_explore_with_me.stats.output_dto.OutputDTO;
import com.github.java_explore_with_me.stats.service.interfaces.StatsService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public void saveHit(@RequestBody InputDTO inputDTO) {
        log.info("Запрос на сохранение хита с inputDTO: {}", inputDTO);
        statsService.saveHit(inputDTO);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<OutputDTO>> getStats(
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Запрос на получение статистики start: {}, end: {}, uris: {}, unique: {}", start, end, uris, unique);
        if (start.isAfter(end)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(statsService.getStats(start, end, uris, unique));
    }
}
