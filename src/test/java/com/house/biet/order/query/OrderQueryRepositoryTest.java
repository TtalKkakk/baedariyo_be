package com.house.biet.order.query;

import com.house.biet.common.domain.enums.OrderStatus;
import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.fixtures.StoreFixture;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.infrastructure.OrderRepositoryJpaAdapter;
import com.house.biet.order.query.repository.OrderQueryRepositoryJpaAdapter;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.vo.StoreName;
import com.house.biet.support.config.QueryDslTestConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({
        OrderRepositoryJpaAdapter.class,
        OrderQueryRepositoryJpaAdapter.class,
        QueryDslTestConfig.class,
})
@ActiveProfiles("test")
class OrderQueryRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderQueryRepository orderQueryRepository;

    @Autowired
    private EntityManager em;

    /* =========================
     * Rider 조회 테스트
     * ========================= */

    @Test
    @DisplayName("성공 - 배달원 ID 조회")
    void findRiderIdByOrderId_Success() {
        // given
        Long riderId = 3L;

        Store store = StoreFixture.createStore();
        em.persist(store);

        Order order = OrderFixtures.order(store.getId(), 1L);
        order.markPaid();
        order.assignRider(riderId);

        Order savedOrder = orderRepository.save(order);

        em.flush();
        em.clear();

        // when
        Long result = orderQueryRepository.findRiderIdByOrderId(savedOrder.getId());

        // then
        assertThat(result).isEqualTo(riderId);
    }

    @Test
    @DisplayName("실패 - 주문이 존재하지 않는 경우")
    void findRiderIdByOrderId_Error_OrderNotFound() {
        // given
        Long notExistsOrderId = 99999L;

        // when & then
        assertThatThrownBy(() -> orderQueryRepository.findRiderIdByOrderId(notExistsOrderId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND_OR_RIDER_NOT_ASSIGNED.getMessage());
    }

    @Test
    @DisplayName("실패 - 배달원이 배정되지 않은 경우")
    void findRiderIdByOrderId_Error_RiderNotAssigned() {
        // given
        Store store = StoreFixture.createStore();
        em.persist(store);

        Order order = OrderFixtures.order(store.getId(), 1L);
        Order savedOrder = orderRepository.save(order);

        em.flush();
        em.clear();

        // when & then
        assertThatThrownBy(() -> orderQueryRepository.findRiderIdByOrderId(savedOrder.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND_OR_RIDER_NOT_ASSIGNED.getMessage());
    }

    /* =========================
     * 주문 요약 조회 테스트
     * ========================= */

    @Test
    @DisplayName("성공 - 상태로 주문 요약 목록 조회")
    void findOrderSummariesByOrderStatus_Success() {
        // given
        Store store = StoreFixture.createStore();
        em.persist(store);

        Order paid1 = OrderFixtures.order(store.getId(), 1L);
        paid1.markPaid();

        Order paid2 = OrderFixtures.order(store.getId(), 2L);
        paid2.markPaid();

        Order notPaid = OrderFixtures.order(store.getId(), 3L);

        em.persist(paid1);
        em.persist(paid2);
        em.persist(notPaid);

        em.flush();
        em.clear();

        // when
        var result = orderQueryRepository
                .findOrderSummariesByOrderStatus(OrderStatus.PAID);

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("성공 - 해당 상태 주문이 없으면 빈 리스트 반환")
    void findOrderSummariesByOrderStatus_Success_Empty() {
        // given
        Store store = StoreFixture.createStore();
        em.persist(store);

        Order order = OrderFixtures.order(store.getId(), 1L);
        em.persist(order);

        em.flush();
        em.clear();

        // when
        var result = orderQueryRepository
                .findOrderSummariesByOrderStatus(OrderStatus.PAID);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("성공 - 주문은 최신순으로 정렬된다")
    void findOrderSummariesByOrderStatus_Success_Sorted() {
        // given
        Store store = StoreFixture.createStore();
        em.persist(store);

        Order order1 = OrderFixtures.order(store.getId(), 1L);
        order1.markPaid();

        Order order2 = OrderFixtures.order(store.getId(), 2L);
        order2.markPaid();

        em.persist(order1);
        em.persist(order2);

        em.flush();
        em.clear();

        // when
        var result = orderQueryRepository
                .findOrderSummariesByOrderStatus(OrderStatus.PAID);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).orderId())
                .isGreaterThan(result.get(1).orderId()); // 🔥 안정 정렬 검증
    }

    @Test
    @DisplayName("성공 - 주문 조회 시 store 정보가 포함된다")
    void findOrderSummariesByOrderStatus_Success_WithStore() {
        // given
        Store store = StoreFixture.createStore();
        em.persist(store);

        Order order = OrderFixtures.order(store.getId(), 1L);
        order.markPaid();
        em.persist(order);

        em.flush();
        em.clear();

        // when
        var result = orderQueryRepository
                .findOrderSummariesByOrderStatus(OrderStatus.PAID);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).storeName()).isNotNull();
        assertThat(result.get(0).storeAddress()).isNotBlank();
        assertThat(result.get(0).customerAddress()).isNotBlank();
        assertThat(result.get(0).fee()).isPositive();
    }
}
