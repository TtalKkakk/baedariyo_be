package com.house.biet.storeSearch.query.recent.infrastructure.redis;

import com.house.biet.storeSearch.query.config.StoreSearchRedisKey;
import com.house.biet.storeSearch.query.recent.port.RecentSearchRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RecentSearchRepositoryRedisAdapter implements RecentSearchRepositoryPort {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int MAX_SIZE = 10;

    @Override
    public void saveKeyword(Long userId, String keyword) {

        String key = StoreSearchRedisKey.recentSearchKey(userId);
        long timestamp = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(key, keyword, timestamp);

        long minScore = 0;
        long maxScore = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7;

        redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore);
    }

    @Override
    public List<String> getRecentKeywords(Long userId) {

        String key = StoreSearchRedisKey.recentSearchKey(userId);

        Set<String> result =
                redisTemplate.opsForZSet().reverseRange(key, 0, MAX_SIZE - 1);

        return result == null ? List.of() : List.copyOf(result);
    }

    @Override
    public void deleteKeyword(Long userId, String keyword) {

        String key = StoreSearchRedisKey.recentSearchKey(userId);

        redisTemplate.opsForZSet().remove(key, keyword);
    }

    @Override
    public void deleteAll(Long userId) {
        String key = StoreSearchRedisKey.recentSearchKey(userId);

        redisTemplate.delete(key);
    }
}
