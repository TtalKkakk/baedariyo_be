package com.house.biet.delivery.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurrentRiderLocation {

    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    private double latitude;
    private double longitude;

    public CurrentRiderLocation(double latitude, double longitude) {
        validate(latitude, longitude);

        this.latitude = latitude;
        this.longitude = longitude;
    }

    private void validate(double latitude, double longitude) {
        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new CustomException(ErrorCode.INVALID_LATITUDE_RANGE);
        }

        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new CustomException(ErrorCode.INVALID_LONGITUDE_RANGE);
        }
    }
}
