package com.rainbow.api.dto;

public record ActivityQuizOptionResponseDTO(
        Long id,
        String optionText,
        boolean isCorrect
) {
}
