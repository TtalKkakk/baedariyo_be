package com.house.biet.delivery.command.domain.aggregate;

import com.house.biet.common.domain.enums.DeliveryStatus;
import com.house.biet.delivery.command.domain.event.DeliveryCompletedEvent;
import com.house.biet.global.jpa.BaseTimeEntity;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "deliveries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = true)
    private Long riderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    LocalDateTime deliveryStartedAt;

    LocalDateTime deliveryCompleteAt;

    /* =========================
   상태 이력
   ========================= */

    @OneToMany(mappedBy = "delivery",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<DeliveryStatusHistory> histories = new ArrayList<>();

    /* =========================
    도메인 이벤트
    ========================= */

    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

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
        changeStatus(DeliveryStatus.ASSIGNED);
    }

    /* =========================
       픽업 완료
       ========================= */

    public void pickup() {
        validateStatus(DeliveryStatus.ASSIGNED);

        changeStatus(DeliveryStatus.PICKED_UP);
    }

    /* =========================
       배달 시작
       ========================= */

    public void startDelivery() {
        validateStatus(DeliveryStatus.PICKED_UP);

        this.deliveryStartedAt = LocalDateTime.now();
        changeStatus(DeliveryStatus.IN_DELIVERY);
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

        this.deliveryCompleteAt = LocalDateTime.now();
        changeStatus(DeliveryStatus.COMPLETED);

        domainEvents.add(new DeliveryCompletedEvent(this.id, this.orderId, this.riderId));
    }

    /* =========================
       취소
       ========================= */

    public void cancel() {
        if (this.deliveryStatus == DeliveryStatus.COMPLETED || this.deliveryStatus == DeliveryStatus.CANCELLED) {
            throw new CustomException(ErrorCode.INVALID_DELIVERY_STATUS_TRANSITION);
        }

        this.deliveryStatus = DeliveryStatus.CANCELLED;
    }

    /* =========================
       상태 변경 중앙화
       ========================= */

    private void changeStatus(DeliveryStatus newStatus) {
        DeliveryStatus oldStatus = this.deliveryStatus;
        this.deliveryStatus = newStatus;

        this.histories.add(new DeliveryStatusHistory(this, oldStatus, newStatus));
    }

    private void validateStatus(DeliveryStatus expected) {
        if (this.deliveryStatus != expected) {
            throw new CustomException(ErrorCode.INVALID_DELIVERY_STATUS_TRANSITION);
        }
    }

    /* =========================
   도메인 이벤트 반환
   ========================= */
    public void clearDomainEvents() {
        domainEvents.clear();
    }
}