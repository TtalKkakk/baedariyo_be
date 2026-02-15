package com.house.biet.order.command.infrastructure.messaging.rider;

import com.house.biet.order.command.application.port.RiderCandidate;
import com.house.biet.order.command.application.port.RiderFinder;
import com.house.biet.order.command.domain.event.OrderCreatedEvent;
import com.house.biet.support.config.RabbitTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("rabbit")
@Testcontainers
@Import(RabbitTestConfig.class)
class RabbitRiderCallSenderTest {

    @Container
    static RabbitMQContainer rabbit =
            new RabbitMQContainer("rabbitmq:3.13-management");

    @DynamicPropertySource
    static void rabbitProps(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.username", () -> "guest");
        registry.add("spring.rabbitmq.password", () -> "guest");
        registry.add("spring.rabbitmq.host", rabbit::getHost);
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
    }

    @Autowired
    RiderCallSender riderCallSender;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @MockitoBean
    RiderFinder riderFinder;

    @Test
    @DisplayName("성공 - 라이더 호출 메시지 발행")
    void send_Success() {
        // given
        RiderCandidate candidate = new RiderCandidate(1L, 0.3);

        OrderCreatedEvent event =
                new OrderCreatedEvent(
                        10L,
                        /* pickup */ null,
                        /* drop */ null,
                        LocalDateTime.now()
                );

        // when
        riderCallSender.send(candidate, event);

        // then
        Object message = rabbitTemplate.receiveAndConvert("rider.call.queue");
        assertThat(message).isNotNull();
    }
}