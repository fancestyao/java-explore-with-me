package ru.practicum.compilation.dto;

import java.util.Set;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class UpdatedCompilationRequest {
    private final Set<Long> events;
    private final Boolean pinned;
    @Size(min = 1, max = 50)
    private final String title;
}
