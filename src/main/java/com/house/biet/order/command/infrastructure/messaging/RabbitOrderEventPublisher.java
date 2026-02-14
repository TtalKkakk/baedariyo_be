package com.house.biet.order.command.infrastructure.messaging;

import com.house.biet.order.command.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitOrderEventPublisher implements OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {
        String routingKey = "order.created." + event.getPickupLocationDto().getRegion();

        rabbitTemplate.convertAndSend(
                "order.exchange",
                routingKey,
                event
        );
    }
}
