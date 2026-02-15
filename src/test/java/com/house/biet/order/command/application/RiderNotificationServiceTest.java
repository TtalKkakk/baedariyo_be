package com.house.biet.order.command.application;

import com.house.biet.order.command.application.port.RiderCandidate;
import com.house.biet.order.command.application.port.RiderFinder;
import com.house.biet.order.command.domain.event.OrderCreatedEvent;
import com.house.biet.order.command.infrastructure.messaging.rider.RiderCallSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RiderNotificationServiceTest {

    @InjectMocks
    RiderNotificationService riderNotificationService;

    @Mock
    RiderFinder riderFinder;

    @Mock
    RiderCallSender riderCallSender;

    @Test
    @DisplayName("성공 - 라이더 알림 서비스")
    void notifyNearByRiders_Success() {
        // given
        OrderCreatedEvent event = new OrderCreatedEvent(
                1L,
                2L,
                new OrderCreatedEvent.PickupLocationDto(
                        36.32,
                        127.12,
                        "서울시 강남구"
                ),
                LocalDateTime.now()
        );

        RiderCandidate candidate = new RiderCandidate(1L, 0.3);

        given(riderFinder.findNearby(any(), anyDouble()))
                .willReturn(List.of(candidate));

        // when
        riderNotificationService.notifyNearByRiders(event);

        // then
        verify(riderCallSender).send(candidate, event);
    }
}