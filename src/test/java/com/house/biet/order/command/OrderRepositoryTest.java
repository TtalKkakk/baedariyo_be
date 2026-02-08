package com.house.biet.order.command;

import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.infrastructure.OrderRepositoryJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(OrderRepositoryJpaAdapter.class)
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    void setup() {
        Long userId = 1L;

        order = OrderFixtures.order(userId);
        orderRepository.save(order);
    }

    @Test
    @DisplayName("성공 - 주문 존재 여부 확인")
    void existsById_Success() {
        boolean exists = orderRepository.existsById(order.getId());
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 주문 ID")
    void existsById_False() {
        boolean exists = orderRepository.existsById(9999L);
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("성공 - 주문 저장 및 조회")
    void saveAndFind_Success() {
        Optional<Order> found = orderRepository.findById(order.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(order.getId());
        assertThat(found.get().getUserId()).isEqualTo(order.getUserId());
        assertThat(found.get().getRiderId()).isEqualTo(order.getRiderId());
    }

    @Test
    @DisplayName("성공 - 비관적 락 조회 가능")
    void findByIdForUpdate_Success() {
        Optional<Order> found = orderRepository.findByIdForUpdate(order.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(order.getId());
    }
}
