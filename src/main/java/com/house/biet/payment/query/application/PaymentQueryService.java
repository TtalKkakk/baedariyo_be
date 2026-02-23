package com.house.biet.payment.query.application;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.command.domain.aggregate.Payment;

import java.util.List;
import java.util.Optional;

/**
 * Payment Query Application Service.
 *
 * <p>
 * 결제 도메인의 조회(Query) 전용 서비스 계층이다.
 * </p>
 *
 * <h3>역할</h3>
 * <ul>
 *     <li>읽기 전용 데이터 조회</li>
 *     <li>조회 트랜잭션 관리 (readOnly)</li>
 *     <li>화면 표시용 데이터 제공</li>
 * </ul>
 *
 * <h3>설계 원칙</h3>
 * <ul>
 *     <li>상태 변경 로직은 포함하지 않는다.</li>
 *     <li>읽기 성능 최적화를 위해 Projection 또는 Querydsl 확장이 가능하다.</li>
 *     <li>Entity 직접 반환은 단순 구조에서만 허용한다.</li>
 * </ul>
 */
public interface PaymentQueryService {

    /**
     * 특정 상태의 결제 목록을 조회한다.
     *
     * @param status 조회할 결제 상태
     * @return 해당 상태의 결제 목록
     */
    List<Payment> findByStatus(PaymentStatus status);

    /**
     * 특정 주문에 대한 결제 이력을 조회한다.
     *
     * <p>
     * 1:N 구조에서 하나의 주문에 여러 결제가 존재할 수 있다.
     * </p>
     *
     * @param orderId 주문 ID
     * @return 해당 주문의 결제 목록
     */
    List<Payment> findByOrderId(Long orderId);

    /**
     * 특정 사용자의 전체 결제 이력을 조회한다.
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 결제 목록
     */
    List<Payment> findByUserId(Long userId);

    /**
     * 특정 사용자의 특정 상태 결제 목록을 조회한다.
     *
     * @param userId 사용자 ID
     * @param statuses 조회할 결제 상태 목록
     * @return 조건에 해당하는 결제 목록
     */
    List<Payment> findByUserIdAndStatuses(
            Long userId,
            List<PaymentStatus> statuses
    );

    /**
     * 특정 주문의 승인된 결제를 조회한다.
     *
     * @param orderId 주문 ID
     * @return 승인된 결제 (없을 경우 Optional.empty)
     */
    Optional<Payment> findApprovedByOrderId(Long orderId);

    /**
     * paymentKey를 기준으로 결제를 조회한다.
     *
     * @param paymentKey 결제 식별 키
     * @return 해당 paymentKey의 결제 (없을 경우 Optional.empty)
     */
    Optional<Payment> findByPaymentKey(String paymentKey);
}