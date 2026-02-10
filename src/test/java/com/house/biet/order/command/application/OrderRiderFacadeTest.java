package com.house.biet.order.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.application.OrderRiderAssignService;
import com.house.biet.order.command.application.OrderRiderFacade;
import com.house.biet.rider.query.RiderQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderRiderFacadeTest {

    @InjectMocks
    private OrderRiderFacade orderRiderFacade;

    @Mock
    private RiderQueryService riderQueryService;

    @Mock
    private OrderRiderAssignService orderRiderAssignService;

    @Test
    @DisplayName("성공 - Rider가 AccountId로 조회되어 주문에 배정")
    void assignRider_Success() {
        // given
        Long accountId = 10L;
        Long orderId = 100L;
        Long riderId = 1L;

        when(riderQueryService.getRiderIdByAccountId(accountId))
                .thenReturn(riderId);

        // when
        orderRiderFacade.assignRider(accountId, orderId);

        // then
        verify(riderQueryService, times(1)).getRiderIdByAccountId(accountId);
        verify(orderRiderAssignService, times(1)).assignRider(orderId, riderId);
    }

    @Test
    @DisplayName("실패 - RiderQueryService에서 조회 실패 시 예외 발생")
    void assignRider_Fail_RiderNotFound() {
        // given
        Long accountId = 10L;
        Long orderId = 100L;

        when(riderQueryService.getRiderIdByAccountId(accountId))
                .thenThrow(new CustomException(ErrorCode.RIDER_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> orderRiderFacade.assignRider(accountId, orderId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.RIDER_NOT_FOUND.getMessage());

        verify(riderQueryService, times(1)).getRiderIdByAccountId(accountId);
        verify(orderRiderAssignService, never()).assignRider(anyLong(), anyLong());
    }
}
