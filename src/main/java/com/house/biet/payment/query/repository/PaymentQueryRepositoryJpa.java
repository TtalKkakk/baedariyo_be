package com.house.biet.payment.query.repository;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.query.application.dto.PaymentDetailResponseDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryRepositoryJpa extends Repository<Payment, Long> {

    @Query("""
            select new com.house.biet.payment.query.application.dto.PaymentDetailResponseDto(
                p.id,
                p.orderId,
                p.userId,
                p.money.amount,
                p.status,
                p.paymentKey.value,
                p.createdAt
            )
            from Payment p
            where p.status = :status
            """)
    List<PaymentDetailResponseDto> findAllByStatus(PaymentStatus status);

    @Query("""
            select new com.house.biet.payment.query.application.dto.PaymentDetailResponseDto(
                p.id,
                p.orderId,
                p.userId,
                p.money.amount,
                p.status,
                p.paymentKey.value,
                p.createdAt
            )
            from Payment p
            where p.id = :paymentId
            """)
    Optional<PaymentDetailResponseDto> findDetailById(Long paymentId);

    @Query("""
            select new com.house.biet.payment.query.application.dto.PaymentDetailResponseDto(
                p.id,
                p.orderId,
                p.userId,
                p.money.amount,
                p.status,
                p.paymentKey.value,
                p.createdAt
            )
            from Payment p
            where p.orderId = :orderId
            """)
    List<PaymentDetailResponseDto> findAllByOrderId(Long orderId);

    @Query("""
            select new com.house.biet.payment.query.application.dto.PaymentDetailResponseDto(
                p.id,
                p.orderId,
                p.userId,
                p.money.amount,
                p.status,
                p.paymentKey.value,
                p.createdAt
            )
            from Payment p
            where p.userId = :userId
            """)
    List<PaymentDetailResponseDto> findAllByUserId(Long userId);

    @Query("""
            select new com.house.biet.payment.query.application.dto.PaymentDetailResponseDto(
                p.id,
                p.orderId,
                p.userId,
                p.money.amount,
                p.status,
                p.paymentKey.value,
                p.createdAt
            )
            from Payment p
            where p.userId = :userId
              and p.status in :statuses
            """)
    List<PaymentDetailResponseDto> findAllByUserIdAndStatusIn(Long userId, List<PaymentStatus> statuses);

    @Query("""
            select new com.house.biet.payment.query.application.dto.PaymentDetailResponseDto(
                p.id,
                p.orderId,
                p.userId,
                p.money.amount,
                p.status,
                p.paymentKey.value,
                p.createdAt
            )
            from Payment p
            where p.orderId = :orderId
              and p.status = 'APPROVED'
            """)
    Optional<PaymentDetailResponseDto> findApprovedByOrderId(Long orderId);

    @Query("""
            select new com.house.biet.payment.query.application.dto.PaymentDetailResponseDto(
                p.id,
                p.orderId,
                p.userId,
                p.money.amount,
                p.status,
                p.paymentKey.value,
                p.createdAt
            )
            from Payment p
            where p.paymentKey.value = :paymentKey
            """)
    Optional<PaymentDetailResponseDto> findByPaymentKey(String paymentKey);
}
