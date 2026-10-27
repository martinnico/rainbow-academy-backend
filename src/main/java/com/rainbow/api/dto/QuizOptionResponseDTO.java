package com.rainbow.api.dto;

public record QuizOptionResponseDTO(
        Long id,
        Long questionId,
        String optionText,
        boolean isCorrect
) {
}
