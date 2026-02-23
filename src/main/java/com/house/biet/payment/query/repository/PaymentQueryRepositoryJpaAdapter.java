package com.house.biet.payment.query.repository;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.query.PaymentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentQueryRepositoryJpaAdapter implements PaymentQueryRepository {

    private final PaymentQueryRepositoryJpa paymentQueryRepositoryJpa;

    @Override
    public List<Payment> findAllByStatus(PaymentStatus status) {
        return paymentQueryRepositoryJpa.findAllByStatus(status);
    }

    @Override
    public List<Payment> findAllByOrderId(Long orderId) {
        return paymentQueryRepositoryJpa.findAllByOrderId(orderId);
    }

    @Override
    public List<Payment> findAllByUserId(Long userId) {
        return paymentQueryRepositoryJpa.findAllByUserId(userId);
    }

    @Override
    public List<Payment> findAllByUserIdAndStatusIn(Long userId, List<PaymentStatus> statuses) {
        return paymentQueryRepositoryJpa.findAllByUserIdAndStatusIn(userId, statuses);
    }

    @Override
    public Optional<Payment> findApprovedByOrderId(Long orderId) {
        return paymentQueryRepositoryJpa.findApprovedByOrderId(orderId);
    }

    @Override
    public Optional<Payment> findByPaymentKey(String paymentKey) {
        return paymentQueryRepositoryJpa.findByPaymentKey(paymentKey);
    }
}
