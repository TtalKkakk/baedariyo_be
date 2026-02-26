package com.house.biet.payment.command.application.dto;

import jakarta.validation.constraints.NotNull;

public record PaymentApproveRequestDto(

        @NotNull(message = "transaction ID 는 필수입니다")
        String transactionId
) {
}
