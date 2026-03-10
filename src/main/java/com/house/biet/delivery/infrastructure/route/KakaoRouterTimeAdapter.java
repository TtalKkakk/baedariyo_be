package com.house.biet.delivery.infrastructure.route;

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

    @Override
    public int calculateDeliveryMinutes(
            double startLat,
            double startLng,
            double endLat,
            double endLng
    ) {

        try {

            int duration = kakaoRouteClient.requestRouteTime(
                    startLat,
                    startLng,
                    endLat,
                    endLng
            );

            log.info("Kakao API route time success : {} minutes", duration);

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
}
