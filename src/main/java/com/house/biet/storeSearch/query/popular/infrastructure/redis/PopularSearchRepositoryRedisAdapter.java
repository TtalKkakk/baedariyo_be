package com.house.biet.storeSearch.query.popular.infrastructure.redis;


import com.house.biet.storeSearch.query.config.StoreSearchRedisKey;
import com.house.biet.storeSearch.query.popular.port.PopularSearchRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class PopularSearchRepositoryRedisAdapter implements PopularSearchRepositoryPort {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void increaseScore(String keyword) {
        String key = StoreSearchRedisKey.popularSearchKey();

        redisTemplate.opsForZSet().incrementScore(key, keyword, 1);
    }

    @Override
    public List<String> getTopKeywords() {
        String key = StoreSearchRedisKey.popularSearchKey();

        Set<String> result = redisTemplate.opsForZSet().reverseRange(key, 0, 9);

        return result == null ? List.of() : List.copyOf(result);
    }
}
