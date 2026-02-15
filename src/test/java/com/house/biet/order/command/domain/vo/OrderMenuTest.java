package com.house.biet.order.command.domain.vo;

import com.house.biet.common.domain.vo.Money;
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
        assertThat(orderMenu.getMenuPrice()).isEqualTo(givenMoney);
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
    @DisplayName("성공 - 주문 메뉴 총 가격 계산 (수량 반영)")
    void totalPrice_reflectsQuantity() {
        // given
        int quantity = 3;
        OrderMenu orderMenu = new OrderMenu(
                givenStoreId,
                givenMenuId,
                givenMenuName,
                quantity,
                givenMoney
        );

        // when
        Money totalPrice = orderMenu.totalPrice();

        // then
        assertThat(totalPrice.value()).isEqualTo(givenMenuPrice * quantity);
    }

    @Test
    @DisplayName("성공 - OrderMenu equals/hashCode는 quantity 무시하고 비교")
    void equals_ignoreQuantity() {
        // given
        OrderMenu menu1 = new OrderMenu(givenStoreId, givenMenuId, givenMenuName, 2, givenMoney);
        OrderMenu menu2 = new OrderMenu(givenStoreId, givenMenuId, givenMenuName, 5, givenMoney);

        // then
        // quantity가 달라도 동일 메뉴로 판단
        assertThat(menu1).isEqualTo(menu2);
        assertThat(menu1.hashCode()).isEqualTo(menu2.hashCode());
    }

    @Test
    @DisplayName("성공 - 새로운 수량 합산 후 totalPrice 계산")
    void mergeQuantity_totalPrice() {
        // given
        int originalQuantity = 2;
        int addedQuantity = 3;

        OrderMenu original = new OrderMenu(givenStoreId, givenMenuId, givenMenuName, originalQuantity, givenMoney);
        OrderMenu added = new OrderMenu(givenStoreId, givenMenuId, givenMenuName, addedQuantity, givenMoney);

        // VO 불변성을 지키기 위해 새 객체 생성
        OrderMenu merged = new OrderMenu(
                original.getStoreId(),
                original.getMenuId(),
                original.getMenuName(),
                original.getQuantity() + added.getQuantity(),
                original.getMenuPrice()
        );

        // when
        Money totalPrice = merged.totalPrice();

        // then
        assertThat(merged.getQuantity()).isEqualTo(originalQuantity + addedQuantity);
        assertThat(totalPrice.value()).isEqualTo(givenMenuPrice * (originalQuantity + addedQuantity));
    }
}
