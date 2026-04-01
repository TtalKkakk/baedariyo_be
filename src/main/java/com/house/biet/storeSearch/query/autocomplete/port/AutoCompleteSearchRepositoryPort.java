package com.house.biet.storeSearch.query.autocomplete.port;

import java.util.List;

public interface AutoCompleteSearchRepositoryPort {

    /**
     * 검색어을 저장한다
     *
     * @param keyword 검색어
     */
    void saveKeyword(String keyword);

    /**
     * 목록을 검색한다
     *
     * @param prefix prefix 값
     * @return 조회 결과 목록
     */
    List<String> search(String prefix);
}
