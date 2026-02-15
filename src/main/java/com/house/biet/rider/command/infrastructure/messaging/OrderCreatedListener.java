package com.house.biet.rider.command.infrastructure.messaging;

import com.house.biet.order.command.domain.event.OrderCreatedEvent;
import com.house.biet.order.command.application.RiderNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final RiderNotificationService riderNotificationService;

    @RabbitListener(queues = "rider.notification.queue")
    public void onOrderCreated(OrderCreatedEvent event) {
        riderNotificationService.notifyNearByRiders(event);
    }
}
