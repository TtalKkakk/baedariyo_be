package com.house.biet.delivery.command.domain.aggregate;

import com.house.biet.common.domain.enums.DeliveryStatus;
import com.house.biet.global.jpa.BaseTimeEntity;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = true)
    private Long riderId;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    LocalDateTime deliveryStartedAt;

    LocalDateTime deliveryCompleteAt;

    /* =========================
       생성자
       ========================= */

    private Delivery(Long orderId) {
        this.orderId = orderId;
        this.deliveryStatus = DeliveryStatus.READY;
    }

    public static Delivery create(Long orderId) {
        return new Delivery(orderId);
    }
    
    /* =========================
       라이더 배정
       ========================= */

    public void assignRider(Long riderId) {
        validateStatus(DeliveryStatus.READY);

        if (this.riderId != null) {
            throw new CustomException(ErrorCode.ALREADY_ASSIGN_RIDER);
        }

        this.riderId = riderId;
        this.deliveryStatus = DeliveryStatus.ASSIGNED;
    }

    /* =========================
       픽업 완료
       ========================= */

    public void pickup() {
        validateStatus(DeliveryStatus.ASSIGNED);

        this.deliveryStatus = DeliveryStatus.PICKED_UP;
    }

    /* =========================
       배달 시작
       ========================= */

    public void startDelivery() {
        validateStatus(DeliveryStatus.PICKED_UP);

        this.deliveryStatus = DeliveryStatus.IN_DELIVERY;
        this.deliveryStartedAt = LocalDateTime.now();
    }

    /* =========================
       위치 상태 업데이트
       ========================= */

    public void validateLocationUpdate(Long riderId) {
        if (!this.riderId.equals(riderId)) {
            throw new CustomException(ErrorCode.ORDER_RIDER_MISMATCH);
        }

        validateStatus(DeliveryStatus.IN_DELIVERY);
    }

    /* =========================
       배달 완료
       ========================= */

    public void complete() {
        validateStatus(DeliveryStatus.IN_DELIVERY);

        this.deliveryStatus = DeliveryStatus.COMPLETED;
        this.deliveryCompleteAt = LocalDateTime.now();
    }

    /* =========================
       취소
       ========================= */

    public void cancel() {
        if (this.deliveryStatus == DeliveryStatus.CANCELLED)
            throw new CustomException(ErrorCode.INVALID_DELIVERY_STATUS_TRANSITION);

        this.deliveryStatus = DeliveryStatus.CANCELLED;
    }

    /* =========================
       상태 검증
       ========================= */

    private void validateStatus(DeliveryStatus expected) {
        if (this.deliveryStatus != expected) {
            throw new CustomException(ErrorCode.INVALID_DELIVERY_STATUS_TRANSITION);
        }
    }
}