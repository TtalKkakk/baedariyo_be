package com.house.biet.order.query.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.query.OrderQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderQueryServiceTest {

    @Mock
    private OrderQueryRepository orderQueryRepository;

    @InjectMocks
    private OrderQueryServiceImpl orderQueryService;

    @Test
    @DisplayName("성공 - 주문의 배달원 일치")
    void isRiderOfOrder_Success() {
        // given
        Long orderId = 1L;
        Long riderId = 10L;

        given(orderQueryRepository.findRiderIdByOrderId(orderId))
                .willReturn(riderId);

        // when
        boolean result = orderQueryService.isRiderOfOrder(orderId, riderId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("성공 - 주문의 배달원 불일치")
    void isRiderOfOrder_Success_NotMatch() {
        // given
        Long orderId = 1L;
        Long assignedRiderId = 10L;
        Long otherRiderId = 20L;

        given(orderQueryRepository.findRiderIdByOrderId(orderId))
                .willReturn(assignedRiderId);

        // when
        boolean result = orderQueryService.isRiderOfOrder(orderId, otherRiderId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("실패 - 주문이 존재하지 않는 경우")
    void isRiderOfOrder_Error_ORDER_NOT_FOUND() {
        // given
        Long orderId = 1L;

        given(orderQueryRepository.findRiderIdByOrderId(orderId))
                .willThrow(new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // when & then
        assertThatThrownBy(() ->
                orderQueryService.isRiderOfOrder(orderId, 10L)
        )
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ORDER_NOT_FOUND);
    }
}