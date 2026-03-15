package com.house.biet.storeSearch.query.recent.application;

import com.house.biet.storeSearch.query.recent.port.RecentSearchRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSearchService {

    private final RecentSearchRepositoryPort repository;

    public void save(Long userId, String keyword) {
        repository.saveKeyword(userId, keyword);
    }

    public List<String> getRecentKeywords(Long userId) {
        return repository.getRecentKeywords(userId);
    }

    public void deleteKeyword(Long userId, String keyword) {
        repository.deleteKeyword(userId, keyword);
    }

    public void deleteAll(Long userId) {
        repository.deleteAll(userId);
    }
}
