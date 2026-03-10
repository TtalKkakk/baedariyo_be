package com.house.biet.delivery.infrastructure.route;

import java.util.List;

public record KakaoDirectionsResponse(
        List<Route> routes
) {

    public record Route(
            Summary summary
    ) {}

    public record Summary(
            int distance,
            int duration
    ) {}
}