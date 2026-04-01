package com.house.biet.store.query;

import com.house.biet.store.query.dto.StoreMenuQueryDto;

import java.util.List;
import java.util.UUID;

public interface StoreQueryRepository {

    /**
     * 가게 메뉴 목록을 조회한다
     *
     * @param storeId 가게 식별자
     * @return 조회 결과 목록
     */
    List<StoreMenuQueryDto> findMenusById(Long storeId);

    /**
     * 가게 메뉴 목록을 조회한다
     *
     * @param publicStoreId 가게 공개 식별자
     * @return 조회 결과 목록
     */
    List<StoreMenuQueryDto> findMenusByPublicId(UUID publicStoreId);
}
