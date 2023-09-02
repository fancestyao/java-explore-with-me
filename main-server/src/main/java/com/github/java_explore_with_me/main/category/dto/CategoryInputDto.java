package com.github.java_explore_with_me.main.category.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInputDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
