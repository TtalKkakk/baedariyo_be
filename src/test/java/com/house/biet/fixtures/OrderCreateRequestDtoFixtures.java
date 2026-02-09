package com.house.biet.fixtures;

import com.house.biet.order.command.domain.dto.OrderCreateRequestDto;
import com.house.biet.order.command.domain.dto.OrderMenuRequestDto;
import com.house.biet.order.command.domain.vo.PaymentMethod;

import java.util.List;

public class OrderCreateRequestDtoFixtures {

    public static OrderCreateRequestDto sample(Long storeId) {
        return new OrderCreateRequestDto(
                storeId,
                List.of(OrderMenuRequestDtoFixtures.defaultMenu()),
                "가게 요청사항입니다",
                "라이더 요청사항입니다",
                "서울시 강남구 테스트로 123",
                PaymentMethod.CARD
        );
    }

    public static OrderCreateRequestDto withMenus(Long storeId, List<OrderMenuRequestDto> menus) {
        return new OrderCreateRequestDto(
                storeId,
                menus,
                "가게 요청",
                "라이더 요청",
                "서울시 강남구",
                PaymentMethod.CARD
        );
    }

    public static OrderCreateRequestDto cashOrder(Long storeId) {
        return new OrderCreateRequestDto(
                storeId,
                List.of(OrderMenuRequestDtoFixtures.defaultMenu()),
                null,
                null,
                "서울시 마포구",
                PaymentMethod.CASH
        );
    }
}
