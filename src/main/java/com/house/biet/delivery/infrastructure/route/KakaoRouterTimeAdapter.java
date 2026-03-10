package com.house.biet.delivery.infrastructure.route;

import com.house.biet.delivery.infrastructure.redis.RouteTimeCacheRepository;
import com.house.biet.delivery.port.RouteTimePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoRouterTimeAdapter implements RouteTimePort {

    private final KakaoRouteClient kakaoRouteClient;
    private final DistanceRouteCalculator distanceRouteCalculator;
    private final RouteTimeCacheRepository cacheRepository;

    @Override
    public int calculateDeliveryMinutes(
            double startLat,
            double startLng,
            double endLat,
            double endLng
    ) {

        String cacheKey = generateKey(startLat, startLng, endLat, endLng);
        Integer cached = cacheRepository.get(cacheKey);

        if (cached != null) {
            log.info("Route time cache hit");
            return cached;
        }

        try {

            int duration = kakaoRouteClient.requestRouteTime(
                    startLat,
                    startLng,
                    endLat,
                    endLng
            );

            log.info("Kakao API route time success : {} minutes", duration);

            cacheRepository.save(cacheKey, duration);

            return duration;

        } catch (Exception e) {

            log.warn("Kakao API failed. fallback to distance calculation", e);

            return distanceRouteCalculator.estimateMinutes(
                    startLat,
                    startLng,
                    endLat,
                    endLng
            );
        }
    }

    private String generateKey(
            double startLat,
            double startLng,
            double endLat,
            double endLng
    ) {
        double sLat = round(startLat);
        double sLng = round(startLng);
        double eLat = round(endLat);
        double eLng = round(endLng);

        return "route:%f:%f:%f:%f".formatted(
                sLat,
                sLng,
                eLat,
                eLng
        );
    }

    private double round(double value) {
        return Math.round(value * 1000) / 1000.0;
    }
}
