package com.house.biet.fixtures;

import com.house.biet.store.command.domain.vo.BusinessHour;

import java.time.LocalTime;

public class BusinessHourFixture {

    private LocalTime openTime = LocalTime.of(10, 0);
    private LocalTime closeTime = LocalTime.of(22, 30);

    private BusinessHourFixture() {}

    public static BusinessHourFixture aBusinessHour() {
        return new BusinessHourFixture();
    }

    public BusinessHourFixture withOpenTime(LocalTime openTime) {
        this.openTime = openTime;
        return this;
    }

    public BusinessHourFixture withCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
        return this;
    }

    public BusinessHour build() {
        return new BusinessHour(openTime, closeTime);
    }
}