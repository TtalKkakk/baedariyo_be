package com.house.biet.store.command.domain.vo;

import com.house.biet.fixtures.BusinessHourFixture;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BusinessHourTest {

    @Test
    @DisplayName("성공 - 정상적인 운영시간 생성")
    void createBusinessHour_Success() {
        // given
        LocalTime open = LocalTime.of(10, 0 );
        LocalTime close = LocalTime.of(22, 30);

        // when
        BusinessHour businessHour = new BusinessHour(open, close);

        // then
        assertThat(businessHour.getOpenTime()).isEqualTo(open);
        assertThat(businessHour.getCloseTime()).isEqualTo(close);
    }

    @Test
    @DisplayName("실패 - 오픈 시간이 마감 시간보다 늦으면 예외 발생")
    void createBusinessHour_Fail_OpenAfterClose() {
        // given
        LocalTime open = LocalTime.of(23, 0);
        LocalTime close = LocalTime.of(22, 30);

        // when & then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> new BusinessHour(open, close)
        );

        assertThat(exception.getErrorCode())
                .isEqualTo(ErrorCode.INVALID_BUSINESS_HOUR_TIME_RANGE);
    }

    @Test
    @DisplayName("성공 - 현재 시간이 운영시간 내에 있으면 true")
    void isOpen_True_WhenWithinBusinessHour() {
        // given
        BusinessHour businessHour = BusinessHourFixture.aBusinessHour().build();
        LocalTime now = LocalTime.of(14, 0);

        // when
        boolean result = businessHour.isOpen(now);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("성공 - 현재 시간이 오픈 시간 이전이면 false")
    void isOpen_False_WhenBeforeOpenTime() {
        // given
        BusinessHour businessHour = BusinessHourFixture.aBusinessHour().build();
        LocalTime time = LocalTime.of(3, 0);

        // when
        boolean result = businessHour.isOpen(time);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("성공 - 현재 시간이 마감 시간 이후면 false")
    void isOpen_False_WhenAfterCloseTime() {
        // given
        BusinessHour businessHour = BusinessHourFixture.aBusinessHour().build();
        LocalTime time = LocalTime.of(23, 0);

        // when
        boolean result = businessHour.isOpen(time);

        // then
        assertThat(result).isFalse();
    }
}
