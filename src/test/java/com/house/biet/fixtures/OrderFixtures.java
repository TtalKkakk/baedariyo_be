package com.house.biet.fixtures;

import com.house.biet.common.domain.enums.PaymentMethod;
import com.house.biet.common.domain.vo.Address;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.vo.DeliveryLocation;
import com.house.biet.order.command.domain.vo.OrderMenu;
import com.house.biet.order.command.domain.vo.OrderMenuName;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixtures {

    private OrderFixtures() {
    }

    public static Order order(Long userId) {
        return order(1L, userId);
    }

    public static Order order(Long storeId, Long userId) {
        OrderMenu menu = new OrderMenu(
                storeId,
                100L,
                new OrderMenuName("test-menu"),
                2,
                new Money(6300)
        );

        return new Order(
                storeId,
                userId,
                List.of(menu),
                "store-request",
                "rider-request",
                new Address(
                        "Seoul road address",
                        "Seoul jibun address",
                        "101-1001"
                ),
                new DeliveryLocation(
                        36.32,
                        127.12,
                        "Gangnam"
                ),
                PaymentMethod.CARD,
                LocalDateTime.now()
        );
    }
}
