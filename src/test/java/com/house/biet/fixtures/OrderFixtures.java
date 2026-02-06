package com.house.biet.fixtures;

import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.vo.MenuName;
import com.house.biet.order.command.domain.vo.Money;
import com.house.biet.order.command.domain.vo.OrderMenu;
import com.house.biet.order.command.domain.vo.PaymentMethod;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.user.command.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixtures {

    private OrderFixtures() {}

    public static Order order(User user, Rider rider) {
        OrderMenu menu = new OrderMenu(
                1L,
                100L,
                new MenuName("치즈버거"),
                2,
                new Money(6300)
        );

        return new Order(
                1L,
                user,
                rider,
                List.of(menu),
                "가게 요청",
                "라이더 요청",
                "서울시 강남구",
                PaymentMethod.CARD,
                true,
                LocalDateTime.now()
        );
    }
}
