package com.house.biet.delivery.command.application;

import com.house.biet.delivery.command.DeliveryRepository;
import com.house.biet.delivery.command.domain.aggregate.Delivery;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private Delivery delivery;

    @InjectMocks
    private DeliveryService deliveryService;

    @Test
    @DisplayName("성공 - 배달 생성 성공")
    void create_Success() {
        // given
        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.empty());

        Delivery newDelivery = Delivery.create(1L);

        given(deliveryRepository.save(any(Delivery.class)))
                .willReturn(newDelivery);

        // when
        Delivery result = deliveryService.create(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("에러 - 이미 존재하는 배달 생성 시 예외")
    void create_Error_alreadyExists() {
        // given
        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        // when & then
        assertThatThrownBy(() -> deliveryService.create(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DELIVERY_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("성공 - 라이더 배정 성공")
    void assignRider_Success() {
        // given
        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        // when
        deliveryService.assignRider(1L, 100L);

        // then
        verify(delivery).assignRider(100L);
    }

    @Test
    @DisplayName("성공 - 픽업 상태 변경")
    void pickup_Success() {
        // given
        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        // when
        deliveryService.pickUp(1L);

        // then
        verify(delivery).pickup();
    }

    @Test
    @DisplayName("성공 - 배달 중 상태 변경")
    void inDelivery_Success() {
        // given
        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        // when
        deliveryService.inDelivery(1L);

        // then
        verify(delivery).startDelivery();
    }

    @Test
    @DisplayName("성공 - 배달 완료")
    void complete_Success() {
        // given
        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        // when
        deliveryService.complete(1L);

        // then
        verify(delivery).complete();
    }

    @Test
    @DisplayName("성공 - 배달 취소")
    void cancel_Success() {
        // given
        given(deliveryRepository.findByOrderId(1L))
                .willReturn(Optional.of(delivery));

        // when
        deliveryService.cancel(1L);

        // then
        verify(delivery).cancel();
    }

    @Test
    @DisplayName("에러 - 존재하지 않는 주문 조회 시 예외")
    void getByOrderId_Error() {
        // given
        given(deliveryRepository.findByOrderId(999L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> deliveryService.getByOrderId(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DELIVERY_NOT_FOUND.getMessage());
    }
}