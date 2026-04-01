package com.house.biet.storeSearch.query.popular.port;

import java.util.List;

public interface PopularSearchRepositoryPort {

    /**
     * Score을 처리한다
     *
     * @param keyword 검색어
     */
    void increaseScore(String keyword);

    /**
     * Top Keywords을 조회한다
     *
     * @return 조회 결과 목록
     */
    List<String> getTopKeywords();
}
