package com.github.java_explore_with_me.main.requests.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.java_explore_with_me.main.requests.status.Status;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private Status status;
}
