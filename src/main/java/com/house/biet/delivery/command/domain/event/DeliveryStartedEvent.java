package com.house.biet.delivery.command.domain.event;

public record DeliveryStartedEvent(
        Long deliveryId,
        Long orderId,
        Long riderId
) {
}
