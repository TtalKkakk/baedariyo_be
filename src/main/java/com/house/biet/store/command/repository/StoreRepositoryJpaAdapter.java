package com.house.biet.store.command.repository;

import com.house.biet.store.command.StoreRepository;
import com.house.biet.store.command.domain.aggregate.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryJpaAdapter implements StoreRepository {

    private final StoreRepositoryJpa storeRepositoryJpa;

    @Override
    public Store save(Store store) {
        return storeRepositoryJpa.save(store);
    }

    @Override
    public Optional<Store> findById(Long storeId) {
        return storeRepositoryJpa.findById(storeId);
    }

    @Override
    public Optional<Store> findByPublicId(UUID publicStoreId) {
        return storeRepositoryJpa.findByPublicId(publicStoreId);
    }
}
