package com.house.biet.order.command.domain.aggregate;

import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.order.command.domain.vo.MenuName;
import com.house.biet.order.command.domain.vo.Money;
import com.house.biet.order.command.domain.vo.OrderMenu;
import com.house.biet.order.command.domain.vo.OrderStatus;
import com.house.biet.order.command.domain.vo.PaymentMethod;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.rider.command.domain.vo.VehicleType;
import com.house.biet.user.command.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

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

    // ---------- 유저/라이더 ----------
    User user;
    Rider rider;

    // ---------- 주문 요청 관련 ----------
    String storeRequest;
    String riderRequest;
    String deliveryAddress;
    PaymentMethod paymentMethod;
    boolean paid;
    LocalDateTime estimatedTime;

    @BeforeEach
    void setup() {
        // OrderMenu 객체 생성
        menu1 = new OrderMenu(storeId, menuId1, new MenuName(menuName1), quantity1, new Money(price1));
        menu2 = new OrderMenu(storeId, menuId2, new MenuName(menuName2), quantity2, new Money(price2));

        // 테스트용 User / Rider 객체 생성 (단순 ID 세팅)
        // User 계정 생성
        Account userAccount = Account.signup(
                new Email("user@example.com"),
                Password.encrypt(UUID.randomUUID().toString().substring(1, 30), ENCODER),
                UserRole.USER
        );

        // Rider 계정 생성
        Account riderAccount = Account.signup(
                new Email("rider@example.com"),
                Password.encrypt(UUID.randomUUID().toString().substring(1, 30), ENCODER),
                UserRole.RIDER
        );

        // User 객체 생성
        user = User.create(
                userAccount,
                "홍길동",
                "Gildong",
                "010-1111-1111"
        );

        // Rider 객체 생성
        rider = Rider.create(
                riderAccount,
                "김라이더",
                "RiderKim",
                "010-2222-2222",
                VehicleType.MOTORCYCLE
        );

        // 주문 요청 기본값
        storeRequest = "가게 요청";
        riderRequest = "라이더 요청";
        deliveryAddress = "서울시 강남구";
        paymentMethod = PaymentMethod.CARD;
        paid = false;
        estimatedTime = LocalDateTime.now().plusMinutes(30);
    }

    @Test
    @DisplayName("성공 - 주문 생성 및 총액 계산")
    void createOrder_Success() {
        Order order = new Order(
                storeId,
                user,
                rider,
                List.of(menu1, menu2),
                storeRequest,
                riderRequest,
                deliveryAddress,
                paymentMethod,
                paid,
                estimatedTime
        );

        assertThat(order.getStoreId()).isEqualTo(storeId);
        assertThat(order.getMenus()).containsExactlyInAnyOrder(menu1, menu2);
        assertThat(order.getTotalPrice().value()).isEqualTo(price1 * quantity1 + price2 * quantity2);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDERED);
        assertThat(order.getUser().getId()).isEqualTo(user.getId());
        assertThat(order.getRider().getId()).isEqualTo(rider.getId());
        assertThat(order.getStoreRequest()).isEqualTo(storeRequest);
        assertThat(order.getRiderRequest()).isEqualTo(riderRequest);
        assertThat(order.getDeliveryAddress()).isEqualTo(deliveryAddress);
        assertThat(order.getPaymentMethod()).isEqualTo(paymentMethod);
        assertThat(order.isPaid()).isEqualTo(paid);
        assertThat(order.getEstimatedTime()).isEqualTo(estimatedTime);
    }

    @Test
    @DisplayName("실패 - 메뉴 없는 주문 생성")
    void createOrder_Fail_NoMenu() {
        assertThatThrownBy(() -> new Order(
                storeId,
                user,
                rider,
                List.of(),
                storeRequest,
                riderRequest,
                deliveryAddress,
                paymentMethod,
                paid,
                estimatedTime
        ))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EMPTY_ORDER_MENU.getMessage());
    }

    @Test
    @DisplayName("성공 - 새로운 메뉴 추가 및 총액 갱신")
    void addMenu_Success_NewMenu() {
        Order order = new Order(storeId, user, rider, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, paid, estimatedTime);

        order.addMenu(menu2);

        assertThat(order.getMenus()).containsExactlyInAnyOrder(menu1, menu2);
        assertThat(order.getTotalPrice().value()).isEqualTo(price1 * quantity1 + price2 * quantity2);
    }

    @Test
    @DisplayName("성공 - 기존 메뉴 추가 시 수량 합산")
    void addMenu_Success_ExistingMenu() {
        Order order = new Order(storeId, user, rider, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, paid, estimatedTime);

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
        Order order = new Order(storeId, user, rider, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, paid, estimatedTime);

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
        Order order = new Order(storeId, user, rider, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, paid, estimatedTime);

        order.cancel();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("실패 - 주문 취소 잘못된 상태")
    void cancelOrder_Fail() {
        Order order = new Order(storeId, user, rider, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, paid, estimatedTime);

        order.markPaid();
        order.markDelivering();

        assertThatThrownBy(order::cancel)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ORDER_CANCEL.getMessage());
    }

    @Test
    @DisplayName("성공 - 결제, 배달, 완료 상태 전이")
    void orderStatusFlow_Success() {
        Order order = new Order(storeId, user, rider, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, paid, estimatedTime);

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
        Order order = new Order(storeId, user, rider, List.of(menu1),
                storeRequest, riderRequest, deliveryAddress, paymentMethod, paid, estimatedTime);

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
