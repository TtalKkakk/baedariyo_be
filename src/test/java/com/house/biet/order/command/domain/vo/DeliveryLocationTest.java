package com.house.biet.order.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeliveryLocationTest {

    double latitude = 37.5665;
    double longitude = 126.9780;
    String region = "서울시 마포구";

    @Test
    @DisplayName("성공 - DeliveryLocation 생성")
    void createDeliveryLocation_Success() {
        // when
        DeliveryLocation deliveryLocation =
                new DeliveryLocation(latitude, longitude, region);

        // then
        assertThat(deliveryLocation).isNotNull();
        assertThat(deliveryLocation.getLatitude()).isEqualTo(latitude);
        assertThat(deliveryLocation.getLongitude()).isEqualTo(longitude);
        assertThat(deliveryLocation.getRegion()).isEqualTo(region);
    }

    @Test
    @DisplayName("에러 - region 이 null")
    void createDeliveryLocation_Error_NullRegion() {
        // when & then
        assertThatThrownBy(() ->
                new DeliveryLocation(latitude, longitude, null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_REGION_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - region 이 빈 문자열")
    void createDeliveryLocation_Error_EmptyRegion() {
        // when & then
        assertThatThrownBy(() ->
                new DeliveryLocation(latitude, longitude, " "))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_REGION_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - 위도가 범위를 벗어남")
    void createDeliveryLocation_Error_InvalidLatitude() {
        // given
        double invalidLatitude = 100.0;

        // when & then
        assertThatThrownBy(() ->
                new DeliveryLocation(invalidLatitude, longitude, region))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_LATITUDE_RANGE.getMessage());
    }

    @Test
    @DisplayName("에러 - 경도가 범위를 벗어남")
    void createDeliveryLocation_Error_InvalidLongitude() {
        // given
        double invalidLongitude = 200.0;

        // when & then
        assertThatThrownBy(() ->
                new DeliveryLocation(latitude, invalidLongitude, region))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_LONGITUDE_RANGE.getMessage());
    }

    @Test
    @DisplayName("성공 - 두 DeliveryLocation 간 거리 계산")
    void distanceTo_Success() {
        // given
        DeliveryLocation loc1 =
                new DeliveryLocation(37.5665, 126.9780, "서울시청");

        DeliveryLocation loc2 =
                new DeliveryLocation(37.5796, 126.9770, "경복궁");

        // when
        double distance = loc1.distanceTo(loc2);

        // then
        assertThat(distance).isGreaterThan(100);
        assertThat(distance).isLessThan(2000);
    }

    @Test
    @DisplayName("에러 - 거리 계산 시 target 이 null")
    void distanceTo_Error_NullTarget() {
        // given
        DeliveryLocation loc =
                new DeliveryLocation(latitude, longitude, region);

        // when & then
        assertThatThrownBy(() -> loc.distanceTo(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_DELIVERY_LOCATION.getMessage());
    }
}
