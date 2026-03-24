package com.house.biet.payment.query;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.query.application.dto.MyPaymentDetailResponseDto;
import com.house.biet.payment.query.application.dto.MyPaymentSearchCondition;
import com.house.biet.payment.query.application.dto.PaymentDetailResponseDto;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryRepository {

    List<PaymentDetailResponseDto> findAllByStatus(PaymentStatus status);

    Optional<PaymentDetailResponseDto> findById(Long paymentId);

    List<PaymentDetailResponseDto> findAllByOrderId(Long orderId);

    List<PaymentDetailResponseDto> findAllByUserId(Long userId);

    List<PaymentDetailResponseDto> findAllByUserIdAndStatusIn(Long userId, List<PaymentStatus> statuses);

    Optional<PaymentDetailResponseDto> findApprovedByOrderId(Long orderId);

    Optional<PaymentDetailResponseDto> findByPaymentKey(String paymentKey);

    List<MyPaymentDetailResponseDto> findMyPaymentDetailList(MyPaymentSearchCondition myPaymentSearchCondition);
}
