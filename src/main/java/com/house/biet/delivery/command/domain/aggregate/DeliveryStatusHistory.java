package com.house.biet.delivery.command.domain.aggregate;

import com.house.biet.common.domain.enums.DeliveryStatus;
import com.house.biet.global.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_status_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryStatusHistory extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus toStatus;

    public DeliveryStatusHistory(Delivery delivery,
                                 DeliveryStatus fromStatus,
                                 DeliveryStatus toStatus) {
        this.delivery = delivery;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }
}
