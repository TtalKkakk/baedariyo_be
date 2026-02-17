package com.house.biet.fixtures;

import com.house.biet.order.command.domain.dto.DeliveryAddressDto;
import com.house.biet.order.command.domain.dto.OrderCreateRequestDto;
import com.house.biet.order.command.domain.dto.OrderMenuRequestDto;
import com.house.biet.common.domain.enums.PaymentMethod;

import java.util.List;

public class OrderCreateRequestDtoFixtures {

    private static DeliveryAddressDto deliveryAddressDto1 = new DeliveryAddressDto(
            "서울특별시 강남구 테헤란로 123",
            "서울특별시 강남구 역삼동 123-45",
            "B동 202호"
    );

    private static DeliveryAddressDto deliveryAddressDto2 = new DeliveryAddressDto(
            "서울특별시 마포구 월드컵북로 396",
            "서울특별시 마포구 상암동 1605",
            "101동 1001호"
    );

    public static OrderCreateRequestDto sample(Long storeId) {
        return new OrderCreateRequestDto(
                storeId,
                List.of(OrderMenuRequestDtoFixtures.defaultMenu()),
                "가게 요청사항입니다",
                "라이더 요청사항입니다",
                deliveryAddressDto1,
                PaymentMethod.CARD
        );
    }

    public static OrderCreateRequestDto withMenus(Long storeId, List<OrderMenuRequestDto> menus) {
        return new OrderCreateRequestDto(
                storeId,
                menus,
                "가게 요청",
                "라이더 요청",
                deliveryAddressDto1,
                PaymentMethod.CARD
        );
    }

    public static OrderCreateRequestDto cashOrder(Long storeId) {
        return new OrderCreateRequestDto(
                storeId,
                List.of(OrderMenuRequestDtoFixtures.defaultMenu()),
                null,
                null,
                deliveryAddressDto2,
                PaymentMethod.CASH
        );
    }
}
