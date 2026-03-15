package com.house.biet.storeSearch.query.autocomplete.infrastructure.redis;

import com.house.biet.storeSearch.query.autocomplete.port.AutoCompleteSearchRepositoryPort;
import com.house.biet.storeSearch.query.config.StoreSearchRedisKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
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
        keyword = normalize(keyword);

        redisTemplate.opsForZSet().add(KEY, keyword, 0);
    }

    @Override
    public List<String> search(String prefix) {
        prefix = normalize(prefix);

        Range<String> range = Range.of(
                Range.Bound.inclusive(prefix),
                Range.Bound.inclusive(prefix + "\uffff")
        );

        Limit limit = Limit.limit().count(10);

        Set<String> result = redisTemplate.opsForZSet().rangeByLex(KEY, range, limit);

        return result == null ? List.of() : List.copyOf(result);
    }

    private String normalize(String keyword) {
        return keyword.trim().toLowerCase();
    }
}
