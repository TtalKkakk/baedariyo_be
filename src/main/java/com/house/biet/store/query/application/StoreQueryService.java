package com.house.biet.store.query.application;

import com.house.biet.store.query.dto.StoreMenuQueryDto;

import java.util.List;
import java.util.UUID;

public interface StoreQueryService {

    /**
     * 가게 메뉴 목록을 조회한다
     *
     * @param storeId 가게 식별자
     * @return 조회 결과 목록
     */
    List<StoreMenuQueryDto> getMenusByStoreId(Long storeId);

    /**
     * 가게 메뉴 목록을 조회한다
     *
     * @param publicStoreId 가게 공개 식별자
     * @return 조회 결과 목록
     */
    List<StoreMenuQueryDto> getMenusByPublicId(UUID publicStoreId);
}
