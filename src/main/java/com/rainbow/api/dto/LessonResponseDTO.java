package com.rainbow.api.dto;

public record LessonResponseDTO(
        Long id,
        Long courseId,
        Integer orderIndex,
        String title,
        String videoUrl,
        String description
) {
}
