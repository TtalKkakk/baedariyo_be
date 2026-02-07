package com.house.biet.order.command.domain.application;

import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.vo.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceConcurrencyTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private Long orderId;

    @BeforeEach
    void setUp() {
        Order order = OrderFixtures.order(1L, 2L);
        orderRepository.save(order);
        orderId = order.getId();
    }

    @Test
    void OrderConcurrencyCancel_Success() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        executor.submit(() -> {
            try {
                orderService.cancelOrder(orderId);
            } finally {
                latch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                orderService.cancelOrder(orderId);
            } finally {
                latch.countDown();
            }
        });

        latch.await();

        Order order = orderService.getOrder(orderId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }
}