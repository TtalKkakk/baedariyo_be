package com.house.biet.delivery.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RouteTimeCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration TTL = Duration.ofMinutes(10);

    /**
     * 대상을 조회한다
     *
     * @param key key 값
     * @return 처리 결과 값
     */
    public Integer get(String key) {

        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        return Integer.valueOf(String.valueOf(value));
    }

    /**
     * 대상을 저장한다
     *
     * @param key key 값
     * @param minutes minutes 값
     */
    public void save(String key, int minutes) {
        redisTemplate.opsForValue()
                .set(key, minutes, TTL);
    }
}
