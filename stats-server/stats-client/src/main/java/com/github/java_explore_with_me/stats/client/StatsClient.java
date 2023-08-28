package com.github.java_explore_with_me.stats.client;

import com.github.java_explore_with_me.stats.input_dto.InputDTO;
import com.github.java_explore_with_me.stats.output_dto.OutputDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StatsClient {
    private static final String STATS_URL = "http://localhost:9090";
    private final RestTemplate restTemplate;

    public StatsClient() {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    public void saveHit(InputDTO inputDTO) {
        final String url = STATS_URL + "/hit";
        restTemplate.postForEntity(url, inputDTO, Void.class);
    }

    public List<OutputDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        final String url = STATS_URL + "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        ResponseEntity<List<OutputDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                },
                Map.of("start", start, "end", end, "uris", uris, "unique", unique)
        );
        return response.getBody();
    }
}