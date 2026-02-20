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
    public void deleteByPublicId(UUID publicId) {
        storeReviewRepositoryJpa.deleteByPublicId(publicId);
    }

    @Override
    public Optional<StoreReview> findByPublicId(UUID publicId) {
        return storeReviewRepositoryJpa.findByPublicId(publicId);
    }

    @Override
    public List<StoreReview> findByStorePublicId(UUID storePublicId) {
        return storeReviewRepositoryJpa.findByStorePublicId(storePublicId);
    }

    @Override
    public List<StoreReview> findByUserId(Long userId) {
        return storeReviewRepositoryJpa.findByUserId(userId);
    }
}
