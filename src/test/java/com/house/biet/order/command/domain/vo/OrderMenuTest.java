package com.house.biet.order.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderMenuTest {

    Long givenStoreId = 3L;
    Long givenMenuId = 5L;
    String givenMenuNameValue = "고추마요순살";
    int givenQuantity = 2;
    int givenMenuPrice = 1000;

    MenuName givenMenuName;
    Money givenMoney;

    @BeforeEach
    void setup() {
        givenMenuName = new MenuName(givenMenuNameValue);
        givenMoney = new Money(givenMenuPrice);
    }


    @Test
    @DisplayName("성공 - 주문 메뉴 생성")
    void createOrderMenu_Success() {
        // when
        OrderMenu orderMenu = new OrderMenu(
                givenStoreId,
                givenMenuId,
                givenMenuName,
                givenQuantity,
                givenMoney
        );

        // then
        assertThat(orderMenu).isNotNull();
        assertThat(orderMenu.getStoreId()).isEqualTo(givenStoreId);
        assertThat(orderMenu.getMenuId()).isEqualTo(givenMenuId);
        assertThat(orderMenu.getMenuName()).isEqualTo(givenMenuName);
        assertThat(orderMenu.getQuantity()).isEqualTo(givenQuantity);
    }

    @Test
    @DisplayName("실패 - 주문 수량이 0 이하이면 예외 발생")
    void createOrderMenu_Fail_InvalidQuantity() {
        // given
        int invalidQuantity = 0;

        // when & then
        assertThatThrownBy(() -> new OrderMenu(
                givenStoreId,
                givenMenuId,
                givenMenuName,
                invalidQuantity,
                givenMoney
        ))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MENU_QUANTITY.getMessage());
    }

    @Test
    @DisplayName("성공 - 주문 메뉴 총 가격 계산")
    void calculateTotalPrice_Success() {
        // given
        OrderMenu orderMenu = new OrderMenu(
                givenStoreId,
                givenMenuId,
                givenMenuName,
                givenQuantity,
                givenMoney
        );

        // when
        Money totalPrice = orderMenu.totalPrice();

        // then
        assertThat(totalPrice).isEqualTo(new Money(givenMenuPrice * givenQuantity));
    }

    @Test
    @DisplayName("성공 - 값이 같으면 동일한 OrderMenu로 판단")
    void equalsAndHashCode_Success() {
        // given
        OrderMenu orderMenu1 = new OrderMenu(
                givenStoreId,
                givenMenuId,
                new MenuName(givenMenuNameValue),
                givenQuantity,
                new Money(givenMenuPrice)
        );

        OrderMenu orderMenu2 = new OrderMenu(
                givenStoreId,
                givenMenuId,
                new MenuName(givenMenuNameValue),
                givenQuantity,
                new Money(givenMenuPrice)
        );

        // then
        assertThat(orderMenu1).isEqualTo(orderMenu2);
        assertThat(orderMenu1.hashCode()).isEqualTo(orderMenu2.hashCode());
    }
}