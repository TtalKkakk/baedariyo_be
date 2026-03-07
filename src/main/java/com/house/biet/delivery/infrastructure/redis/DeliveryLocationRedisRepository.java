package com.house.biet.delivery.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeliveryLocationRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX = "delivery:order:";
    private static final Duration TTL = Duration.ofMinutes(30);

    private String key(Long orderId) {
        return PREFIX + orderId;
    }

    public void save(Long orderId,
                     Long riderId,
                     double latitude,
                     double longitude) {

        DeliveryLocationCache cache =
                new DeliveryLocationCache(
                        riderId,
                        latitude,
                        longitude,
                        LocalDateTime.now()
                );

        redisTemplate.opsForValue().set(key(orderId), cache, TTL);
    }

    /**
     * 현재 위치 조회
     */
    public Optional<DeliveryLocationCache> getLatestLocation(Long orderId) {
        Object value = redisTemplate.opsForValue().get(key(orderId));

        return Optional.ofNullable((DeliveryLocationCache) value);
    }

    /**
     * 배달 종료 시 위치 삭제
     */
    public void delete(Long orderId) {
        redisTemplate.delete(key(orderId));
    }
}