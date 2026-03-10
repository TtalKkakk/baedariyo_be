package com.house.biet.fixtures;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.common.domain.vo.Address;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.vo.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;

public class StoreFixture {

    public static Store createStore() {
        Address address = new Address("roadAddressFixture", "jibunAddressFixture", "detailAddressFixture");

        return Store.create(
                new StoreName("테스트 가게"),
                StoreCategory.CHICKEN,
                address,
                new GeoLocation(37.2123, 129.222),
                StoreThumbnail.of("https://test-thumbnail.png"),
                createBusinessHours(),
                new StoreOperationInfo("테스트 가게 설명", "테스트 원산지 설명"),
                new Money(15000),
                new Money(3000)
        );
    }

    public static Store createStoreWithoutOptionalInfo() {
        Address address = new Address("roadAddressFixture", "jibunAddressFixture", "detailAddressFixture");

        return Store.create(
                new StoreName("기본 가게"),
                StoreCategory.CAFE_DESSERT,
                address,
                new GeoLocation(37.2123, 129.222),
                null,                   // thumbnail nullable
                null,                   // businessHours nullable
                null,                   // operationInfo nullable
                new Money(10000),
                new Money(2000)
        );
    }

    private static BusinessHours createBusinessHours() {
        Map<DayOfWeek, BusinessHour> hours = new EnumMap<>(DayOfWeek.class);

        hours.put(
                DayOfWeek.MONDAY,
                new BusinessHour(
                        LocalTime.of(10, 0),
                        LocalTime.of(22, 0)
                )
        );

        hours.put(
                DayOfWeek.TUESDAY,
                new BusinessHour(
                        LocalTime.of(10, 0),
                        LocalTime.of(22, 0)
                )
        );

        return new BusinessHours(hours);
    }
}
