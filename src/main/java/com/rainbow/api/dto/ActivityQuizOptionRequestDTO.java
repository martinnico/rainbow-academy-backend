package com.rainbow.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ActivityQuizOptionRequestDTO(
        @NotBlank String optionText,
        boolean isCorrect
) {
}
