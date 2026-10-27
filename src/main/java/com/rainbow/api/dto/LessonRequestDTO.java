package com.rainbow.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LessonRequestDTO(
        @NotNull Long courseId,
        @NotNull @Min(1) Integer orderIndex,
        @NotBlank String title,
        @NotBlank String videoUrl,
        @NotBlank String description
) {
}
