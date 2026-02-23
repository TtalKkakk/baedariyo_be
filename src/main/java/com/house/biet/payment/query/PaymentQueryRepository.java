package com.house.biet.payment.query;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.command.domain.aggregate.Payment;

import java.util.List;
import java.util.Optional;

/**
 * Payment 조회 전용 Repository.
 *
 * <p>
 * 화면 표시 및 조회 최적화를 위한 읽기 모델 접근 계층이다.
 * 상태 변경과 같은 명령 작업은 포함하지 않는다.
 * </p>
 */
public interface PaymentQueryRepository {

    /**
     * 특정 상태의 결제 목록을 조회한다.
     *
     * @param status 결제 상태
     * @return 해당 상태의 Payment 목록
     */
    List<Payment> findAllByStatus(PaymentStatus status);

    /**
     * 특정 주문의 결제 이력을 조회한다.
     *
     * @param orderId 주문 ID
     * @return 해당 주문에 대한 Payment 목록
     */
    List<Payment> findAllByOrderId(Long orderId);

    /**
     * 특정 사용자의 전체 결제 이력을 조회한다.
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 Payment 목록
     */
    List<Payment> findAllByUserId(Long userId);

    /**
     * 특정 사용자의 특정 상태 결제 목록을 조회한다.
     *
     * @param userId 사용자 ID
     * @param statuses 조회할 결제 상태 목록
     * @return 조건에 해당하는 Payment 목록
     */
    List<Payment> findAllByUserIdAndStatusIn(
            Long userId,
            List<PaymentStatus> statuses
    );

    /**
     * 특정 주문의 승인된 결제를 조회한다.
     *
     * <p>
     * 주문당 승인된 결제는 하나만 존재해야 한다는
     * 도메인 규칙을 전제로 한다.
     * </p>
     *
     * @param orderId 주문 ID
     * @return 승인된 Payment (없을 경우 Optional.empty)
     */
    Optional<Payment> findApprovedByOrderId(Long orderId);

    /**
     * paymentKey를 기준으로 결제를 조회한다.
     *
     * <p>
     * 중복 결제 방지 및 멱등성 보장을 위해 사용된다.
     * </p>
     *
     * @param paymentKey 결제 식별 키
     * @return 해당 paymentKey의 Payment (없을 경우 Optional.empty)
     */
    Optional<Payment> findByPaymentKey(String paymentKey);
}