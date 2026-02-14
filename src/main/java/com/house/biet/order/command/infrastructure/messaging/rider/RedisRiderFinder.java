package com.house.biet.order.command.infrastructure.messaging.rider;

import com.house.biet.order.command.application.port.RiderCandidate;
import com.house.biet.order.command.application.port.RiderFinder;
import com.house.biet.order.command.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("redis")
@RequiredArgsConstructor
public class RedisRiderFinder implements RiderFinder {

    private final StringRedisTemplate redisTemplate;

    @Override
    public List<RiderCandidate> findNearby(
            OrderCreatedEvent.PickupLocationDto pickupLocationDto,
            double radiusKm
    ) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();

        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                geoOps.radius(
                        "rider:location",
                        new Circle(
                                new Point(
                                        pickupLocationDto.getLongitude(),
                                        pickupLocationDto.getLatitude()
                                ),
                                new Distance(radiusKm, Metrics.KILOMETERS)
                        )
                );

        if (results == null)
            return List.of();

        return results.getContent().stream()
                .map(r -> new RiderCandidate(
                        Long.valueOf(r.getContent().getName()),
                        r.getDistance().getValue()
                ))
                .toList();
    }
}
