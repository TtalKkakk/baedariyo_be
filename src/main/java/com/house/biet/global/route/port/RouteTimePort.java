package com.house.biet.global.route.port;

public interface RouteTimePort {

    int calculateDeliveryMinutes(
            double startLat,
            double startLng,
            double endLat,
            double endLng
    );

}
