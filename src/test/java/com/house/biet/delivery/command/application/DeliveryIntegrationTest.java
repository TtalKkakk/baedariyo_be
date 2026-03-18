package com.house.biet.delivery.command.application;


import com.house.biet.common.domain.enums.DeliveryStatus;
import com.house.biet.common.domain.enums.OrderStatus;
import com.house.biet.common.domain.enums.PaymentMethod;
import com.house.biet.common.domain.vo.Address;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.delivery.infrastructure.redis.DeliveryLocationRedisRepository;
import com.house.biet.fixtures.AddressFixtures;
import com.house.biet.fixtures.DeliveryLocationFixtures;
import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.order.command.application.OrderService;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.vo.DeliveryLocation;
import com.house.biet.order.command.domain.vo.OrderMenu;
import com.house.biet.payment.command.application.PaymentService;
import com.house.biet.delivery.command.domain.aggregate.Delivery;
import com.house.biet.support.config.TestRiderCallSenderConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestRiderCallSenderConfig.class)
class DeliveryIntegrationTest {

    @Autowired OrderService orderService;
    @Autowired PaymentService paymentService;
    @Autowired DeliveryService deliveryService;

    @MockitoBean
    private DeliveryLocationRedisRepository deliveryLocationRedisRepository;

    @DisplayName("성공 - 주문부터 배달 완료까지 전체 흐름 성공")
    @Test
    void orderToDeliveryFlow_Success() {

        Long userId = 1L;

        OrderMenu menu = new OrderMenu(
                1L,
                100L,
                OrderFixtures.order(userId).getMenus().get(0).getOrderMenuName(),
                2,
                OrderFixtures.order(userId).getMenus().get(0).getMenuPrice()
        );

        Address addressMapo = AddressFixtures.mapoAddress();
        DeliveryLocation deliveryLocationMapo = DeliveryLocationFixtures.seoulMapo();

        // 1️⃣ 주문 생성 (DB 저장)
        Long orderId = orderService.create(
                1L,
                userId,
                List.of(menu),
                "가게 요청",
                "라이더 요청",
                addressMapo,
                deliveryLocationMapo,
                PaymentMethod.CARD,
                LocalDateTime.now()
        ).getId();

        deliveryService.create(orderId);

        // 2️⃣ 결제 생성
        Long paymentId = paymentService.createPayment(
                orderId,
                userId,
                new Money(10000),
                "pk-1"
        );

        // 3️⃣ 결제 요청
        paymentService.request(paymentId);

        // 4️⃣ 결제 승인 (여기서 이벤트 발생)
        paymentService.approve(paymentId, "TX-1");

        // 🔥 여기서 Order가 ORDERED 되어야 함
        Order confirmedOrder = orderService.getOrder(orderId);
        assertThat(confirmedOrder.getStatus())
                .isEqualTo(OrderStatus.ORDERED);

        // 🔥 Delivery가 자동 생성되었는지 확인
        Delivery delivery = deliveryService.getByOrderId(orderId);

        // order 결제
        orderService.markPaid(orderId);
        Order paidOrder = orderService.getOrder(orderId);
        assertThat(paidOrder.getStatus()).isEqualTo(OrderStatus.PAID);

        // 5️⃣ 배달 흐름 진행
        // 라이더 부여
        deliveryService.assignRider(orderId, 100L);
        deliveryService.pickUp(orderId);

        // 라이더 배달중
        deliveryService.inDelivery(orderId);
        orderService.markDelivering(orderId);
        Order DeliveringOrder = orderService.getOrder(orderId);
        assertThat(DeliveringOrder.getStatus()).isEqualTo(OrderStatus.DELIVERING);

        // 라이더 배달 완료
        deliveryService.complete(orderId);
        orderService.markDelivered(orderId);

        // 최종 검증
        Order finalOrder = orderService.getOrder(orderId);
        assertThat(finalOrder.getStatus()).isEqualTo(OrderStatus.DELIVERED);

        Delivery finalDelivery = deliveryService.getByOrderId(orderId);
        assertThat(finalDelivery.getDeliveryStatus())
                .isEqualTo(DeliveryStatus.COMPLETED);
    }
}