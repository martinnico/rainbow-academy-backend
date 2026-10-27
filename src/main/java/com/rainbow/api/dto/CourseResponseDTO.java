package com.rainbow.api.dto;

import com.rainbow.api.entity.CourseLevel;

import java.math.BigDecimal;

public record CourseResponseDTO(
        Long id,
        String title,
        String description,
        CourseLevel level,
        BigDecimal monthlyPrice,
        boolean active
) {
}
