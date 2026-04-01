package com.house.biet.payment.command.repository;

import com.house.biet.payment.command.domain.aggregate.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepositoryJpa
        extends JpaRepository<Payment, Long> {

    /**
     * 결제 키을 조회한다
     *
     * @param paymentKey 결제 키
     * @return 조회 결과
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentKey.value = :paymentKey")
    Optional<Payment> findByPaymentKey(String paymentKey);

    /**
     * 결제 키을 처리한다
     *
     * @param paymentKey 결제 키
     * @return existsByPaymentKey 결과
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Payment p WHERE p.paymentKey.value = :paymentKey")
    boolean existsByPaymentKey(String paymentKey);
}
