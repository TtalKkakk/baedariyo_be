package com.house.biet.store.command;

import com.house.biet.store.command.domain.entity.StoreReview;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * StoreReview Aggregate의 영속성과 상태 변경을 담당하는 Repository.
 *
 * <p>
 * 본 인터페이스는 Command 영역에 속하며,
 * 도메인 로직 수행에 필요한 엔티티 단위 조회와 변경만을 책임진다.
 * 화면 조회 목적의 데이터 조합, join, DTO 반환은 허용하지 않는다.
 * </p>
 */
public interface StoreReviewRepository {

    /**
     * StoreReview를 저장한다.
     *
     * @param storeReview 저장할 StoreReview 엔티티
     * @return 저장된 StoreReview
     */
    StoreReview save(StoreReview storeReview);

    /**
     * publicStoreReviewId를 기준으로 StoreReview를 삭제한다.
     *
     * @param publicStoreReviewId 공개 식별자
     */
    void deleteByPublicStoreReviewId(UUID publicStoreReviewId);

    /**
     * publicStoreReviewId를 기준으로 StoreReview를 조회한다.
     *
     * <p>
     * 수정, 삭제, 권한 검증 등 도메인 로직 수행을 위한 조회에 사용된다.
     * </p>
     *
     * @param publicStoreReviewId 공개 식별자
     * @return StoreReview Optional
     */
    Optional<StoreReview> findByPublicStoreReviewId(UUID publicStoreReviewId);

    /**
     * 특정 가게에 속한 StoreReview 목록을 조회한다.
     *
     * <p>
     * 도메인 규칙 검증 또는 집합 단위 처리 용도로 사용된다.
     * </p>
     *
     * @param storePublicId 가게 공개 식별자
     * @return StoreReview 목록
     */
    List<StoreReview> findByPublicStoreId(UUID storePublicId);

    /**
     * 특정 사용자가 작성한 StoreReview 목록을 조회한다.
     *
     * @param userId 사용자 식별자
     * @return StoreReview 목록
     */
    List<StoreReview> findByUserId(Long userId);
}
