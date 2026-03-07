package com.house.biet.order.command.domain.event;

public record OrderConfirmedEvent(
        Long orderId
) {
}
