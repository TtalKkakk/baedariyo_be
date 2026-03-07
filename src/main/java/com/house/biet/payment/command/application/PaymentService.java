package com.house.biet.payment.command.application;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.payment.command.PaymentRepository;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.command.domain.vo.TransactionId;
import com.house.biet.payment.command.repository.PaymentRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Payment Command Application Service.
 *
 * <p>
 * 결제 도메인의 명령(Command) 작업을 처리하는 애플리케이션 계층이다.
 * </p>
 *
 * <h3>역할</h3>
 * <ul>
 *     <li>결제 생성</li>
 *     <li>결제 상태 전이 처리</li>
 *     <li>트랜잭션 경계 관리</li>
 *     <li>중복 결제 방지 1차 검증</li>
 * </ul>
 *
 * <h3>설계 원칙</h3>
 * <ul>
 *     <li>비즈니스 로직은 Aggregate(Payment)에 위임한다.</li>
 *     <li>상태 전이 규칙은 Payment 내부에서 검증한다.</li>
 *     <li>본 서비스는 흐름 제어와 저장 책임만 가진다.</li>
 * </ul>
 *
 * <h3>동시성 고려</h3>
 * <p>
 * paymentKey 중복은 1차적으로 existsByPaymentKey로 검증하지만,
 * 최종적으로는 DB unique 제약 조건을 통해 보장된다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 새로운 결제를 생성한다.
     *
     * <p>
     * 동일한 paymentKey가 존재할 경우 중복 결제로 간주하고 예외를 발생시킨다.
     * 생성된 결제의 초기 상태는 {@code READY}이다.
     * </p>
     *
     * @param orderId 결제가 발생한 주문 ID
     * @param userId 사용자 ID
     * @param money 결제 금액
     * @param paymentKeyValue 중복 방지를 위한 결제 식별 키
     * @return 생성된 Payment의 식별자(ID)
     * @throws CustomException DUPLICATE_PAYMENT 예외 (중복 결제 시)
     */
    public Long createPayment(Long orderId, Long userId, Money money, String paymentKeyValue) {
        if (paymentRepository.existsByPaymentKey(paymentKeyValue)) {
            throw new CustomException(ErrorCode.DUPLICATE_PAYMENT);
        }

        Payment payment = Payment.create(
                orderId,
                userId,
                money,
                new PaymentKey(paymentKeyValue)
        );

        return paymentRepository.save(payment).getId();
    }

    /**
     * 결제를 PG 요청 상태로 전이한다.
     *
     * <p>
     * READY 상태에서만 REQUESTED 상태로 전이할 수 있다.
     * 상태 전이 검증은 Aggregate 내부에서 수행된다.
     * </p>
     *
     * @param paymentId 결제 식별자
     * @throws CustomException PAYMENT_NOT_FOUND 예외 (결제 미존재 시)
     */
    public void request(Long paymentId) {
        Payment payment = getPayment(paymentId);
        payment.request();
    }

    /**
     * PG 승인 완료 처리.
     *
     * <p>
     * REQUESTED 상태에서만 APPROVED 상태로 전이 가능하다.
     * 승인 시 외부 PG에서 발급한 transactionId를 저장한다.
     * </p>
     *
     * @param paymentId 결제 식별자
     * @param transactionIdValue PG 승인 트랜잭션 ID
     * @throws CustomException PAYMENT_NOT_FOUND 예외
     */
    public void approve(Long paymentId, String transactionIdValue) {
        try {
            Payment payment = getPayment(paymentId);
            if (payment.isApproved())
                throw new CustomException(ErrorCode.ALREADY_APPROVED_PAYMENT);

            payment.approve(new TransactionId(transactionIdValue));
            paymentRepository.saveAndFlush(payment);
            publishEvents(payment);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomException(ErrorCode.PAYMENT_CONCURRENCY_CONFLICT);
        }
    }

    /**
     * 결제 승인 실패 처리.
     *
     * <p>
     * REQUESTED 상태에서 FAILED 상태로 전이한다.
     * </p>
     *
     * @param paymentId 결제 식별자
     */
    public void fail(Long paymentId) {
        Payment payment = getPayment(paymentId);
        payment.fail();
    }

    /**
     * 승인된 결제를 취소한다.
     *
     * <p>
     * APPROVED 상태에서만 CANCELED 상태로 전이 가능하다.
     * </p>
     *
     * @param paymentId 결제 식별자
     */
    public void cancel(Long paymentId) {
        Payment payment = getPayment(paymentId);
        payment.cancel();
    }

    /**
     * 결제를 조회한다.
     *
     * @param paymentId 결제 식별자
     * @return Payment Aggregate
     * @throws CustomException PAYMENT_NOT_FOUND 예외
     */
    private Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    private void publishEvents(Payment payment) {
        payment.getDomainEvents().forEach(eventPublisher::publishEvent);
        payment.clearDomainEvents();
    }
}