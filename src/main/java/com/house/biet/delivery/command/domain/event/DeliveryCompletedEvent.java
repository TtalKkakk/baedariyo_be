package com.house.biet.delivery.command.domain.event;

public record DeliveryCompletedEvent(
        Long deliveryId,
        Long orderId,
        Long riderId
) {}