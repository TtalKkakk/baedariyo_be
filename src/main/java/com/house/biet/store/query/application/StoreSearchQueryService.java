package com.house.biet.store.query.application;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.store.query.dto.StoreSearchResponseDto;

import java.util.List;

public interface StoreSearchQueryService {

    List<StoreSearchResponseDto> searchStores(
            String keyword,
            StoreCategory storeCategory,
            double customerLatitude,
            double customerLongitude,
            int offset,
            int size
    );
}
