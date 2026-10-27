package com.rainbow.api.dto;

import com.rainbow.api.entity.StudentProgressStatus;

import java.time.LocalDateTime;

public record StudentProgressResponseDTO(
        Long id,
        Long studentId,
        Long lessonId,
        StudentProgressStatus status,
        Integer score,
        LocalDateTime completedAt
) {
}
