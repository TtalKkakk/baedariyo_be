package com.house.biet.storeSearch.query.autocomplete.application;

import com.house.biet.storeSearch.query.autocomplete.port.AutoCompleteSearchRepositoryPort;
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
class AutoCompleteSearchServiceTest {

    @Mock
    private AutoCompleteSearchRepositoryPort repository;

    @InjectMocks
    private AutoCompleteSearchService service;

    @Test
    @DisplayName("성공 - 자동완성 키워드 저장")
    void registerKeyword_Success() {

        // given
        String keyword = "치킨";

        // when
        service.registerKeyword(keyword);

        // then
        then(repository).should().saveKeyword(keyword);
    }

    @Test
    @DisplayName("성공 - 자동완성 검색")
    void search_Success() {

        // given
        String prefix = "치";
        List<String> expected = List.of("치킨", "치즈볼");

        given(repository.search(prefix)).willReturn(expected);

        // when
        List<String> result = service.search(prefix);

        // then
        assertThat(result).isEqualTo(expected);
        then(repository).should().search(prefix);
    }
}