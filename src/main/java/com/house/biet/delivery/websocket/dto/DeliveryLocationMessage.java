package com.house.biet.delivery.websocket.dto;

public record DeliveryLocationMessage(
        Long orderId,
        Long riderId,
        double latitude,
        double longitude,
        long timestamp
) {
}