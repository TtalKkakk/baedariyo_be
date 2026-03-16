package com.house.biet.storeSearch.query.autocomplete.infrastructure.redis;

import com.house.biet.storeSearch.query.autocomplete.port.AutoCompleteSearchRepositoryPort;
import com.house.biet.storeSearch.query.config.StoreSearchRedisKey;
import com.house.biet.storeSearch.query.util.HangulTypingGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class AutoCompleteSearchRepositoryRedisAdapter implements AutoCompleteSearchRepositoryPort {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String KEY = StoreSearchRedisKey.autoCompleteSearchKey();

    @Override
    public void saveKeyword(String keyword) {

        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();

        for (String prefix: HangulTypingGenerator.generate(keyword)) {
            String key = KEY + prefix;

            zSetOperations.incrementScore(key, keyword, 1.0);
        }
    }

    @Override
    public List<String> search(String prefix) {

        String key = KEY + prefix;

        Set<String> result = redisTemplate.opsForZSet().reverseRange(key, 0, 9);

        return result == null ? List.of() : List.copyOf(result);
    }
}
