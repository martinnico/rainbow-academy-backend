package com.rainbow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WordSearchWordRequestDTO(
        @NotNull Long activityId,
        @NotBlank String word
) {
}
