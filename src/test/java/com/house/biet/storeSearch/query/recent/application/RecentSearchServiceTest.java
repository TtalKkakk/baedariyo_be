package com.house.biet.storeSearch.query.recent.application;

import com.house.biet.storeSearch.query.common.SearchKeywordNormalizer;
import com.house.biet.storeSearch.query.recent.port.RecentSearchRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecentSearchServiceTest {

    @Mock
    private RecentSearchRepositoryPort repository;

    @Mock
    private SearchKeywordNormalizer normalizer;

    @InjectMocks
    private RecentSearchService service;

    @Test
    @DisplayName("성공 - 최근 검색어 저장")
    void save_Success() {

        // given
        Long userId = 1L;
        String keyword = "치킨";

        given(normalizer.normalize(keyword)).willReturn(keyword);

        // when
        service.save(userId, keyword);

        // then
        then(repository).should().saveKeyword(userId, keyword);
    }

    @Test
    @DisplayName("성공 - 최근 검색어 조회")
    void getRecentKeywords_Success() {

        // given
        Long userId = 1L;
        List<String> expected = List.of("치킨", "피자");

        given(repository.getRecentKeywords(userId)).willReturn(expected);

        // when
        List<String> result = service.getRecentKeywords(userId);

        // then
        assertThat(result).isEqualTo(expected);
        then(repository).should().getRecentKeywords(userId);
    }

    @Test
    @DisplayName("성공 - 특정 검색어 삭제")
    void deleteKeyword_Success() {

        // given
        Long userId = 1L;
        String keyword = "치킨";

        given(normalizer.normalize(keyword)).willReturn(keyword);

        // when
        service.deleteKeyword(userId, keyword);

        // then
        then(repository).should().deleteKeyword(userId, keyword);
    }

    @Test
    @DisplayName("성공 - 전체 검색어 삭제")
    void deleteAll_Success() {

        // given
        Long userId = 1L;

        // when
        service.deleteAll(userId);

        // then
        then(repository).should().deleteAll(userId);
    }
}