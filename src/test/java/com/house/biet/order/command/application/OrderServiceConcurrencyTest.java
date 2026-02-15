package com.house.biet.order.command.application;

import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.application.OrderService;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.vo.OrderStatus;
import com.house.biet.support.config.ServiceConcurrencyTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceConcurrencyTest extends ServiceConcurrencyTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private Long orderId;

    @BeforeEach
    void setUp() {
        Order order = OrderFixtures.order(1L);
        orderRepository.save(order);
        orderId = order.getId();
    }

    static final int threadCount = 3;

    @Test
    void OrderConcurrencyCancel_Success() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    orderService.cancelOrder(orderId);
                    success.incrementAndGet();
                } catch (CustomException e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Order order = orderService.getOrder(orderId);

        assertThat(success.get()).isEqualTo(1);
        assertThat(fail.get()).isEqualTo(threadCount-1);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }
}