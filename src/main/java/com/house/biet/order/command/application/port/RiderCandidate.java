package com.house.biet.order.command.application.port;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RiderCandidate {

    private final Long riderId;
    private final double distanceKm;
}

