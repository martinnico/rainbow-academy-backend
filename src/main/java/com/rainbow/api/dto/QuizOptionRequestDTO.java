package com.rainbow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuizOptionRequestDTO(
        @NotNull Long questionId,
        @NotBlank String optionText,
        @NotNull Boolean isCorrect
) {
}
