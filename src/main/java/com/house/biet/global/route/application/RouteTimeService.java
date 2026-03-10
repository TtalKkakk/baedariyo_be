package com.house.biet.global.route.application;

import com.house.biet.global.route.port.RouteTimePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteTimeService {

    private final RouteTimePort routeTimePort;

    public int calculateEstimatedDeliveryMinutes(
            double storeLat,
            double storeLng,
            double customerLat,
            double customerLng
    ) {

        return routeTimePort.calculateDeliveryMinutes(
                storeLat,
                storeLng,
                customerLat,
                customerLng
        );
    }
}
