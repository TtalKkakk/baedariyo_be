package com.house.biet.storeSearch.query.autocomplete.infrastructure.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AutoCompleteSearchRepositoryRedisAdapterTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private AutoCompleteSearchRepositoryRedisAdapter repository;

    @Test
    @DisplayName("성공 - 자동완성 키워드 저장")
    void saveKeyword_Success() {
        // given
        String keyword = "치킨";

        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);

        // when
        repository.saveKeyword(keyword);

        // then
        verify(zSetOperations).add(anyString(), eq(keyword), eq(0.0));
    }
    
    @Test
    @DisplayName("성공 - prefix 기반 자동완성 검색")
    void search_Success() {
        // given
        String prefix = "치";
        
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
        given(zSetOperations.rangeByLex(anyString(), any(Range.class), any(Limit.class)))
                .willReturn(Set.of("치킨", "치즈볼"));

        // when
        repository.search(prefix);

        // then
        verify(zSetOperations).rangeByLex(anyString(), any(Range.class), any(Limit.class));
    }
}