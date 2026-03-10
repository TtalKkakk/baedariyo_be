package com.house.biet.global.geocoding.infrastructure;

public record VWorldResponse(
        Response response
) {

    public record Response(
            String status,
            Result result
    ) {}

    public record Result(
            Point point
    ) {}

    public record Point(
            String x,
            String y
    ) {}
}
