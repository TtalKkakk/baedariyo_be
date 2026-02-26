package com.house.biet.payment.query.application;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.query.PaymentQueryRepository;
import com.house.biet.payment.query.application.dto.MyPaymentDetailResponseDto;
import com.house.biet.payment.query.application.dto.MyPaymentSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final PaymentQueryRepository paymentQueryRepository;

    @Override
    public List<Payment> findByStatus(PaymentStatus status) {
        return paymentQueryRepository.findAllByStatus(status);
    }

    @Override
    public Optional<Payment> findById(Long paymentId) {
        return paymentQueryRepository.findById(paymentId);
    }

    @Override
    public List<Payment> findByOrderId(Long orderId) {
        return paymentQueryRepository.findAllByOrderId(orderId);
    }

    @Override
    public List<Payment> findByUserId(Long userId) {
        return paymentQueryRepository.findAllByUserId(userId);
    }

    @Override
    public List<Payment> findByUserIdAndStatuses(
            Long userId,
            List<PaymentStatus> statuses
    ) {
        return paymentQueryRepository
                .findAllByUserIdAndStatusIn(userId, statuses);
    }

    @Override
    public Optional<Payment> findApprovedByOrderId(Long orderId) {
        return paymentQueryRepository
                .findApprovedByOrderId(orderId);
    }

    @Override
    public Optional<Payment> findByPaymentKey(String paymentKey) {
        return paymentQueryRepository
                .findByPaymentKey(paymentKey);
    }

    @Override
    public List<MyPaymentDetailResponseDto> findMyPaymentDetailList(MyPaymentSearchCondition myPaymentSearchCondition) {
        return paymentQueryRepository
                .findMyPaymentDetailList(myPaymentSearchCondition);
    }
}