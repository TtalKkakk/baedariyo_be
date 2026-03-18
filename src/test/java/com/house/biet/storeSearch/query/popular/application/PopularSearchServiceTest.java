package com.house.biet.storeSearch.query.popular.application;

import com.house.biet.storeSearch.query.common.SearchKeywordNormalizer;
import com.house.biet.storeSearch.query.popular.port.PopularSearchRepositoryPort;
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
class PopularSearchServiceTest {

    @Mock
    private PopularSearchRepositoryPort repository;

    @Mock
    private SearchKeywordNormalizer normalizer;

    @InjectMocks
    private PopularSearchService service;

    @Test
    @DisplayName("성공 - 인기검색어 점수 증가")
    void increase_Success() {

        // given
        String keyword = "치킨";

        given(normalizer.normalize(keyword)).willReturn(keyword);

        // when
        service.increase(keyword);

        // then
        then(repository).should().increaseScore(keyword);
    }

    @Test
    @DisplayName("성공 - 인기검색어 조회")
    void getTopKeywords_Success() {

        // given
        List<String> expected = List.of("치킨", "피자");

        given(repository.getTopKeywords()).willReturn(expected);

        // when
        List<String> result = service.getTopKeywords();

        // then
        assertThat(result).isEqualTo(expected);
        then(repository).should().getTopKeywords();
    }
}