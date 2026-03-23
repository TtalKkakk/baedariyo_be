package com.house.biet.payment.command.application;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.payment.command.PaymentRepository;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.command.repository.PaymentRepositoryJpa;
import com.house.biet.support.config.ServiceConcurrencyTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceConcurrencyTest extends ServiceConcurrencyTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentRepositoryJpa paymentRepositoryJpa;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @DisplayName("성공 - 낙관적락 동시 승인 충돌 제어")
    void approveConcurrency_OptimisticLock() throws InterruptedException {

        // given - 테스트용 Payment 생성
        Payment payment = paymentRepository.save(
                Payment.create(
                        1L,
                        2L,
                        new Money(10000),
                        new PaymentKey("pk-concurrency")
                )
        );
        payment.request();
        paymentRepositoryJpa.saveAndFlush(payment);

        int threadCount = 3;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger nonSuccessCount = new AtomicInteger();

        // TransactionTemplate 사용해서 각 스레드마다 독립 트랜잭션 실행
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    transactionTemplate.execute(status -> {
                        try {
                            paymentService.approve(
                                    payment.getId(),
                                    UUID.randomUUID().toString()
                            );
                            successCount.incrementAndGet();
                        } catch (CustomException e) {
                            if (e.getErrorCode() == ErrorCode.PAYMENT_CONCURRENCY_CONFLICT
                                    || e.getErrorCode() == ErrorCode.ALREADY_APPROVED_PAYMENT) {
                                nonSuccessCount.incrementAndGet();
                            }
                        } catch (Exception e) {
                            nonSuccessCount.incrementAndGet();
                        }
                        return null;
                    });
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then - 낙관적 락으로 인해 한 스레드만 성공
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(nonSuccessCount.get()).isEqualTo(threadCount - 1);
    }
}
