package com.house.biet.storeSearch.query.autocomplete.application;

import com.house.biet.storeSearch.query.autocomplete.port.AutoCompleteSearchRepositoryPort;
import com.house.biet.storeSearch.query.common.SearchKeywordNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoCompleteSearchService {

    private final AutoCompleteSearchRepositoryPort repository;
    private final SearchKeywordNormalizer normalizer;

    /**
     * 검색어을 처리한다
     *
     * @param keyword 검색어
     */
    public void registerKeyword(String keyword) {
        String normalizedKeyword = normalizer.normalize(keyword);

        repository.saveKeyword(normalizedKeyword);
    }

    /**
     * 목록을 검색한다
     *
     * @param prefix prefix 값
     * @return 조회 결과 목록
     */
    public List<String> search(String prefix) {
        String normalizedPrefix = normalizer.normalize(prefix);

        return repository.search(normalizedPrefix);
    }
}
