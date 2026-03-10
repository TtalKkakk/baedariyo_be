package com.house.biet.delivery.port;

public interface RouteTimePort {

    int calculateDeliveryMinutes(
            double startLat,
            double startLng,
            double endLat,
            double endLng
    );

}
