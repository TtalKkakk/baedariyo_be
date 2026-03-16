package com.house.biet.storeSearch.query.autocomplete.infrastructure.redis;

import com.house.biet.storeSearch.query.config.StoreSearchRedisKey;
import com.house.biet.storeSearch.query.util.HangulTypingGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
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
        final String PREFIX = StoreSearchRedisKey.autoCompleteSearchKey();
        String keyword = "황금올리브닭다리";

        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);

        List<String> expectedPrefixes = HangulTypingGenerator.generate(keyword);

        // when
        repository.saveKeyword(keyword);

        // then
        verify(zSetOperations, times(expectedPrefixes.size()))
                .incrementScore(keyCaptor.capture(), eq(keyword), eq(1.0));

        List<String> capturedKeys = keyCaptor.getAllValues();

        List<String> expectedKeys = expectedPrefixes.stream()
                .map(prefix -> PREFIX + prefix)
                .toList();

        assertThat(capturedKeys)
                .containsExactlyElementsOf(expectedKeys);
    }

    @Test
    @DisplayName("성공 - prefix 기반 자동완성 검색")
    void search_Success() {
        // given
        String prefix = "치";

        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
        given(zSetOperations.reverseRange(anyString(), eq(0L), eq(9L)))
                .willReturn(Set.of("치킨", "치즈볼"));

        // when
        List<String> result = repository.search(prefix);

        // then
        verify(zSetOperations).reverseRange(anyString(), eq(0L), eq(9L));

        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder("치킨", "치즈볼");
    }

    @Test
    @DisplayName("성공 - 자동완성 결과 없을 때 빈 리스트 반환")
    void search_Success_EmptyResult() {
        // given
        String prefix = "피자";

        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
        given(zSetOperations.reverseRange(anyString(), eq(0L), eq(9L)))
                .willReturn(null);

        // when
        List<String> result = repository.search(prefix);

        // then
        assertThat(result).isEmpty();
    }
}