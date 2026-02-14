package com.house.biet.order.command.infrastructure.messaging;

import com.house.biet.order.command.domain.event.OrderCreatedEvent;

/**
 * 주문 도메인 이벤트를 외부 시스템이나 다른 모듈로 발행하는 인터페이스입니다.
 */
public interface OrderEventPublisher {

    /**
     * 주문 생성 이벤트를 발행합니다.
     *
     * @param event 발행할 주문 생성 이벤트 객체
     */
    void publishOrderCreated(OrderCreatedEvent event);
}
