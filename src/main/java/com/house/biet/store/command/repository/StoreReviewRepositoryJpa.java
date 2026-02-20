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
    @Query("DELETE FROM StoreReview sr WHERE sr.publicId = :publicId")
    void deleteByPublicId(UUID publicId);

    @Query("SELECT sr FROM StoreReview sr WHERE sr.publicId = :publicId")
    Optional<StoreReview> findByPublicId(UUID publicId);

    @Query("SELECT sr FROM StoreReview sr WHERE sr.storePublicId = :storePublicId")
    List<StoreReview> findByStorePublicId(UUID storePublicId);

    @Query("SELECT sr FROM StoreReview sr WHERE sr.userId = :userId")
    List<StoreReview> findByUserId(Long userId);
}
