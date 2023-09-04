package ru.practicum.output_dto;

import lombok.*;

@Data
@NoArgsConstructor
@Builder
public class OutputDTO {
    private String app;
    private String uri;
    private Long hits;

    public OutputDTO(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
