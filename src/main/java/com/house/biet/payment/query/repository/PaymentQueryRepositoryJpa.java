package com.house.biet.payment.query.repository;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.command.domain.aggregate.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Payment 조회용 JPA Repository.
 *
 * <p>
 * Spring Data JPA 기반 읽기 전용 구현체이다.
 * 화면 표시 및 조회 목적의 데이터 접근을 담당한다.
 * </p>
 */
public interface PaymentQueryRepositoryJpa
        extends JpaRepository<Payment, Long> {

    /**
     * 특정 상태의 모든 결제를 조회한다.
     *
     * @param status 결제 상태
     * @return 해당 상태의 결제 목록
     */
    @Query("SELECT p FROM Payment p WHERE p.status = :status")
    List<Payment> findAllByStatus(PaymentStatus status);

    /**
     * 특정 주문에 대한 모든 결제를 조회한다.
     *
     * @param orderId 주문 ID
     * @return 해당 주문의 결제 목록
     */
    @Query("SELECT p FROM Payment p WHERE p.orderId = :orderId")
    List<Payment> findAllByOrderId(Long orderId);

    /**
     * 특정 사용자의 전체 결제 이력을 조회한다.
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 결제 목록
     */
    List<Payment> findAllByUserId(Long userId);

    /**
     * 특정 사용자의 특정 상태 결제 목록을 조회한다.
     *
     * @param userId 사용자 ID
     * @param statuses 조회할 결제 상태 목록
     * @return 조건에 해당하는 결제 목록
     */
    List<Payment> findAllByUserIdAndStatusIn(Long userId, List<PaymentStatus> statuses);

    /**
     * 특정 주문의 승인된 결제를 조회한다.
     *
     * <p>
     * 주문당 승인된 결제는 하나만 존재해야 한다는
     * 도메인 규칙을 전제로 한다.
     * </p>
     *
     * @param orderId 주문 ID
     * @return 승인된 결제 (없을 경우 Optional.empty)
     */
    @Query("SELECT p FROM Payment p WHERE p.orderId = :orderId AND p.status = 'APPROVED'")
    Optional<Payment> findApprovedByOrderId(Long orderId);

    /**
     * paymentKey를 기준으로 결제를 조회한다.
     *
     * @param paymentKey 결제 식별 키
     * @return 해당 paymentKey의 결제 (없을 경우 Optional.empty)
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentKey.value = :paymentKey")
    Optional<Payment> findByPaymentKey(String paymentKey);
}