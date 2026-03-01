package com.house.biet.delivery.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrentRiderLocationTest {

    @Test
    @DisplayName("성공 - 현재 라이더 위치 생성")
    void CreateCurrentRiderLocation_Success() {
        // given
        double latitude = 37.5665;
        double longitude = 126.9780;

        // when
        CurrentRiderLocation location =
                new CurrentRiderLocation(latitude, longitude);

        // then
        assertThat(location.getLatitude()).isEqualTo(latitude);
        assertThat(location.getLongitude()).isEqualTo(longitude);
    }

    @Test
    @DisplayName("실패 - 위도 범위 초과")
    void CreateCurrentRiderLocation_Error_InvalidLatitude() {
        assertThatThrownBy(() -> new CurrentRiderLocation(100.0, 127.0))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_LATITUDE_RANGE.getMessage());
    }

    @Test
    @DisplayName("실패 - 경도 범위 초과")
    void CreateCurrentRiderLocation_Error_InvalidLongitude() {
        assertThatThrownBy(() -> new CurrentRiderLocation(37.0, 200.0))
                        .isInstanceOf(CustomException.class)
                        .hasMessage(ErrorCode.INVALID_LATITUDE_RANGE.getMessage());
    }
}