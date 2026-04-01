package com.house.biet.store.query;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.store.query.dto.StoreSearchQueryDto;

import java.util.List;

public interface StoreSearchQueryRepository {

    /**
     * Stores을 검색한다
     *
     * @param keyword 검색어
     * @param storeCategory storeCategory 값
     * @param latitude latitude 값
     * @param longitude longitude 값
     * @param offset offset 값
     * @param size size 값
     * @return 조회 결과 목록
     */
    List<StoreSearchQueryDto> searchStores(
            String keyword,
            StoreCategory storeCategory,
            double latitude,
            double longitude,
            int offset,
            int size
    );
}
