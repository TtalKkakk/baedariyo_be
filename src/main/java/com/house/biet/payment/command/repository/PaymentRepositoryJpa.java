package com.house.biet.payment.command.repository;

import com.house.biet.payment.command.domain.aggregate.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepositoryJpa
        extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.paymentKey.value = :paymentKey")
    Optional<Payment> findByPaymentKey(String paymentKey);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Payment p WHERE p.paymentKey.value = :paymentKey")
    boolean existsByPaymentKey(String paymentKey);
}
