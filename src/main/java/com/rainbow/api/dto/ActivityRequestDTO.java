package com.rainbow.api.dto;

import com.rainbow.api.entity.ActivityType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ActivityRequestDTO(
        @NotNull Long lessonId,
        @NotNull ActivityType type,
        @NotBlank String title,
        @NotNull @Min(0) Integer minPassScore,
        List<String> words,
        List<ActivityQuizQuestionRequestDTO> questions
) {
}
