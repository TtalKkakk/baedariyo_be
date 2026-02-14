package com.house.biet.order.command.infrastructure.messaging.rider;

import com.house.biet.order.command.application.port.RiderCandidate;
import com.house.biet.order.command.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("rabbit")
@RequiredArgsConstructor
public class RabbitRiderCallSender implements RiderCallSender {

    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(RiderCandidate candidate, OrderCreatedEvent event) {
        RiderCallMessage message =
                new RiderCallMessage(event.getOrderId(), candidate.getRiderId());

        rabbitTemplate.convertAndSend(
                "rider.call.exchange",
                "rider.call",
                message);
    }
}
