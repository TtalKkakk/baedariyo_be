package com.house.biet.global.route.infrastructure;

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