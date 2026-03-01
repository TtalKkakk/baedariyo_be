package com.house.biet.delivery.infrastructure.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryLocationCache implements Serializable {

    private Long riderId;
    private double latitude;
    private double longitude;
    private LocalDateTime updatedAt;
}
