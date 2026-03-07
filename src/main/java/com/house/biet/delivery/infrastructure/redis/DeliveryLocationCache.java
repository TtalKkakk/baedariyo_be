package com.house.biet.delivery.infrastructure.redis;

import java.io.Serializable;
import java.time.LocalDateTime;

public record DeliveryLocationCache(
        Long riderId,
        double latitude,
        double longitude,
        LocalDateTime updatedAt
) implements Serializable {}