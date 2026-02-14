package com.house.biet.order.command.infrastructure.messaging.rider;

public record RiderCallMessage(
        Long orderId,
        Long riderId
) {
}
