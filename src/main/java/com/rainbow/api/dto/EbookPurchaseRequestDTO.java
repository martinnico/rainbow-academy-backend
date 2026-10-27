package com.rainbow.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EbookPurchaseRequestDTO(
        @NotNull Long studentId,
        @NotNull Long ebookId,
        @NotNull LocalDateTime purchaseDate,
        @NotNull @DecimalMin("0.0") BigDecimal amountPaid
) {
}
