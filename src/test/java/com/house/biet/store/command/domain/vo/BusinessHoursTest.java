package com.house.biet.store.command.domain.vo;

import com.house.biet.fixtures.BusinessHoursFixture;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class BusinessHoursTest {

    @Test
    @DisplayName("성공 - 요일별 영업시간으로 BusinessHours 생성")
    void createBusinessHours_success() {
        // given
        Map<DayOfWeek, BusinessHour> hours = new EnumMap<>(DayOfWeek.class);
        hours.put(
                DayOfWeek.MONDAY,
                new BusinessHour(LocalTime.of(9, 0), LocalTime.of(18, 0))
        );

        // when
        BusinessHours businessHours = new BusinessHours(hours);

        // then
        assertThat(businessHours.getHours()).hasSize(1);
        assertThat(businessHours.getHours())
                .containsKey(DayOfWeek.MONDAY);
    }

    @Test
    @DisplayName("성공 - 영업 중인 시간에는 true 반환")
    void isOpen_true_whenWithinBusinessHour() {
        // given
        BusinessHours businessHours = BusinessHoursFixture.withDefaultWeekdays();

        // when
        boolean result = businessHours.isOpen(
                DayOfWeek.MONDAY,
                LocalTime.of(10, 0)
        );

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("성공 - 영업 종료 시간 이후에는 false 반환")
    void isOpen_false_whenOutsideBusinessHour() {
        // given
        BusinessHours businessHours = BusinessHoursFixture.withDefaultWeekdays();

        // when
        boolean result = businessHours.isOpen(
                DayOfWeek.MONDAY,
                LocalTime.of(23, 0)
        );

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("성공 - 해당 요일 영업시간이 없으면 false 반환")
    void isOpen_false_whenDayNotExists() {
        // given
        BusinessHours businessHours = BusinessHoursFixture.onlyWeekday(DayOfWeek.MONDAY);

        // when
        boolean result = businessHours.isOpen(
                DayOfWeek.SUNDAY,
                LocalTime.of(10, 0)
        );

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("에러 - hours가 null이면 예외 발생")
    void createBusinessHours_fail_whenHoursNull() {
        // given
        Map<DayOfWeek, BusinessHour> hours = null;

        // when & then
        assertThatThrownBy(() -> new BusinessHours(hours))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EMPTY_BUSINESS_HOURS.getMessage());
    }

    @Test
    @DisplayName("에러 - hours가 비어있으면 예외 발생")
    void createBusinessHours_fail_whenHoursEmpty() {
        // given
        Map<DayOfWeek, BusinessHour> hours = new EnumMap<>(DayOfWeek.class);

        // when & then
        assertThatThrownBy(() -> new BusinessHours(hours))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EMPTY_BUSINESS_HOURS.getMessage());
    }
}
