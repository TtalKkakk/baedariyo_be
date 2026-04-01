package com.house.biet.payment.command;

import com.house.biet.payment.command.domain.aggregate.Payment;

import java.util.Optional;

/**
 * Payment Aggregate에 대한 Command 전용 Repository.
 *
 * <p>
 * 이 Repository는 Aggregate Root의 생명주기 관리 책임을 가진다.
 * 상태 변경, 생성, 저장과 같은 명령(Command) 작업을 지원한다.
 * </p>
 *
 * <p>
 * 조회 목적의 복잡한 읽기 모델은 포함하지 않으며,
 * 필요 시 Query 전용 Repository로 분리한다.
 * </p>
 */
public interface PaymentRepository {

    /**
     * Payment Aggregate를 저장한다.
     *
     * @param payment 저장할 Payment Aggregate
     * @return 저장된 Payment (식별자 포함)
     */
    Payment save(Payment payment);

    /**
     * Payment Aggregate를 저장하고 즉시 DB와 동기화한다.
     *
     * @param payment 저장할 Payment Aggregate
     * @return 저장된 Payment (식별자 포함)
     */
    Payment saveAndFlush(Payment payment);

    /**
     * 식별자로 Payment를 조회한다.
     *
     * @param id Payment ID
     * @return Optional Payment
     */
    Optional<Payment> findById(Long id);

    /**
     * 중복 결제 방지를 위한 paymentKey 기준 조회.
     *
     * @param paymentKey 결제 식별 키
     * @return Optional Payment
     */
    Optional<Payment> findByPaymentKey(String paymentKey);

    /**
     * 해당 paymentKey가 이미 존재하는지 여부 확인.
     *
     * @param paymentKey 결제 식별 키
     * @return 존재 여부
     */
    boolean existsByPaymentKey(String paymentKey);
}