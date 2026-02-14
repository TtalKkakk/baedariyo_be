package com.house.biet.order.command.infrastructure.messaging.rider;

import com.house.biet.order.command.application.port.RiderCandidate;
import com.house.biet.order.command.domain.event.OrderCreatedEvent;

public interface RiderCallSender {

    void send(RiderCandidate candidate, OrderCreatedEvent event);
}
