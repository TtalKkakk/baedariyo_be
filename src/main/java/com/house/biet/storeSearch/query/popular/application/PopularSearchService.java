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

    public void increase(String keyword) {
        String normalizedKeyword = normalizer.normalize(keyword);

        repository.increaseScore(normalizedKeyword);
    }

    public List<String> getTopKeywords() {
        return repository.getTopKeywords();
    }
}
