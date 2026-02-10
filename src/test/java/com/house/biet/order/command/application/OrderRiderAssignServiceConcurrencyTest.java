package com.house.biet.order.command.application;

import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.application.OrderRiderAssignService;
import com.house.biet.order.command.domain.aggregate.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderRiderAssignServiceConcurrencyTest {

    @Autowired
    private OrderRiderAssignService orderRiderAssignService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("여러 라이더가 동시에 주문을 수락하면 한 명만 성공한다")
    void assignRider_concurrent_only_one_success() throws Exception {
        // given
        Order order = OrderFixtures.order(1L);
        order.markPaid();  // 주문 결제
        orderRepository.save(order);

        Long orderId = order.getId();

        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (long riderId = 1L; riderId <= threadCount; riderId++) {
            long currentRiderId = riderId;

            executorService.submit(() -> {
                try {
                    orderRiderAssignService.assignRider(orderId, currentRiderId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    // e.printStackTrace();
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        Order result = orderRepository.findById(orderId).orElseThrow();

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(threadCount-1);
        assertThat(result.getRiderId()).isNotNull();
    }
}