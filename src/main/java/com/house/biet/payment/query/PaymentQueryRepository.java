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
     * 특정 상태의 결제 목록 조회
     *
     * @param status 결제 상태
     * @return Payment 목록
     */
    List<Payment> findAllByStatus(PaymentStatus status);

    /**
     * 특정 주문의 결제 이력 조회
     *
     * @param orderId 주문 ID
     * @return Payment 목록
     */
    List<Payment> findAllByOrderId(Long orderId);
}