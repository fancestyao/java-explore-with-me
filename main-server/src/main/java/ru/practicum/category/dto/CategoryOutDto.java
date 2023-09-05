package ru.practicum.category.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CategoryOutDto {
    private final Long id;
    private final String name;
}
