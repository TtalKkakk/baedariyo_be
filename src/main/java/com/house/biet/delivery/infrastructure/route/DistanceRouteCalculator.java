package com.house.biet.delivery.infrastructure.route;

import org.springframework.stereotype.Component;

@Component
public class DistanceRouteCalculator {

    private static final double EARTH_RADIUS = 6371; // km
    private static final double AVERAGE_SPEED = 20.0; // km/h

    public int estimateMinutes(
            double startLat,
            double startLng,
            double endLat,
            double endLng
    ) {

        double distance = haversine(startLat, startLng, endLat, endLng);

        return (int)((distance / AVERAGE_SPEED) * 60);
    }

    private double haversine(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}