package com.house.biet.store.command;

import com.house.biet.store.command.domain.aggregate.Store;

import java.util.Optional;
import java.util.UUID;

/**
 * Store Aggregate에 대한 영속성 포트.
 *
 * <p>
 * Store는 Aggregate Root이며,
 * Menu는 Store 내부 엔티티로서 Store를 통해서만 조회/관리된다.
 * </p>
 */
public interface StoreRepository {

    /**
     * Store Aggregate를 저장한다.
     *
     * @param store 저장할 Store
     * @return 저장된 Store
     */
    Store save(Store store);

    /**
     * 내부 식별자(ID)로 Store를 조회한다.
     *
     * @param storeId Store의 내부 ID
     * @return Store Optional
     */
    Optional<Store> findById(Long storeId);

    /**
     * 외부 공개 식별자(publicId)로 Store를 조회한다.
     *
     * @param publicStoreId Store의 공개 ID
     * @return Store Optional
     */
    Optional<Store> findByPublicId(UUID publicStoreId);
}