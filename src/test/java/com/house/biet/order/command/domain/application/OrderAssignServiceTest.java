package com.house.biet.order.command.domain.application;

import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.domain.aggregate.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderAssignServiceTest {

    @InjectMocks
    private OrderAssignService orderAssignService;

    @Mock
    private OrderRepository orderRepository;

    @Test
    @DisplayName("성공 - 주문에 라이더를 배정")
    void assignRider_Success() {
        // given
        Long userId = 2L;
        Long orderId = 4L;
        Long riderId = 10L;

        Order order = OrderFixtures.order(userId, null);
        order.markPaid();

        when(orderRepository.findByIdForUpdate(orderId))
                .thenReturn(Optional.of(order));

        // when
        orderAssignService.assignRider(orderId, riderId);

        // then
        assertThat(order.getRiderId()).isEqualTo(riderId);
        verify(orderRepository).findByIdForUpdate(orderId);
    }

    @Test
    @DisplayName("에러 - 주문이 존재하지 않음")
    void assignRider_Error_OrderNotFound() {
        // given
        Long orderId = 1L;
        Long riderId = 10L;

        when(orderRepository.findByIdForUpdate(orderId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderAssignService.assignRider(orderId, riderId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND.getMessage());
    }
}
