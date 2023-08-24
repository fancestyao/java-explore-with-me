package com.github.java_explore_with_me.stats.output_dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
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
