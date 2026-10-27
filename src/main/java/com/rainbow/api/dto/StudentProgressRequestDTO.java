package com.rainbow.api.dto;

import com.rainbow.api.entity.StudentProgressStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record StudentProgressRequestDTO(
        @NotNull Long studentId,
        @NotNull Long lessonId,
        @NotNull StudentProgressStatus status,
        @Min(0) Integer score,
        LocalDateTime completedAt
) {
}
