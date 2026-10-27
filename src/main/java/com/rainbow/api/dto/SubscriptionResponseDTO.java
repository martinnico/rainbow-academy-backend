package com.rainbow.api.dto;

import com.rainbow.api.entity.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionResponseDTO(
        Long id,
        Long studentId,
        Long courseId,
        SubscriptionStatus status,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal amountPaid
) {
}
