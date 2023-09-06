package ru.practicum.comments.dto;

import lombok.*;
import ru.practicum.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdatedCommentDto {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 3000)
    private String content;
    private LocalDateTime updated;
    private EventShortDto event;
}