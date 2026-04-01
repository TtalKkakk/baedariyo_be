package com.house.biet.store.query.application;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.store.query.dto.StoreSearchResponseDto;

import java.util.List;

public interface StoreSearchQueryService {

    /**
     * Stores을 검색한다
     *
     * @param accountId 계정 식별자
     * @param keyword 검색어
     * @param storeCategory storeCategory 값
     * @param customerLatitude customerLatitude 값
     * @param customerLongitude customerLongitude 값
     * @param offset offset 값
     * @param size size 값
     * @return 조회 결과 목록
     */
    List<StoreSearchResponseDto> searchStores(
            Long accountId,
            String keyword,
            StoreCategory storeCategory,
            double customerLatitude,
            double customerLongitude,
            int offset,
            int size
    );
}
