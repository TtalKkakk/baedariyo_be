package com.house.biet.payment.query.application.dto;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.command.domain.aggregate.Payment;

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

    public static PaymentDetailResponseDto from(Payment payment) {
        return new PaymentDetailResponseDto(
                payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getMoney().getAmount(),
                payment.getStatus(),
                payment.getPaymentKey().getValue(),
                payment.getCreatedAt()
        );
    }
}