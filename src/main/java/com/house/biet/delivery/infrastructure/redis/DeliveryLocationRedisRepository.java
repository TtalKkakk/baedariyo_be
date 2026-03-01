package com.house.biet.delivery.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class DeliveryLocationRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX = "delivery:order:";
    private static final Duration TTL = Duration.ofMinutes(30);

    public void save(Long orderId,
                     Long riderId,
                     double latitude,
                     double longitude) {

        String key = PREFIX + orderId;

        DeliveryLocationCache cache =
                new DeliveryLocationCache(
                        riderId,
                        latitude,
                        longitude,
                        LocalDateTime.now()
                );

        redisTemplate.opsForValue().set(key, cache, TTL);
    }

    public DeliveryLocationCache getLatestLocation(Long orderId) {
        return (DeliveryLocationCache)
                redisTemplate.opsForValue().get(PREFIX + orderId);
    }

    public void delete(Long orderId) {
        redisTemplate.delete(PREFIX + orderId);
    }
}