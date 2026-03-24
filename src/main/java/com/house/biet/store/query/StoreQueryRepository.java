package com.house.biet.store.query;

import com.house.biet.store.query.dto.StoreMenuQueryDto;

import java.util.List;
import java.util.UUID;

public interface StoreQueryRepository {

    List<StoreMenuQueryDto> findMenusById(Long storeId);

    List<StoreMenuQueryDto> findMenusByPublicId(UUID publicStoreId);
}
