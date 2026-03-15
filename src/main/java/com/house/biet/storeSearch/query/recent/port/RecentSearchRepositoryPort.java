package com.house.biet.storeSearch.query.recent.port;

import java.util.List;

public interface RecentSearchRepositoryPort {

    void saveKeyword(Long userId, String keyword);

    List<String> getRecentKeywords(Long userId);

    void deleteKeyword(Long userId, String keyword);

    void deleteAll(Long userId);
}