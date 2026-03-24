package com.house.biet.payment.query.application.dto;

import com.house.biet.common.domain.enums.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentDetailResponseDto(
        Long paymentId,
        Long orderId,
        Long userId,
        Integer amount,
        PaymentStatus status,
        String paymentKey,
        LocalDateTime createdAt
) {
}
