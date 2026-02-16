package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class BusinessHour {

    private LocalTime openTime;
    private LocalTime closeTime;

    public BusinessHour(LocalTime openTime, LocalTime closeTime) {
        if (openTime.isAfter(closeTime))
            throw new CustomException(ErrorCode.INVALID_BUSINESS_HOUR_TIME_RANGE);

        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public boolean isOpen(LocalTime time) {
        return !time.isBefore(openTime) && !time.isAfter(closeTime);
    }
}
