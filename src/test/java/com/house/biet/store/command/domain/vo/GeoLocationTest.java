package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeoLocationTest {

    @Test
    @DisplayName("성공 - 가게 주소 위도 경도 vo 생성")
    void CreateGeoLocation_Success() {
        // given
        double latitude = 37.5665;
        double longitude = 126.9780;

        // when
        GeoLocation location =
                new GeoLocation(latitude, longitude);

        // then
        assertThat(location.getLatitude()).isEqualTo(latitude);
        assertThat(location.getLongitude()).isEqualTo(longitude);
    }

    @Test
    @DisplayName("실패 - 위도 범위 초과")
    void CreateGeoLocation_Error_InvalidLatitude() {
        assertThatThrownBy(() -> new GeoLocation(100.0, 127.0))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_LATITUDE_RANGE.getMessage());
    }

    @Test
    @DisplayName("실패 - 경도 범위 초과")
    void CreateGeoLocation_Error_InvalidLongitude() {
        assertThatThrownBy(() -> new GeoLocation(37.0, 200.0))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_LONGITUDE_RANGE.getMessage());
    }

    @Test
    @DisplayName("성공 - 위도 경도 경계값 허용")
    void createGeoLocation_Success_BoundaryValue() {
        // given
        double latitude = -90.0;
        double longitude = 180.0;

        // when
        GeoLocation location = new GeoLocation(latitude, longitude);

        // then
        assertThat(location.getLatitude()).isEqualTo(latitude);
        assertThat(location.getLongitude()).isEqualTo(longitude);
    }
}