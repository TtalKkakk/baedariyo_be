package com.house.biet.order.query;

import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.infrastructure.OrderRepositoryJpaAdapter;
import com.house.biet.order.query.repository.OrderQueryRepositoryJpaAdapter;
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
})
@ActiveProfiles("test")
class OrderQueryRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderQueryRepository orderQueryRepository;

    @DisplayName("성공 - 배달원 ID 조회")
    @Test
    void findRiderIdByOrderId_Success() {
        // Given
        Long riderId = 3L;

        Order order = OrderFixtures.order(1L);
        order.markPaid();
        order.assignRider(riderId);

        Order savedOrder = orderRepository.save(order);
        Long orderId = savedOrder.getId();

        // When
        Long result = orderQueryRepository.findRiderIdByOrderId(orderId);

        // Then
        assertThat(result).isEqualTo(riderId);
    }
    
    @DisplayName("실패 - 주문이 존재하지 않는 경우")
    @Test
    void findRiderIdByOrderId_Error_ORDER_NOT_FOUND() {
        // Given
        Long notExistsOrderId = 99999L;

        // When & Then
        assertThatThrownBy(() -> orderQueryRepository.findRiderIdByOrderId(notExistsOrderId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND_OR_RIDER_NOT_ASSIGNED.getMessage());
    }

    @DisplayName("실패 - 배달원이 배정되지 않은 경우")
    @Test
    void findRiderIdByOrderId_Error_RIDER_NOT_ASSIGNED() {
        // Given
        Order order = OrderFixtures.order(1L);

        Order savedOrder = orderRepository.save(order);
        Long orderId = savedOrder.getId();

        // When & Then
        assertThatThrownBy(() -> orderQueryRepository.findRiderIdByOrderId(orderId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND_OR_RIDER_NOT_ASSIGNED.getMessage());
    }
}