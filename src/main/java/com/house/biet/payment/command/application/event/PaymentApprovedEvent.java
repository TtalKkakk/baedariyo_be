package com.house.biet.payment.command.application.event;

public record PaymentApprovedEvent(
        Long transactionId,
        Long orderId
) {
}
