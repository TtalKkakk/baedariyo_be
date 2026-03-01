package com.house.biet.delivery.command.domain.aggregate;

import com.house.biet.common.domain.enums.DeliveryStatus;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DeliveryTest {

    @Test
    @DisplayName("성공 - Delivery 생성 시 상태는 READY")
    void create_Success() {
        // when
        Delivery delivery = Delivery.create(1L);

        // then
        assertThat(delivery.getOrderId()).isEqualTo(1L);
        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.READY);
    }

    @Test
    @DisplayName("성공 - READY 상태에서 라이더 배정")
    void assignRider_Success() {

        // given
        Delivery delivery = Delivery.create(1L);

        // when
        delivery.assignRider(100L);

        // then
        assertThat(delivery.getRiderId()).isEqualTo(100L);
        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.ASSIGNED);
    }

    @Test
    @DisplayName("실패 - READY 상태가 아니면 라이더 배정 불가")
    void assignRider_Error_InvalidStatus() {

        // given
        Delivery delivery = Delivery.create(1L);
        delivery.assignRider(100L);

        // when & then
        assertThatThrownBy(() -> delivery.assignRider(200L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_DELIVERY_STATUS_TRANSITION.getMessage());
    }

    @Test
    @DisplayName("성공 - ASSIGNED 상태에서 픽업")
    void pickup_Success() {

        // given
        Delivery delivery = Delivery.create(1L);
        delivery.assignRider(100L);

        // when
        delivery.pickup();

        // then
        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.PICKED_UP);
    }

    @Test
    @DisplayName("실패 - ASSIGNED 상태가 아니면 픽업 불가")
    void pickup_Error_InvalidStatus() {

        // given
        Delivery delivery = Delivery.create(1L);

        // when & then
        assertThatThrownBy(delivery::pickup)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_DELIVERY_STATUS_TRANSITION.getMessage());
    }

    @Test
    @DisplayName("성공 - PICKED_UP 상태에서 배달 시작")
    void startDelivery_Success() {

        // given
        Delivery delivery = Delivery.create(1L);
        delivery.assignRider(100L);
        delivery.pickup();

        // when
        delivery.startDelivery();

        // then
        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.IN_DELIVERY);
        assertThat(delivery.getDeliveryStartedAt()).isNotNull();
    }

    @Test
    @DisplayName("실패 - PICKED_UP 상태가 아니면 배달 시작 불가")
    void startDelivery_Error_InvalidStatus() {

        // given
        Delivery delivery = Delivery.create(1L);
        delivery.assignRider(100L);

        // when & then
        assertThatThrownBy(delivery::startDelivery)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_DELIVERY_STATUS_TRANSITION.getMessage());
    }

    @Test
    @DisplayName("실패 - 현재 rider와 입력된 rider Id가 동일하지 않으면 에러")
    void validateLocationUpdate_Error_NotCorrectRiderId() {
        // given
        Delivery delivery = Delivery.create(1L);
        delivery.assignRider(100L);

        Long anotherRiderId = 9999L;

        // when & then
        assertThatThrownBy(() -> delivery.validateLocationUpdate(anotherRiderId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ORDER_RIDER_MISMATCH.getMessage());
    }

    @Test
    @DisplayName("실패 - IN_DELIVERY 상태가 아니면 상태 변경 불가")
    void validateLocationUpdate_Error_InvalidStatus() {
        // given
        Long riderId = 100L;

        Delivery delivery = Delivery.create(1L);
        delivery.assignRider(riderId);

        // when & then
        assertThatThrownBy(() -> delivery.validateLocationUpdate(riderId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_DELIVERY_STATUS_TRANSITION.getMessage());
    }


    @Test
    @DisplayName("성공 - IN_DELIVERY 상태에서 배달 완료")
    void complete_Success() {
        // given
        Delivery delivery = Delivery.create(1L);
        delivery.assignRider(100L);
        delivery.pickup();
        delivery.startDelivery();

        // when
        delivery.complete();

        // then
        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.COMPLETED);
    }

    @Test
    @DisplayName("실패 - IN_DELIVERY 상태가 아니면 완료 불가")
    void complete_Error() {

        // given
        Delivery delivery = Delivery.create(1L);
        delivery.assignRider(100L);
        delivery.pickup();

        // when & then
        assertThatThrownBy(delivery::complete)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_DELIVERY_STATUS_TRANSITION.getMessage());
    }
}