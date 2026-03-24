package com.house.biet.payment.query.repository;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.query.PaymentQueryRepository;
import com.house.biet.payment.query.application.dto.MyPaymentDetailResponseDto;
import com.house.biet.payment.query.application.dto.MyPaymentSearchCondition;
import com.house.biet.payment.query.application.dto.PaymentDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentQueryRepositoryJpaAdapter implements PaymentQueryRepository {

    private final PaymentQueryRepositoryJpa paymentQueryRepositoryJpa;
    private final PaymentQueryRepositoryQuerydsl paymentQueryRepositoryQuerydsl;

    @Override
    public List<PaymentDetailResponseDto> findAllByStatus(PaymentStatus status) {
        return paymentQueryRepositoryJpa.findAllByStatus(status);
    }

    @Override
    public Optional<PaymentDetailResponseDto> findById(Long paymentId) {
        return paymentQueryRepositoryJpa.findDetailById(paymentId);
    }

    @Override
    public List<PaymentDetailResponseDto> findAllByOrderId(Long orderId) {
        return paymentQueryRepositoryJpa.findAllByOrderId(orderId);
    }

    @Override
    public List<PaymentDetailResponseDto> findAllByUserId(Long userId) {
        return paymentQueryRepositoryJpa.findAllByUserId(userId);
    }

    @Override
    public List<PaymentDetailResponseDto> findAllByUserIdAndStatusIn(Long userId, List<PaymentStatus> statuses) {
        return paymentQueryRepositoryJpa.findAllByUserIdAndStatusIn(userId, statuses);
    }

    @Override
    public Optional<PaymentDetailResponseDto> findApprovedByOrderId(Long orderId) {
        return paymentQueryRepositoryJpa.findApprovedByOrderId(orderId);
    }

    @Override
    public Optional<PaymentDetailResponseDto> findByPaymentKey(String paymentKey) {
        return paymentQueryRepositoryJpa.findByPaymentKey(paymentKey);
    }

    @Override
    public List<MyPaymentDetailResponseDto> findMyPaymentDetailList(MyPaymentSearchCondition myPaymentSearchCondition) {
        return paymentQueryRepositoryQuerydsl.findMyPaymentDetailList(myPaymentSearchCondition);
    }
}
