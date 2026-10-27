package com.rainbow.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ActivityQuizQuestionRequestDTO(
        @NotBlank String questionText,
        @NotNull @Min(1) Integer orderIndex,
        List<ActivityQuizOptionRequestDTO> options
) {
}
