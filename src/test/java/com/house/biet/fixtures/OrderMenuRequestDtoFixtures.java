package com.house.biet.fixtures;

import com.house.biet.order.command.domain.dto.OrderMenuRequestDto;

public class OrderMenuRequestDtoFixtures {

    public static OrderMenuRequestDto defaultMenu() {
        return new OrderMenuRequestDto(
                1L,
                "테스트 메뉴",
                10000,
                1
        );
    }

    public static OrderMenuRequestDto menu(
            Long menuId,
            String menuName,
            int menuPrice,
            int quantity
    ) {
        return new OrderMenuRequestDto(
                menuId,
                menuName,
                menuPrice,
                quantity
        );
    }
}
