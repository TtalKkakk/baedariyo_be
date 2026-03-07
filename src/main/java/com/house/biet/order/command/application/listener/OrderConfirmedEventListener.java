package com.house.biet.order.command.application.listener;

import com.house.biet.delivery.command.application.DeliveryService;
import com.house.biet.order.command.domain.event.OrderConfirmedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderConfirmedEventListener {

    private final DeliveryService deliveryService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderConfirmedEvent event) {
        // TODO: Order 성공 시 이벤트 로직 추가
    }
}
