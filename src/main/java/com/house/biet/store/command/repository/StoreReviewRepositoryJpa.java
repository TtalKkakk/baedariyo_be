package com.house.biet.store.command.repository;

import com.house.biet.store.command.domain.entity.StoreReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreReviewRepositoryJpa
        extends JpaRepository<StoreReview, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM StoreReview sr WHERE sr.publicStoreReviewId = :publicStoreReviewId")
    void deleteByPublicStoreReviewId(UUID publicStoreReviewId);

    @Query("SELECT sr FROM StoreReview sr WHERE sr.publicStoreReviewId = :publicStoreReviewId")
    Optional<StoreReview> findByPublicStoreReviewId(UUID publicStoreReviewId);

    @Query("SELECT sr FROM StoreReview sr WHERE sr.storeId = :storePublicId")
    List<StoreReview> findByPublicStoreId(UUID storePublicId);

    @Query("SELECT sr FROM StoreReview sr WHERE sr.userId = :userId")
    List<StoreReview> findByUserId(Long userId);
}
