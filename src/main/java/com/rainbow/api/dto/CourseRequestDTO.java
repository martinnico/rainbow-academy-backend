package com.rainbow.api.dto;

import com.rainbow.api.entity.CourseLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CourseRequestDTO(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull CourseLevel level,
        @NotNull @DecimalMin("0.0") BigDecimal monthlyPrice,
        @NotNull Boolean active
) {
}
