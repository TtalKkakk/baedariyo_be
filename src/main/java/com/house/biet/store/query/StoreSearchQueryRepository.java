package com.house.biet.store.query;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.store.query.dto.StoreSearchQueryDto;

import java.util.List;

public interface StoreSearchQueryRepository {

    List<StoreSearchQueryDto> searchStores(
            String keyword,
            StoreCategory storeCategory,
            double latitude,
            double longitude,
            int offset,
            int size
    );
}
