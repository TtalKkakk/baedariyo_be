package com.house.biet.store.query.application;

import com.house.biet.store.query.dto.StoreMenuQueryDto;

import java.util.List;
import java.util.UUID;

public interface StoreQueryService {

    List<StoreMenuQueryDto> getMenusByStoreId(Long storeId);

    List<StoreMenuQueryDto> getMenusByPublicId(UUID publicStoreId);
}
