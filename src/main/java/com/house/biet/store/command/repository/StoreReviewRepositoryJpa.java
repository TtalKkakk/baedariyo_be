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

    /**
     * 공개 식별자을 삭제한다
     *
     * @param publicId publicId 값
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM StoreReview sr WHERE sr.publicId = :publicId")
    void deleteByPublicId(UUID publicId);

    /**
     * 공개 식별자을 조회한다
     *
     * @param publicId publicId 값
     * @return 조회 결과
     */
    @Query("SELECT sr FROM StoreReview sr WHERE sr.publicId = :publicId")
    Optional<StoreReview> findByPublicId(UUID publicId);

    /**
     * 가게 공개 식별자을 조회한다
     *
     * @param storePublicId storePublicId 값
     * @return 조회 결과 목록
     */
    @Query("SELECT sr FROM StoreReview sr WHERE sr.storePublicId = :storePublicId")
    List<StoreReview> findByStorePublicId(UUID storePublicId);

    /**
     * 사용자 식별자을 조회한다
     *
     * @param userId 사용자 식별자
     * @return 조회 결과 목록
     */
    @Query("SELECT sr FROM StoreReview sr WHERE sr.userId = :userId")
    List<StoreReview> findByUserId(Long userId);
}
