package com.house.biet.order.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryLocation {

    private static final double EARTH_RADIUS_METER = 6_371_000; // 지구 반지름 (m)

    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    private double latitude;
    private double longitude;
    private String region;

    public DeliveryLocation(double latitude, double longitude, String region) {
        validate(latitude, longitude, region);

        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
    }

    /**
     * 다른 위치까지의 거리 계산 (미터 단위)
     */
    public double distanceTo(DeliveryLocation target) {
        if (target == null) {
            throw new CustomException(ErrorCode.INVALID_DELIVERY_LOCATION);
        }

        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(target.latitude);
        double lon2 = Math.toRadians(target.longitude);

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METER * c;
    }

    private void validate(double latitude, double longitude, String region) {
        if (region == null || region.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_REGION_FORMAT);
        }

        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new CustomException(ErrorCode.INVALID_LATITUDE_RANGE);
        }

        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new CustomException(ErrorCode.INVALID_LONGITUDE_RANGE);
        }
    }
}
