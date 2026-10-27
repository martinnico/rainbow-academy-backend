package com.rainbow.api.dto;

import jakarta.validation.constraints.NotNull;

public record CompleteLessonRequestDTO(
        @NotNull Integer score
) {
}
