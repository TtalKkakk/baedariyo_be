package com.house.biet.order.command.application;

import com.house.biet.fixtures.OrderCreateRequestDtoFixtures;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.application.OrderService;
import com.house.biet.order.command.application.OrderUserFacade;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.dto.OrderCreateRequestDto;
import com.house.biet.order.command.domain.vo.PaymentMethod;
import com.house.biet.user.query.UserQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUserFacadeTest {

    @InjectMocks
    private OrderUserFacade orderUserFacade;

    @Mock
    private OrderService orderService;

    @Mock
    private UserQueryService userQueryService;

    @Test
    @DisplayName("성공 - 주문 시 생성")
    void createOrderAndNotify_Success() {
        // given
        Long accountId = 1L;
        Long userId = 10L;
        Long storeId = 100L;

        OrderCreateRequestDto requestDto = OrderCreateRequestDtoFixtures.sample(storeId);

        Order order = mock(Order.class);

        given(userQueryService.getUserIdByAccountId(accountId))
                .willReturn(userId);

        given(orderService.create(
                eq(storeId),
                eq(userId),
                anyList(),
                any(String.class),
                any(String.class),
                any(String.class),
                any(PaymentMethod.class),
                any(LocalDateTime.class)
        )).willReturn(order);

        // when
        Order resultOrder = orderUserFacade.createOrderAndNotify(accountId, requestDto);

        // then
        assertThat(resultOrder).isEqualTo(order);

        verify(userQueryService).getUserIdByAccountId(accountId);
        verify(orderService).create(
                eq(storeId),
                eq(userId),
                anyList(),
                any(String.class),
                any(String.class),
                any(String.class),
                any(PaymentMethod.class),
                any(LocalDateTime.class)
        );
    }
    
    @Test
    @DisplayName("에러 - 존재하지 않은 accountId 로 조회")
    void createOrderAndNotify_Error_userNotFound() {
        // given
        Long notExistsAccountId = -1L;
        OrderCreateRequestDto requestDto = OrderCreateRequestDtoFixtures.sample(1L);

        given(userQueryService.getUserIdByAccountId(notExistsAccountId))
                .willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> orderUserFacade.createOrderAndNotify(notExistsAccountId, requestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }
}