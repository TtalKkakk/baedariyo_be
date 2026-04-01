package com.house.biet.global.route.application;

import com.house.biet.global.route.port.RouteTimePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteTimeService {

    private final RouteTimePort routeTimePort;

    /**
     * Estimated 배달 Minutes을 처리한다
     *
     * @param storeLat storeLat 값
     * @param storeLng storeLng 값
     * @param customerLat customerLat 값
     * @param customerLng customerLng 값
     * @return 처리 결과 값
     */
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
