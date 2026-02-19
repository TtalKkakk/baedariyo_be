package com.house.biet.store.command.repository;

import com.house.biet.store.command.StoreReviewRepository;
import com.house.biet.store.command.domain.entity.StoreReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreReviewRepositoryJpaAdapter
        implements StoreReviewRepository {

    private final StoreReviewRepositoryJpa storeReviewRepositoryJpa;

    @Override
    public StoreReview save(StoreReview storeReview) {
        return storeReviewRepositoryJpa.save(storeReview);
    }

    @Override
    public void deleteByPublicStoreReviewId(UUID publicStoreReviewId) {
        storeReviewRepositoryJpa.deleteByPublicStoreReviewId(publicStoreReviewId);
    }

    @Override
    public Optional<StoreReview> findByPublicStoreReviewId(UUID publicStoreReviewId) {
        return storeReviewRepositoryJpa.findByPublicStoreReviewId(publicStoreReviewId);
    }

    @Override
    public List<StoreReview> findByPublicStoreId(UUID storePublicId) {
        return storeReviewRepositoryJpa.findByPublicStoreId(storePublicId);
    }

    @Override
    public List<StoreReview> findByUserId(Long userId) {
        return storeReviewRepositoryJpa.findByUserId(userId);
    }
}
