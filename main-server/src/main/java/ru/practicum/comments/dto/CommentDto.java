package ru.practicum.comments.dto;

import lombok.*;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {
    private Long id;
    private String content;
    private LocalDateTime created;
    private UserDto user;
    private EventShortDto event;
}