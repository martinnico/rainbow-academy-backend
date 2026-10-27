package com.rainbow.api.dto;

public record QuizQuestionResponseDTO(
        Long id,
        Long activityId,
        String questionText,
        Integer orderIndex
) {
}
