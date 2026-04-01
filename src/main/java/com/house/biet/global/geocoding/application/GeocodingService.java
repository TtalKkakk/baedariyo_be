package com.house.biet.global.geocoding.application;

import com.house.biet.global.geocoding.dto.GeoPoint;
import com.house.biet.global.geocoding.infrastructure.VWorldGeocodingClient;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeocodingService {

    private final VWorldGeocodingClient geocodingClient;

    /**
     * 주소를 좌표로 변환한다
     *
     * @param address address 값
     * @return geocode 결과
     */
    public GeoPoint geocode(String address) {

        String normalizedAddress = normalize(address);

        return geocodingClient.geocode(normalizedAddress);
    }

    private String normalize(String address) {

        if (address == null || address.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_ADDRESS_GEOCODING_FORMAT);
        }

        return address.trim().replaceAll("\\s+", " ");
    }
}