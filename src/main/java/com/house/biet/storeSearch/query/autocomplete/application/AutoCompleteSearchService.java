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

    public void registerKeyword(String keyword) {
        String normalizedKeyword = normalizer.normalize(keyword);

        repository.saveKeyword(normalizedKeyword);
    }

    public List<String> search(String prefix) {
        String normalizedPrefix = normalizer.normalize(prefix);

        return repository.search(normalizedPrefix);
    }
}
