package com.house.biet.storeSearch.query.autocomplete.infrastructure.redis;

import com.house.biet.storeSearch.query.autocomplete.port.AutoCompleteSearchRepositoryPort;
import com.house.biet.storeSearch.query.config.StoreSearchRedisKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class AutoCompleteSearchRepositoryRedisAdapter implements AutoCompleteSearchRepositoryPort {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String KEY = StoreSearchRedisKey.autoCompleteSearchKey();

    @Override
    public void saveKeyword(String keyword) {

        for (int i = 1; i <= keyword.length(); i++) {

            String prefix = keyword.substring(0, i);
            String key = KEY + prefix;

            redisTemplate.opsForZSet()
                    .incrementScore(key, keyword, 1);
        }
    }

    @Override
    public List<String> search(String prefix) {

        String key = KEY + prefix;

        Set<String> result = redisTemplate.opsForZSet().reverseRange(key, 0, 9);

        return result == null ? List.of() : List.copyOf(result);
    }
}
