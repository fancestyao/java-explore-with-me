package com.github.java_explore_with_me.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.java_explore_with_me.main.category.dto.CategoryOutDto;
import com.github.java_explore_with_me.main.user.dto.UserDto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EventShortDto {
    private String annotation;
    private CategoryOutDto category;
    private Long confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserDto initiator;
    private boolean paid;
    private String title;
    private Long views;
}
