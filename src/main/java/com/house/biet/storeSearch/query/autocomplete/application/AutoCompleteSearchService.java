package com.house.biet.storeSearch.query.autocomplete.application;

import com.house.biet.storeSearch.query.autocomplete.port.AutoCompleteSearchRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoCompleteSearchService {

    private final AutoCompleteSearchRepositoryPort repository;

    public void registerKeyword(String keyword) {
        repository.saveKeyword(keyword);
    }

    public List<String> search(String prefix) {
        return repository.search(prefix);
    }
}
