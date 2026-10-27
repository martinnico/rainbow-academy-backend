package com.rainbow.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuizQuestionRequestDTO(
        @NotNull Long activityId,
        @NotBlank String questionText,
        @NotNull @Min(1) Integer orderIndex
) {
}
