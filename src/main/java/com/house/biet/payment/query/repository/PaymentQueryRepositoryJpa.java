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
    /**
     * 결제 상세 목록을 조회한다
     *
     * @param status 상태
     * @return 조회 결과 목록
     */
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
    /**
     * 결제 상세 정보를 조회한다
     *
     * @param paymentId 결제 식별자
     * @return 조회 결과
     */
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
    /**
     * 결제 상세 목록을 조회한다
     *
     * @param orderId 주문 식별자
     * @return 조회 결과 목록
     */
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
    /**
     * 결제 상세 목록을 조회한다
     *
     * @param userId 사용자 식별자
     * @return 조회 결과 목록
     */
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
    /**
     * 결제 상세 목록을 조회한다
     *
     * @param userId 사용자 식별자
     * @param statuses 상태 목록
     * @return 조회 결과 목록
     */
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
    /**
     * 결제 상세 정보를 조회한다
     *
     * @param orderId 주문 식별자
     * @return 조회 결과
     */
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
    /**
     * 결제 상세 정보를 조회한다
     *
     * @param paymentKey 결제 키
     * @return 조회 결과
     */
    Optional<PaymentDetailResponseDto> findByPaymentKey(String paymentKey);
}
