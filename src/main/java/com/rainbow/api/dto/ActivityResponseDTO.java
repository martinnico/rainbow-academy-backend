package com.rainbow.api.dto;

import com.rainbow.api.entity.ActivityType;

public record ActivityResponseDTO(
        Long id,
        Long lessonId,
        ActivityType type,
        String title,
        Integer minPassScore
) {
}
