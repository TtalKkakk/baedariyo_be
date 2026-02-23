package com.house.biet.payment.command.domain.aggregate;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.command.domain.vo.TransactionId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 도메인의 Aggregate Root.
 *
 * <p>
 * Payment는 금전 거래의 상태를 관리하는 핵심 Aggregate이다.
 * 상태 기반 모델로 설계되었으며,
 * 외부 PG와의 승인 결과에 따라 상태가 전이된다.
 * </p>
 *
 * <p>
 * 불변 요소:
 * - orderId
 * - money
 * - paymentKey (중복 방지)
 *
 * 가변 요소:
 * - status
 * - transactionId (승인 시 설정)
 * </p>
 */
@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    /** 결제가 발생한 주문 ID */
    @Column(nullable = false)
    private Long orderId;

    /** 결제 금액 */
    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "money", nullable = false)
    )
    private Money money;

    /** 결제 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    /**
     * 중복 결제 방지를 위한 키.
     * 클라이언트 또는 시스템이 생성한다.
     */
    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "payment_key", nullable = false, unique = true)
    )
    private PaymentKey paymentKey;

    /**
     * PG 승인 완료 시 발급되는 외부 트랜잭션 식별자.
     * APPROVED 상태에서만 존재한다.
     */
    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "transaction_id")
    )
    private TransactionId transactionId;

    /**
     * 생성자
     * @param orderId       주문 id
     * @param money         결제 금액
     * @param paymentKey    결제 식별 key
     */
    private Payment(Long orderId, Money money, PaymentKey paymentKey) {
        this.orderId = orderId;
        this.money = money;
        this.paymentKey = paymentKey;
        this.status = PaymentStatus.READY;
    }

    /**
     * Payment 생성.
     *
     * @param orderId 주문 ID
     * @param money 결제 금액
     * @param paymentKey 중복 방지 키
     */
    public static Payment create(Long orderId, Money money, PaymentKey paymentKey) {
        return new Payment(orderId, money, paymentKey);
    }

    /** PG 요청 상태로 전이 */
    public void request() {
        status.validateTransition(PaymentStatus.REQUESTED);
        this.status = PaymentStatus.REQUESTED;
    }

    /**
     * 승인 처리.
     *
     * @param transactionId PG에서 발급한 승인 번호
     */
    public void approve(TransactionId transactionId) {

        status.validateTransition(PaymentStatus.APPROVED);

        this.transactionId = transactionId;
        this.status = PaymentStatus.APPROVED;
    }

    /** 승인 실패 처리 */
    public void fail() {
        status.validateTransition(PaymentStatus.FAILED);
        this.status = PaymentStatus.FAILED;
    }

    /** 승인된 결제를 취소 */
    public void cancel() {
        status.validateTransition(PaymentStatus.CANCELED);
        this.status = PaymentStatus.CANCELED;
    }

    public boolean isApproved() {
        return this.status == PaymentStatus.APPROVED;
    }
}