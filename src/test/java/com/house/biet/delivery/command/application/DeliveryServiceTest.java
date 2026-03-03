package com.house.biet.delivery.command.application;

import com.house.biet.common.domain.enums.DeliveryStatus;
import com.house.biet.delivery.command.DeliveryRepository;
import com.house.biet.delivery.command.domain.aggregate.Delivery;
import com.house.biet.delivery.command.domain.event.DeliveryCompletedEvent;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DeliveryService deliveryService;

    private Delivery delivery;

    @BeforeEach
    void setup() {
        delivery = Delivery.create(1L);
    }

    @Test
    @DisplayName("성공 - 배달 생성 성공")
    void create_Success() {

        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.empty());

        given(deliveryRepository.save(any(Delivery.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        Delivery result = deliveryService.create(1L);

        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(1L);
        assertThat(result.getDeliveryStatus()).isEqualTo(DeliveryStatus.READY);
    }

    @Test
    @DisplayName("에러 - 이미 존재하는 배달 생성 시 예외")
    void create_Error_alreadyExists() {

        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        assertThatThrownBy(() -> deliveryService.create(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DELIVERY_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("성공 - 라이더 배정 성공")
    void assignRider_Success() {

        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        deliveryService.assignRider(1L, 100L);

        assertThat(delivery.getRiderId()).isEqualTo(100L);
        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.ASSIGNED);
    }

    @Test
    @DisplayName("성공 - 픽업 상태 변경")
    void pickup_Success() {

        delivery.assignRider(100L);

        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        deliveryService.pickUp(1L);

        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.PICKED_UP);
    }

    @Test
    @DisplayName("성공 - 배달 중 상태 변경")
    void inDelivery_Success() {

        delivery.assignRider(100L);
        delivery.pickup();

        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        deliveryService.inDelivery(1L);

        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.IN_DELIVERY);
    }

    @Test
    @DisplayName("성공 - 배달 완료")
    void complete_Success() {

        delivery.assignRider(100L);
        delivery.pickup();
        delivery.startDelivery();

        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        deliveryService.complete(1L);

        // 상태 검증
        assertThat(delivery.getDeliveryStatus())
                .isEqualTo(DeliveryStatus.COMPLETED);

        // 이벤트 발행 검증
        verify(eventPublisher).publishEvent(any(DeliveryCompletedEvent.class));
    }

    @Test
    @DisplayName("성공 - 배달 취소")
    void cancel_Success() {

        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        deliveryService.cancel(1L);

        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.CANCELLED);
    }

    @Test
    @DisplayName("에러 - 존재하지 않는 주문 조회 시 예외")
    void getByOrderId_Error() {

        given(deliveryRepository.findByOrderId(999L))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryService.getByOrderId(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DELIVERY_NOT_FOUND.getMessage());
    }
}