package com.rainbow.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EbookRequestDTO(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        @NotBlank String filePath,
        @NotBlank String coverUrl
) {
}
