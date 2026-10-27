package com.rainbow.api.dto;

import com.rainbow.api.entity.StudentProgressStatus;

public record CompleteLessonResponseDTO(
        Long lessonId,
        StudentProgressStatus status,
        Integer score,
        boolean passed,
        Long unlockedNextLessonId,
        boolean courseCompleted
) {
}
