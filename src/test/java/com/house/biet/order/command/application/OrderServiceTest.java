package com.house.biet.order.command.application;

import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.application.OrderService;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.vo.OrderMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    private Order order;
    private OrderMenu menu;

    @BeforeEach
    void setup() {
        order = OrderFixtures.order(1L);
        menu = order.getMenus().get(0);
    }

    /* -------------------- 주문 생성 -------------------- */

    @Test
    @DisplayName("성공 - 주문 생성")
    void createOrder_Success() {
        // given
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // when
        Order saved = orderService.create(
                order.getStoreId(),
                order.getUserId(),
                order.getMenus(),
                order.getStoreRequest(),
                order.getRiderRequest(),
                order.getDeliveryAddress(),
                order.getPaymentMethod(),
                order.getEstimatedTime()
        );

        // then
        assertThat(saved.getUserId()).isEqualTo(order.getUserId());
        assertThat(saved.getMenus()).containsExactlyInAnyOrder(menu);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("성공 - 주문 조회 성공")
    void getOrder_Success() {
        // given
        Long orderId = order.getId();
        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // when
        Order foundOrder = orderService.getOrder(orderId);

        // then
        assertThat(foundOrder).isSameAs(order);
        assertThat(foundOrder.getId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("에러 - 주문이 존재하지 않으면 예외 발생")
    void getOrder_Fail_OrderNotFound() {
        // given
        Long orderId = 999L;
        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.getOrder(orderId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND.getMessage());
    }

    /* -------------------- 메뉴 관리 -------------------- */

    @Test
    @DisplayName("성공 - 메뉴 추가")
    void addMenu_Success() {
        // given
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        OrderMenu newMenu = new OrderMenu(1L, 2L, menu.getMenuName(), 1, menu.getMenuPrice());

        // when
        orderService.addMenu(order.getId(), newMenu);

        // then
        assertThat(order.getMenus()).containsExactlyInAnyOrder(menu, newMenu);
    }

    @Test
    @DisplayName("성공 - 주문 메뉴 제거")
    void removeMenu_success() {
        // given
        Order order = OrderFixtures.order(1L);
        OrderMenu menu = order.getMenus().get(0);

        given(orderRepository.findById(order.getId()))
                .willReturn(Optional.of(order));

        // when
        orderService.removeMenu(order.getId(), menu);

        // then
        assertThat(order.getMenus()).isEmpty();
    }

    @Test
    @DisplayName("실패 - 주문 메뉴 제거 시 주문 없음")
    void removeMenu_fail_orderNotFound() {
        // given
        OrderMenu menu = mock(OrderMenu.class);

        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> orderService.removeMenu(1L, menu))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("성공 - 메뉴 수량 업데이트")
    void updateMenuQuantity_Success() {
        // given
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        int newQuantity = 5;

        // when
        orderService.updateMenuQuantity(order.getId(), menu.getMenuId(), newQuantity);

        // then
        assertThat(order.getMenus().get(0).getQuantity()).isEqualTo(newQuantity);
    }

    /* -------------------- 상태 전이 -------------------- */

    @Test
    @DisplayName("성공 - 주문 상태 전이: 결제 -> 배달 -> 완료")
    void orderStatusFlow_Success() {
        // given
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // when
        orderService.markPaid(order.getId());
        orderService.markDelivering(order.getId());
        orderService.markDelivered(order.getId());

        // then
        assertThat(order.getStatus().name()).isEqualTo("DELIVERED");
    }

    @Test
    @DisplayName("실패 - 잘못된 상태 전이")
    void orderStatusFlow_Fail() {
        // given
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.markDelivering(order.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ORDER_STATUS.getMessage());

        assertThatThrownBy(() -> orderService.markDelivered(order.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ORDER_STATUS.getMessage());
    }

    @Test
    @DisplayName("성공 - 주문 취소")
    void cancelOrder_success() {
        // given
        Order order = OrderFixtures.order(1L);

        given(orderRepository.findById(order.getId()))
                .willReturn(Optional.of(order));

        // when
        orderService.cancelOrder(order.getId());

        // then
        assertThat(order.isCancelled()).isTrue();
    }

    @Test
    @DisplayName("실패 - 배달 중 주문 취소")
    void cancelOrder_fail_invalidStatus() {
        // given
        Order order = OrderFixtures.order(1L);
        order.markPaid();
        order.markDelivering();

        given(orderRepository.findById(order.getId()))
                .willReturn(Optional.of(order));

        // when / then
        assertThatThrownBy(() -> orderService.cancelOrder(order.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ORDER_CANCEL.getMessage());
    }

}
