package com.rainbow.api.dto;

import com.rainbow.api.entity.StudentProgressStatus;

public record CourseRoadmapLessonDTO(
        Long lessonId,
        Integer orderIndex,
        String title,
        String description,
        StudentProgressStatus status,
        Integer score,
        CourseRoadmapActivityDTO activity
) {
}
