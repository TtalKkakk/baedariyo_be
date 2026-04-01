package com.house.biet.storeSearch.query.recent.port;

import java.util.List;

public interface RecentSearchRepositoryPort {

    /**
     * 검색어을 저장한다
     *
     * @param userId 사용자 식별자
     * @param keyword 검색어
     */
    void saveKeyword(Long userId, String keyword);

    /**
     * 최근 Keywords을 조회한다
     *
     * @param userId 사용자 식별자
     * @return 조회 결과 목록
     */
    List<String> getRecentKeywords(Long userId);

    /**
     * 검색어을 삭제한다
     *
     * @param userId 사용자 식별자
     * @param keyword 검색어
     */
    void deleteKeyword(Long userId, String keyword);

    /**
     * 전체을 삭제한다
     *
     * @param userId 사용자 식별자
     */
    void deleteAll(Long userId);
}