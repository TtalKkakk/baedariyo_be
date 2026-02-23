package com.house.biet.payment.query.repository;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.command.domain.aggregate.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryRepositoryJpa
        extends JpaRepository<Payment, Long> {

    /**
     * 특정 상태의 모든 결제를 조회한다.
     *
     * @param status 결제 상태
     * @return 해당 상태의 결제 목록
     */
    @Query("SELECT p FROM Payment p WHERE p.status = :status")
    List<Payment> findAllByStatus(PaymentStatus status);

    /**
     * 특정 주문에 대한 모든 결제를 조회한다.
     *
     * @param orderId 주문 ID
     * @return 해당 주문의 결제 목록
     */
    @Query("SELECT p FROM Payment p WHERE p.orderId = :orderId")
    List<Payment> findAllByOrderId(Long orderId);
}
