package com.house.biet.storeSearch.query.recent.application;

import com.house.biet.storeSearch.query.common.SearchKeywordNormalizer;
import com.house.biet.storeSearch.query.recent.port.RecentSearchRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSearchService {

    private final RecentSearchRepositoryPort repository;
    private final SearchKeywordNormalizer normalizer;

    public void save(Long userId, String keyword) {
        String normalizedKeyword = normalizer.normalize(keyword);

        repository.saveKeyword(userId, normalizedKeyword);
    }

    public List<String> getRecentKeywords(Long userId) {
        return repository.getRecentKeywords(userId);
    }

    public void deleteKeyword(Long userId, String keyword) {
        String normalizedKeyword = normalizer.normalize(keyword);

        repository.deleteKeyword(userId, normalizedKeyword);
    }

    public void deleteAll(Long userId) {
        repository.deleteAll(userId);
    }
}
