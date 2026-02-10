package com.house.biet.order.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeliveryLocationTest {

    double latitude = 127.12;
    double longitude = 36.32;
    String region;

    @Test
    @DisplayName("성공 - DeliveryLocation 생성")
    void createDeliveryLocation_Success() {
        // given
        region = "서울시 마포구";

        // when
        DeliveryLocation deliveryLocation = new DeliveryLocation(latitude, longitude, region);

        // then
        assertThat(deliveryLocation).isNotNull();
        assertThat(deliveryLocation.getLatitude()).isEqualTo(latitude);
        assertThat(deliveryLocation.getLongitude()).isEqualTo(longitude);
        assertThat(deliveryLocation.getRegion()).isEqualTo(region);
    }

    @Test
    @DisplayName("에러 - region 이 null")
    void createDeliveryLocation_Error_NullRegion() {
        // given
        region = null;

        // when & then
        assertThatThrownBy(() -> new DeliveryLocation(latitude, longitude, region))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_REGION_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - region 이 빈 문자열")
    void createDeliveryLocation_Error_EmptyRegion() {
        // given
        region = "";

        // when & then
        assertThatThrownBy(() -> new DeliveryLocation(latitude, longitude, region))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_REGION_FORMAT.getMessage());
    }
}