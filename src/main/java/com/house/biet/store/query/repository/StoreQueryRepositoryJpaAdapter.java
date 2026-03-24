package com.house.biet.store.query.repository;

import com.house.biet.store.query.StoreQueryRepository;
import com.house.biet.store.query.dto.StoreMenuQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreQueryRepositoryJpaAdapter implements StoreQueryRepository {

    private final StoreQueryRepositoryJpa storeQueryRepositoryJpa;

    @Override
    public List<StoreMenuQueryDto> findMenusById(Long storeId) {
        return storeQueryRepositoryJpa.findMenusById(storeId);
    }

    @Override
    public List<StoreMenuQueryDto> findMenusByPublicId(UUID publicStoreId) {
        return storeQueryRepositoryJpa.findMenusByPublicId(publicStoreId);
    }
}
