package com.house.biet.delivery.command.domain.event;

public record DeliveryCanceledEvent(
        Long deliveryId,
        Long orderId,
        Long riderId
) {
}
