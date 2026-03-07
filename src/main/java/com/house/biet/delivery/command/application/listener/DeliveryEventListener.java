package com.house.biet.delivery.command.application.listener;

import com.house.biet.delivery.command.domain.event.DeliveryCanceledEvent;
import com.house.biet.delivery.command.domain.event.DeliveryCompletedEvent;
import com.house.biet.delivery.command.domain.event.DeliveryStartedEvent;
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
    public void handleDeliveryStarted(DeliveryStartedEvent event) {
        // TODO: 추후 배달 시작 관련 기능 추가
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeliveryCompleted(DeliveryCompletedEvent event) {
        // TODO: 추후 배달 완료 메일 등 로직 추가
    }
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeliveryCanceled(DeliveryCanceledEvent event) {
        // TODO: 배달 취소 완료 등 로직 추가
    }
}
