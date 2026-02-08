package com.house.biet.fixtures;

import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.vo.MenuName;
import com.house.biet.order.command.domain.vo.Money;
import com.house.biet.order.command.domain.vo.OrderMenu;
import com.house.biet.order.command.domain.vo.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixtures {

    private OrderFixtures() {}

    /**
     * 테스트용 Order 생성
     *
     * @param userId  주문자 ID
     */
    public static Order order(Long userId) {
        OrderMenu menu = new OrderMenu(
                1L,          // storeId
                100L,        // menuId
                new MenuName("치즈버거"),
                2,           // quantity
                new Money(6300)
        );

        return new Order(
                1L,               // storeId
                userId,
                List.of(menu),
                "가게 요청",
                "라이더 요청",
                "서울시 강남구",
                PaymentMethod.CARD,
                LocalDateTime.now()
        );
    }
}
