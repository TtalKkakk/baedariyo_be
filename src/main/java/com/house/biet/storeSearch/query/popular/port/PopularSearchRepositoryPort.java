package com.house.biet.storeSearch.query.popular.port;

import java.util.List;

public interface PopularSearchRepositoryPort {

    void increaseScore(String keyword);

    List<String> getTopKeywords();
}
