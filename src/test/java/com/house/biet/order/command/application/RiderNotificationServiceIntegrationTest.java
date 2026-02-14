package com.house.biet.order.command.application;

import com.house.biet.order.command.application.port.RiderCandidate;
import com.house.biet.order.command.domain.event.OrderCreatedEvent;
import com.house.biet.order.command.infrastructure.messaging.rider.RiderCallSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("redis")
@Testcontainers
class RiderNotificationServiceIntegrationTest {

    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>("redis:7.2")
                    .withExposedPorts(6379)
                    .waitingFor(
                            Wait.forListeningPort()
                    );


    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port",
                () -> redis.getMappedPort(6379));
    }

    @Autowired
    RiderNotificationService riderNotificationService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @MockitoBean
    RiderCallSender riderCallSender; // 🔥 핵심

    @Test
    @DisplayName("성공 - 주문생성시 주변 라이더들에게 알림을 보낸다.")
    void notifyRiders_Success() {
        // given
        redisTemplate.opsForGeo().add(
                "rider:location",
                new Point(127.0, 37.5),
                "1"
        );
        redisTemplate.opsForGeo().add(
                "rider:location",
                new Point(127.001, 37.501),
                "2"
        );

        OrderCreatedEvent event = new OrderCreatedEvent(
                null,
                null,
                new OrderCreatedEvent.PickupLocationDto(127.0, 37.5, "mapo"),
                LocalDateTime.now()
        );

        // when
        riderNotificationService.notifyRiders(event);

        // then
        verify(riderCallSender, times(2))
                .send(any(RiderCandidate.class), eq(event));
    }
}
