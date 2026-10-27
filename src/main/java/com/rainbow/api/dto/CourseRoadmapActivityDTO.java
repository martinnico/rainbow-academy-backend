package com.rainbow.api.dto;

import com.rainbow.api.entity.ActivityType;

public record CourseRoadmapActivityDTO(
        Long id,
        ActivityType type,
        String title,
        Integer minPassScore
) {
}
