package com.house.biet.store.query;

import com.house.biet.store.command.domain.entity.Menu;

import java.util.List;
import java.util.UUID;

public interface StoreQueryRepository {

    /**
     * Store에 속한 모든 Menu를 조회한다.
     *
     * <p>
     * Menu는 Aggregate 내부 엔티티이므로
     * Store 식별자를 기준으로만 조회한다.
     * </p>
     *
     * @param storeId Store 내부 ID
     * @return Store에 속한 Menu 목록
     */
    List<Menu> findMenusById(Long storeId);

    /**
     * 외부 id를 이용하여 Store에 속한 모든 Menu를 조회한다.
     *
     * @param publicStoreId Store 외부 ID
     * @return Store에 속한 Menu 목록
     */
    List<Menu> findMenusByPublicId(UUID publicStoreId);
}
