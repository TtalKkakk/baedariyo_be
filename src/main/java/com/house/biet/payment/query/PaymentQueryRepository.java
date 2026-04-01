package com.house.biet.payment.query;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.query.application.dto.MyPaymentDetailResponseDto;
import com.house.biet.payment.query.application.dto.MyPaymentSearchCondition;
import com.house.biet.payment.query.application.dto.PaymentDetailResponseDto;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryRepository {

    /**
     * 결제 상세 목록을 조회한다
     *
     * @param status 상태
     * @return 조회 결과 목록
     */
    List<PaymentDetailResponseDto> findAllByStatus(PaymentStatus status);

    /**
     * 결제 상세 정보를 조회한다
     *
     * @param paymentId 결제 식별자
     * @return 조회 결과
     */
    Optional<PaymentDetailResponseDto> findById(Long paymentId);

    /**
     * 결제 상세 목록을 조회한다
     *
     * @param orderId 주문 식별자
     * @return 조회 결과 목록
     */
    List<PaymentDetailResponseDto> findAllByOrderId(Long orderId);

    /**
     * 결제 상세 목록을 조회한다
     *
     * @param userId 사용자 식별자
     * @return 조회 결과 목록
     */
    List<PaymentDetailResponseDto> findAllByUserId(Long userId);

    /**
     * 결제 상세 목록을 조회한다
     *
     * @param userId 사용자 식별자
     * @param statuses 상태 목록
     * @return 조회 결과 목록
     */
    List<PaymentDetailResponseDto> findAllByUserIdAndStatusIn(Long userId, List<PaymentStatus> statuses);

    /**
     * 결제 상세 정보를 조회한다
     *
     * @param orderId 주문 식별자
     * @return 조회 결과
     */
    Optional<PaymentDetailResponseDto> findApprovedByOrderId(Long orderId);

    /**
     * 결제 상세 정보를 조회한다
     *
     * @param paymentKey 결제 키
     * @return 조회 결과
     */
    Optional<PaymentDetailResponseDto> findByPaymentKey(String paymentKey);

    /**
     * 내 결제 상세 목록을 조회한다
     *
     * @param myPaymentSearchCondition 결제 조회 조건
     * @return 조회 결과 목록
     */
    List<MyPaymentDetailResponseDto> findMyPaymentDetailList(MyPaymentSearchCondition myPaymentSearchCondition);
}
