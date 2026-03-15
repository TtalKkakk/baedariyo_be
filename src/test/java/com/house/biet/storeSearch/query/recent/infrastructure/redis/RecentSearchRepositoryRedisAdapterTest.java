package com.house.biet.storeSearch.query.recent.infrastructure.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecentSearchRepositoryRedisAdapterTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private RecentSearchRepositoryRedisAdapter repository;

    @Test
    @DisplayName("성공 - 최근 검색어 저장")
    void saveKeyword_Success() {
        // given
        Long userId = 1L;
        String keyword = "치킨";

        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);

        // when
        repository.saveKeyword(userId, keyword);

        // then
        verify(zSetOperations).add(anyString(), eq(keyword), anyDouble());
    }

    @Test
    @DisplayName("성공 - 최근 검색어 저장 시 오래된 데이터 삭제")
    void saveKeyword_RemoveOldData_Success() {

        // given
        Long userId = 1L;
        String keyword = "치킨";

        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);

        // when
        repository.saveKeyword(userId, keyword);

        // then
        verify(zSetOperations).add(anyString(), eq(keyword), anyDouble());
        verify(zSetOperations).removeRangeByScore(
                anyString(),
                anyDouble(),
                anyDouble()
        );
    }

    @Test
    @DisplayName("성공 - 특정 검색어 삭제")
    void deleteKeyword_Success() {
        // given
        Long userId = 1L;
        String keyword = "치킨";

        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);

        // when
        repository.deleteKeyword(userId, keyword);

        // then
        verify(zSetOperations).remove(anyString(), eq(keyword));
    }

    @Test
    @DisplayName("성공 - 전체 검색어 삭제")
    void deleteAll_Success() {
        // given
        Long userId = 1L;

        // when
        repository.deleteAll(userId);

        // then
        verify(redisTemplate).delete(anyString());
    }
}