package com.rainbow.api.dto;

import java.math.BigDecimal;

public record EbookResponseDTO(
        Long id,
        String title,
        String description,
        BigDecimal price,
        String filePath,
        String coverUrl
) {
}
