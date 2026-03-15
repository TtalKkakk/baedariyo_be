package com.house.biet.storeSearch.query.popular.application;

import com.house.biet.storeSearch.query.popular.port.PopularSearchRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularSearchService {

    private final PopularSearchRepositoryPort repository;

    public void increase(String keyword) {
        repository.increaseScore(keyword);
    }

    public List<String> getTopKeywords() {
        return repository.getTopKeywords();
    }
}
