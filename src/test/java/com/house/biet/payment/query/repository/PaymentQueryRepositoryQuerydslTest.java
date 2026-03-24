package com.house.biet.payment.query.repository;

import com.house.biet.common.domain.enums.PaymentMethod;
import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.fixtures.AddressFixtures;
import com.house.biet.fixtures.DeliveryLocationFixtures;
import com.house.biet.fixtures.StoreFixture;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.vo.OrderMenu;
import com.house.biet.order.command.domain.vo.OrderMenuName;
import com.house.biet.order.command.infrastructure.OrderRepositoryJpaAdapter;
import com.house.biet.payment.command.PaymentRepository;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.command.domain.vo.TransactionId;
import com.house.biet.payment.command.repository.PaymentRepositoryJpaAdapter;
import com.house.biet.payment.query.PaymentQueryRepository;
import com.house.biet.payment.query.application.dto.MyPaymentDetailResponseDto;
import com.house.biet.payment.query.application.dto.MyPaymentSearchCondition;
import com.house.biet.store.command.StoreRepository;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.repository.StoreRepositoryJpaAdapter;
import com.house.biet.support.config.QueryDslTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        StoreRepositoryJpaAdapter.class,
        OrderRepositoryJpaAdapter.class,
        PaymentRepositoryJpaAdapter.class,
        PaymentQueryRepositoryJpaAdapter.class,
        PaymentQueryRepositoryQuerydsl.class,
        QueryDslTestConfig.class
})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentQueryRepositoryQuerydslTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentQueryRepository paymentQueryRepository;

    Long userId;
    Payment ready;
    Payment approved;

    @BeforeEach
    void setup() {
        userId = 1L;

        // Store 생성
        Store store = storeRepository.save(StoreFixture.createStore());

        // Order 생성
        Order order1 = orderRepository.save(
                new Order(
                        1L,
                        userId,
                        List.of(new OrderMenu(
                                store.getId(),
                                1L,
                                new OrderMenuName("menu1"),
                                2,
                                new Money(10000)
                                )
                        ),
                        "storeRequest1",
                        "riderRequest1",
                        AddressFixtures.gangnamAddress(),
                        DeliveryLocationFixtures.seoulGangnam(),
                        PaymentMethod.CARD,
                        LocalDateTime.now()
                )
        );

        Order order2 = orderRepository.save(
                new Order(
                        1L,
                        userId,
                        List.of(new OrderMenu(
                                store.getId(),
                                2L,
                                new OrderMenuName("menu2"),
                                3,
                                new Money(14000)
                                )
                        ),
                        "storeRequest2",
                        "riderRequest2",
                        AddressFixtures.gangnamAddress(),
                        DeliveryLocationFixtures.seoulGangnam(),
                        PaymentMethod.CARD,
                        LocalDateTime.now()
                )
        );

        // Payment 생성
        ready = Payment.create(
                order1.getId(), userId,
                new Money(10000),
                new PaymentKey("pk_q_1")
        );

        approved = Payment.create(
                order2.getId(), userId,
                new Money(20000),
                new PaymentKey("pk_q_2")
        );

        approved.request();
        approved.approve(new TransactionId("tx_q_1"));

        paymentRepository.save(ready);
        paymentRepository.save(approved);
    }


    @Test
    @DisplayName("성공 - userId로 전체 결제 조회 (status null)")
    void findMyPaymentDetailList_Success_AllStatus() {
        // when
        List<MyPaymentDetailResponseDto> result =
                paymentQueryRepository.findMyPaymentDetailList(
                        new MyPaymentSearchCondition(userId, null)
                );

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("성공 - userId + status 조건으로 결제 조회")
    void findMyPaymentDetailList_Success_WithStatus() {
        // when
        List<MyPaymentDetailResponseDto> resultReady =
                paymentQueryRepository.findMyPaymentDetailList(
                        new MyPaymentSearchCondition(userId, PaymentStatus.READY)
                );

        List<MyPaymentDetailResponseDto> resultApproved =
                paymentQueryRepository.findMyPaymentDetailList(
                        new MyPaymentSearchCondition(userId, PaymentStatus.APPROVED)
                );

        // then
        assertThat(resultReady).hasSize(1);
        assertThat(resultReady.get(0).paymentStatus())
                .isEqualTo(PaymentStatus.READY);

        assertThat(resultApproved).hasSize(1);
        assertThat(resultApproved.get(0).paymentStatus())
                .isEqualTo(PaymentStatus.APPROVED);
    }
}