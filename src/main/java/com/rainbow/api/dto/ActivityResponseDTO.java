package com.rainbow.api.dto;

import com.rainbow.api.entity.ActivityType;

import java.util.List;

public record ActivityResponseDTO(
        Long id,
        Long lessonId,
        ActivityType type,
        String title,
        Integer minPassScore,
        List<String> words,
        List<ActivityQuizQuestionResponseDTO> questions
) {
}
