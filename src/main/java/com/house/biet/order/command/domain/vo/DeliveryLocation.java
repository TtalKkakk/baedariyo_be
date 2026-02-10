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

    private double latitude;
    private double longitude;
    private String region;

    public DeliveryLocation(double latitude, double longitude, String region) {
        if (region == null || region.isBlank())
            throw new CustomException(ErrorCode.INVALID_REGION_FORMAT);

        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
    }
}
