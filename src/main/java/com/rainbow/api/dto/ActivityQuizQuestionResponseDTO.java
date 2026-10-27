package com.rainbow.api.dto;

import java.util.List;

public record ActivityQuizQuestionResponseDTO(
        Long id,
        String questionText,
        Integer orderIndex,
        List<ActivityQuizOptionResponseDTO> options
) {
}
