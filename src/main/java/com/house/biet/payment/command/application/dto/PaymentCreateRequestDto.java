package com.house.biet.payment.command.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentCreateRequestDto(

        @NotNull(message = "주문 ID는 필수입니다.")
        Long orderId,

        @NotNull(message = "결제 금액은 필수입니다.")
        @Positive(message = "결제 금액은 0보다 커야 합니다.")
        @Max(value = 2_100_000_000, message = "결제 금액은 21억 원을 초과할 수 없습니다.")
        Integer amount,

        @NotBlank(message = "paymentKey는 필수입니다.")
        String paymentKey
) {
}