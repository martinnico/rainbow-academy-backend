package com.rainbow.api.dto;

import com.rainbow.api.entity.SubscriptionStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionRequestDTO(
        @NotNull Long studentId,
        @NotNull Long courseId,
        @NotNull SubscriptionStatus status,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @DecimalMin("0.0") BigDecimal amountPaid
) {
}
