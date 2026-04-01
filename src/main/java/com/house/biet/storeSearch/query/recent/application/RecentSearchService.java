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

    /**
     * 대상을 저장한다
     *
     * @param userId 사용자 식별자
     * @param keyword 검색어
     */
    public void save(Long userId, String keyword) {
        String normalizedKeyword = normalizer.normalize(keyword);

        repository.saveKeyword(userId, normalizedKeyword);
    }

    /**
     * 최근 Keywords을 조회한다
     *
     * @param userId 사용자 식별자
     * @return 조회 결과 목록
     */
    public List<String> getRecentKeywords(Long userId) {
        return repository.getRecentKeywords(userId);
    }

    /**
     * 검색어을 삭제한다
     *
     * @param userId 사용자 식별자
     * @param keyword 검색어
     */
    public void deleteKeyword(Long userId, String keyword) {
        String normalizedKeyword = normalizer.normalize(keyword);

        repository.deleteKeyword(userId, normalizedKeyword);
    }

    /**
     * 전체을 삭제한다
     *
     * @param userId 사용자 식별자
     */
    public void deleteAll(Long userId) {
        repository.deleteAll(userId);
    }
}
