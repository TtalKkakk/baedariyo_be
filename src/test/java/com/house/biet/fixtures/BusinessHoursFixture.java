package com.house.biet.fixtures;

import com.house.biet.store.command.domain.vo.BusinessHour;
import com.house.biet.store.command.domain.vo.BusinessHours;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;

public class BusinessHoursFixture {

    public static BusinessHours withDefaultWeekdays() {
        Map<DayOfWeek, BusinessHour> hours = new EnumMap<>(DayOfWeek.class);

        hours.put(
                DayOfWeek.MONDAY,
                new BusinessHour(LocalTime.of(9, 0), LocalTime.of(18, 0))
        );
        hours.put(
                DayOfWeek.TUESDAY,
                new BusinessHour(LocalTime.of(9, 0), LocalTime.of(18, 0))
        );
        hours.put(
                DayOfWeek.WEDNESDAY,
                new BusinessHour(LocalTime.of(9, 0), LocalTime.of(18, 0))
        );

        return new BusinessHours(hours);
    }

    public static BusinessHours onlyWeekday(DayOfWeek day) {
        Map<DayOfWeek, BusinessHour> hours = new EnumMap<>(DayOfWeek.class);

        hours.put(
                day,
                new BusinessHour(LocalTime.of(10, 0), LocalTime.of(17, 0))
        );

        return new BusinessHours(hours);
    }
}
