package com.house.biet.delivery.command.application;

import com.house.biet.delivery.command.domain.aggregate.Delivery;
import com.house.biet.delivery.infrastructure.redis.DeliveryLocationRedisRepository;
import com.house.biet.delivery.websocket.dto.DeliveryLocationMessage;
import com.house.biet.rider.query.RiderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryLocationService {

    private final RiderQueryService riderQueryService;
    private final DeliveryService deliveryService;
    private final DeliveryLocationRedisRepository redisRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void handleLocation(DeliveryLocationMessage message, Long accountId) {

        // account → riderId 조회
        Long riderId = riderQueryService.getRiderIdByAccountId(accountId);

        // Delivery 조회
        Delivery delivery = deliveryService.getByOrderId(message.orderId());

        // 도메인 규칙 검증
        delivery.validateLocationUpdate(riderId);

        // Redis 실시간 위치 저장
        redisRepository.save(
                message.orderId(),
                riderId,
                message.latitude(),
                message.longitude()
        );

        // WebSocket 브로드캐스트
        messagingTemplate.convertAndSend(
                "/topic/order/" + message.orderId(),
                message
        );
    }
}