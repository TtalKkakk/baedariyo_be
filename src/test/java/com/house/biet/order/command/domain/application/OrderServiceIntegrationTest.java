package com.house.biet.order.command.domain.application;

import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.vo.OrderMenu;
import com.house.biet.order.command.domain.vo.PaymentMethod;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("성공 - 주문 생성")
    void createOrder_success() {
        // given
        Long storeId = 1L;
        Long userId = 10L;

        OrderMenu menu = new OrderMenu(
                1L,
                100L,
                OrderFixtures.order(userId).getMenus().get(0).getMenuName(),
                2,
                OrderFixtures.order(userId).getMenus().get(0).getMenuPrice()
        );

        // when
        Order order = orderService.create(
                storeId,
                userId,
                List.of(menu),
                "가게 요청",
                "라이더 요청",
                "서울시 강남구",
                PaymentMethod.CARD,
                LocalDateTime.now()
        );

        // then
        assertThat(order.getId()).isNotNull();
        assertThat(order.getUserId()).isEqualTo(userId);
        assertThat(order.getTotalAmount()).isGreaterThan(0);
    }

    @Test
    @DisplayName("성공 - 주문 메뉴 추가")
    void addMenu_success() {
        // given
        Order order = orderRepository.save(
                OrderFixtures.order(1L)
        );

        OrderMenu newMenu = new OrderMenu(
                1L,
                200L,
                order.getMenus().get(0).getMenuName(),
                1,
                order.getMenus().get(0).getMenuPrice()
        );

        int beforeAmount = order.getTotalAmount();

        // when
        orderService.addMenu(order.getId(), newMenu);

        // then
        Order found = orderRepository.findById(order.getId()).get();
        assertThat(found.getMenus().size()).isEqualTo(2);
        assertThat(found.getTotalAmount()).isGreaterThan(beforeAmount);
    }

    @Test
    @DisplayName("성공 - 주문 메뉴 제거")
    void removeMenu_success() {
        // given
        Order order = orderRepository.save(
                OrderFixtures.order(1L)
        );

        OrderMenu menu = order.getMenus().get(0);

        // when
        orderService.removeMenu(order.getId(), menu);

        // then
        Order found = orderRepository.findById(order.getId()).get();
        assertThat(found.getMenus()).isEmpty();
        assertThat(found.getTotalAmount()).isEqualTo(0);
    }

    @Test
    @DisplayName("성공 - 주문 결제 → 배달중 → 배달완료")
    void orderStatusFlow_success() {
        // given
        Order order = orderRepository.save(
                OrderFixtures.order(1L)
        );

        // when
        orderService.markPaid(order.getId());
        orderService.markDelivering(order.getId());
        orderService.markDelivered(order.getId());

        // then
        Order found = orderRepository.findById(order.getId()).get();
        assertThat(found.isPaid()).isFalse();
        assertThat(found.isDelivered()).isTrue();
    }

    @Test
    @DisplayName("성공 - 주문 취소")
    void cancelOrder_success() {
        // given
        Order order = orderRepository.save(
                OrderFixtures.order(1L)
        );

        // when
        orderService.cancelOrder(order.getId());

        // then
        Order found = orderRepository.findById(order.getId()).get();
        assertThat(found.isCancelled()).isTrue();
    }

    @Test
    @DisplayName("성공 - 비관적 락으로 주문 조회")
    void findOrderForUpdate_success() {
        // given
        Order order = orderRepository.save(
                OrderFixtures.order(1L)
        );

        // when
        Order lockedOrder = orderService.findOrderOrThrowForUpdate(order.getId());

        // then
        assertThat(lockedOrder.getId()).isEqualTo(order.getId());
    }
}
