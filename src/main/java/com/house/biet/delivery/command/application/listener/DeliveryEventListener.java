package com.house.biet.delivery.command.application.listener;

import com.house.biet.delivery.command.domain.event.DeliveryCompletedEvent;
import com.house.biet.order.command.application.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DeliveryEventListener {

    private final OrderService orderService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(DeliveryCompletedEvent event) {
        orderService.markDelivered(event.orderId());

        // TODO: 추후 비즈니스 반응 로직 추가

    }
}
