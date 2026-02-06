package com.house.biet.order.command.domain.aggregate;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.domain.vo.MenuName;
import com.house.biet.order.command.domain.vo.Money;
import com.house.biet.order.command.domain.vo.OrderMenu;
import com.house.biet.order.command.domain.vo.OrderStatus;
import com.house.biet.order.command.domain.vo.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    // ---------- 주문/가게/메뉴 관련 ----------
    Long storeId = 1L;

    Long menuId1 = 101L;
    String menuName1 = "치즈버거";
    int quantity1 = 2;
    int price1 = 6300;

    Long menuId2 = 102L;
    String menuName2 = "콜라";
    int quantity2 = 1;
    int price2 = 1200;

    OrderMenu menu1;
    OrderMenu menu2;

    // ---------- 주문 요청 관련 ----------
    Long userId = 1L;
    Long riderId = 2L;

    String storeRequest;
    String riderRequest;
    String deliveryAddress;
    PaymentMethod paymentMethod;
    LocalDateTime estimatedTime;

    @BeforeEach
    void setup() {
        menu1 = new OrderMenu(storeId, menuId1, new MenuName(menuName1), quantity1, new Money(price1));
        menu2 = new OrderMenu(storeId, menuId2, new MenuName(menuName2), quantity2, new Money(price2));

        storeRequest = "가게 요청";
        riderRequest = "라이더 요청";
        deliveryAddress = "서울시 강남구";
        paymentMethod = PaymentMethod.CARD;
        estimatedTime = LocalDateTime.now().plusMinutes(30);
    }

    @Test
    @DisplayName("성공 - 주문 생성 및 총액 계산")
    void createOrder_Success() {
        Order order = new Order(
                storeId,
                userId,
                riderId,
                List.of(menu1, menu2),
                storeRequest,
                riderRequest,
                deliveryAddress,
                paymentMethod,
                estimatedTime
        );

        assertThat(order.getStoreId()).isEqualTo(storeId);
        assertThat(order.getMenus()).containsExactlyInAnyOrder(menu1, menu2);
        assertThat(order.getTotalPrice().value()).isEqualTo(price1 * quantity1 + price2 * quantity2);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDERED);
        assertThat(order.getUserId()).isEqualTo(userId);
        assertThat(order.getRiderId()).isEqualTo(riderId);
        assertThat(order.getStoreRequest()).isEqualTo(storeRequest);
        assertThat(order.getRiderRequest()).isEqualTo(riderRequest);
        assertThat(order.getDeliveryAddress()).isEqualTo(deliveryAddress);
        assertThat(order.getPaymentMethod()).isEqualTo(paymentMethod);
        assertThat(order.getEstimatedTime()).isEqualTo(estimatedTime);
    }

    @Test
    @DisplayName("실패 - 메뉴 없는 주문 생성")
    void createOrder_Fail_NoMenu() {
        assertThatThrownBy(() -> new Order(
                storeId,
                userId,
                riderId,
                List.of(),
                storeRequest,
                riderRequest,
                deliveryAddress,
                paymentMethod,
                estimatedTime
        ))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EMPTY_ORDER_MENU.getMessage());
    }

    @Test
    @DisplayName("성공 - 새로운 메뉴 추가 및 총액 갱신")
    void addMenu_Success_NewMenu() {
        Order order = new Order(storeId, userId, riderId, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, estimatedTime);

        order.addMenu(menu2);

        assertThat(order.getMenus()).containsExactlyInAnyOrder(menu1, menu2);
        assertThat(order.getTotalPrice().value()).isEqualTo(price1 * quantity1 + price2 * quantity2);
    }

    @Test
    @DisplayName("성공 - 기존 메뉴 추가 시 수량 합산")
    void addMenu_Success_ExistingMenu() {
        Order order = new Order(storeId, userId, riderId, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, estimatedTime);

        int addQuantity = 3;
        OrderMenu additionalMenu = new OrderMenu(storeId, menuId1, new MenuName(menuName1), addQuantity, new Money(price1));

        order.addMenu(additionalMenu);

        assertThat(order.getMenus()).hasSize(1);
        assertThat(order.getMenus().get(0).getQuantity()).isEqualTo(quantity1 + addQuantity);
        assertThat(order.getTotalPrice().value()).isEqualTo(price1 * (quantity1 + addQuantity));
    }

    @Test
    @DisplayName("실패 - 다른 가게 메뉴 추가 시 예외")
    void addMenu_Fail_InvalidStore() {
        Order order = new Order(storeId, userId, riderId, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, estimatedTime);

        long otherStoreId = 999L;
        OrderMenu otherStoreMenu = new OrderMenu(otherStoreId, 1L, new MenuName("타가게 메뉴"), 1, new Money(1000));

        assertThatThrownBy(() -> order.addMenu(otherStoreMenu))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_ID.getMessage());
    }

    /* --------------------------- 상태 전이 테스트 --------------------------- */

    @Test
    @DisplayName("성공 - 주문 취소")
    void cancelOrder_Success() {
        Order order = new Order(storeId, userId, riderId, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, estimatedTime);

        order.cancel();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("실패 - 주문 취소 잘못된 상태")
    void cancelOrder_Fail() {
        Order order = new Order(storeId, userId, riderId, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, estimatedTime);

        order.markPaid();
        order.markDelivering();

        assertThatThrownBy(order::cancel)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ORDER_CANCEL.getMessage());
    }

    @Test
    @DisplayName("성공 - 결제, 배달, 완료 상태 전이")
    void orderStatusFlow_Success() {
        Order order = new Order(storeId, userId, riderId, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, estimatedTime);

        order.markPaid();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);

        order.markDelivering();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERING);

        order.markDelivered();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    @DisplayName("실패 - 잘못된 상태 전이 시 예외")
    void orderStatusFlow_Fail_InvalidTransition() {
        Order order = new Order(storeId, userId, riderId, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, estimatedTime);

        assertThatThrownBy(order::markDelivering)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ORDER_STATUS.getMessage());

        assertThatThrownBy(order::markDelivered)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ORDER_STATUS.getMessage());

        order.markPaid();
        assertThatThrownBy(order::markDelivered)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ORDER_STATUS.getMessage());
    }
}
