package com.rainbow.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EbookPurchaseResponseDTO(
        Long id,
        Long studentId,
        Long ebookId,
        LocalDateTime purchaseDate,
        BigDecimal amountPaid
) {
}
