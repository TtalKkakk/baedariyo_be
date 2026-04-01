package com.house.biet.storeSearch.query.popular.application;

import com.house.biet.storeSearch.query.common.SearchKeywordNormalizer;
import com.house.biet.storeSearch.query.popular.port.PopularSearchRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularSearchService {

    private final PopularSearchRepositoryPort repository;
    private final SearchKeywordNormalizer normalizer;

    /**
     * 대상을 처리한다
     *
     * @param keyword 검색어
     */
    public void increase(String keyword) {
        String normalizedKeyword = normalizer.normalize(keyword);

        repository.increaseScore(normalizedKeyword);
    }

    /**
     * Top Keywords을 조회한다
     *
     * @return 조회 결과 목록
     */
    public List<String> getTopKeywords() {
        return repository.getTopKeywords();
    }
}
