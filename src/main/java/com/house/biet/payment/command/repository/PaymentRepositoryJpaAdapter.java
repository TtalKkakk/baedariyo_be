package com.house.biet.payment.command.repository;

import com.house.biet.payment.command.PaymentRepository;
import com.house.biet.payment.command.domain.aggregate.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryJpaAdapter implements PaymentRepository {

    private final PaymentRepositoryJpa paymentRepositoryJpa;

    @Override
    public Payment save(Payment payment) {
        return paymentRepositoryJpa.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepositoryJpa.findById(id);
    }

    @Override
    public Optional<Payment> findByPaymentKey(String paymentKey) {
        return paymentRepositoryJpa.findByPaymentKey(paymentKey);
    }

    @Override
    public boolean existsByPaymentKey(String paymentKey) {
        return paymentRepositoryJpa.existsByPaymentKey(paymentKey);
    }
}
