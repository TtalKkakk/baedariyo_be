package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;


@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessHours {

    @ElementCollection
    @CollectionTable(
            name = "store_business_hours",
            joinColumns = @JoinColumn(name = "store_id")
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "day_of_week")
    private Map<DayOfWeek, BusinessHour> hours = new EnumMap<>(DayOfWeek.class);

    public BusinessHours(Map<DayOfWeek, BusinessHour> hours) {
        if (hours == null || hours.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_BUSINESS_HOURS);
        }
        this.hours.putAll(hours);
    }

    public boolean isOpen(DayOfWeek day, LocalTime time) {
        BusinessHour hour = hours.get(day);
        return hour != null && hour.isOpen(time);
    }
}
