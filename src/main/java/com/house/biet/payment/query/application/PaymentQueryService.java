package com.house.biet.payment.query.application;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.query.application.dto.MyPaymentDetailResponseDto;
import com.house.biet.payment.query.application.dto.MyPaymentSearchCondition;
import com.house.biet.payment.query.application.dto.PaymentDetailResponseDto;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {

    List<PaymentDetailResponseDto> findByStatus(PaymentStatus status);

    Optional<PaymentDetailResponseDto> findById(Long paymentId);

    List<PaymentDetailResponseDto> findByOrderId(Long orderId);

    List<PaymentDetailResponseDto> findByUserId(Long userId);

    List<PaymentDetailResponseDto> findByUserIdAndStatuses(Long userId, List<PaymentStatus> statuses);

    Optional<PaymentDetailResponseDto> findApprovedByOrderId(Long orderId);

    Optional<PaymentDetailResponseDto> findByPaymentKey(String paymentKey);

    List<MyPaymentDetailResponseDto> findMyPaymentDetailList(MyPaymentSearchCondition myPaymentSearchCondition);
}
