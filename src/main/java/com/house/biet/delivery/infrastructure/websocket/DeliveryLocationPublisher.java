package com.house.biet.delivery.infrastructure.websocket;

import com.house.biet.delivery.query.dto.DeliveryLocationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryLocationPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 주문에 대한 배달 위치를 구독자들에게 전송한다.
     */
    public void publish(Long orderId, DeliveryLocationResponseDto location) {

        messagingTemplate.convertAndSend(
                "/topic/delivery/" + orderId,
                location
        );
    }
}