package com.house.biet.storeSearch.query.popular.infrastructure.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PopularSearchRepositoryRedisAdapterTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;
    
    @Mock
    private ZSetOperations<String, String> zSetOperations;
    
    @InjectMocks
    private PopularSearchRepositoryRedisAdapter repository;
    
    @Test
    @DisplayName("성공 - 인기 검색어 점수 증가")
    void increaseScore_Success() {
        // given
        String keyword = "치킨";

        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);

        // when
        repository.increaseScore(keyword);

        // then
        verify(zSetOperations).incrementScore(anyString(), eq(keyword), eq(1.0));
    }

    @Test
    @DisplayName("성공 - 인기 검색어 Top10 조회")
    void getTopKeywords_Success() {
        // given
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
        given(zSetOperations.reverseRange(anyString(), eq(0L), eq(9l)))
                .willReturn(Set.of("치킨", "피자"));

        // when
        repository.getTopKeywords();

        // then
        verify(zSetOperations).reverseRange(anyString(), eq(0L), eq(9L));
    }
}