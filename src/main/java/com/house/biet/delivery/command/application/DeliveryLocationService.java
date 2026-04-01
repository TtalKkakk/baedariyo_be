package com.house.biet.delivery.command.application;

import com.house.biet.delivery.command.domain.aggregate.Delivery;
import com.house.biet.delivery.infrastructure.redis.DeliveryLocationCache;
import com.house.biet.delivery.infrastructure.redis.DeliveryLocationRedisRepository;
import com.house.biet.delivery.infrastructure.websocket.DeliveryLocationPublisher;
import com.house.biet.delivery.query.dto.DeliveryLocationResponseDto;
import com.house.biet.delivery.websocket.dto.DeliveryLocationMessage;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
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
    private final DeliveryLocationPublisher publisher;

    /**
     * 라이더 위치 업데이트 처리
     *
     * @param message message 값
     * @param accountId 계정 식별자
     */
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

    /**
     * 현재 배달 위치 조회
     *
     * @param orderId 주문 식별자
     * @return getCurrentLocation 결과
     */
    @Transactional(readOnly = true)
    public DeliveryLocationResponseDto getCurrentLocation(Long orderId) {
        DeliveryLocationCache cache = redisRepository.getLatestLocation(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_LOCATION_NOT_FOUND));

        return new DeliveryLocationResponseDto(
                cache.latitude(),
                cache.longitude()
        );
    }


    /**
     * 배달 완료 시 위치 삭제
     *
     * @param orderId 주문 식별자
     */
    @Transactional
    public void clearLocation(Long orderId) {
        redisRepository.delete(orderId);
    }

    /**
     * 위치을 변경한다
     *
     * @param orderId 주문 식별자
     * @param latitude latitude 값
     * @param longitude longitude 값
     */
    @Transactional
    public void updateLocation(Long orderId, double latitude, double longitude) {

        DeliveryLocationResponseDto location = new DeliveryLocationResponseDto(latitude, longitude);

        publisher.publish(orderId, location);
    }
}