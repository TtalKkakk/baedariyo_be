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
}
