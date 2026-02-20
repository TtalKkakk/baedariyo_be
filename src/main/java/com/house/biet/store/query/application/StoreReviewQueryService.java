package com.house.biet.store.query.application;

import com.house.biet.store.query.dto.MyStoreReviewDto;
import com.house.biet.store.query.dto.StoreReviewDto;

import java.util.List;
import java.util.UUID;

/**
 * StoreReview 조회 전용 서비스.
 *
 * <p>
 * Query 영역 전용으로, 화면 표시를 위한 조회 로직만 포함.
 * 엔티티 반환이나 상태 변경은 수행하지 않는다.
 * </p>
 */
public interface StoreReviewQueryService {

    /**
     * 사용자가 작성한 리뷰 목록 조회
     *
     * @param userId 사용자 식별자
     * @return 내가 작성한 리뷰 DTO 목록
     */
    List<MyStoreReviewDto> findMyReviews(Long userId);

    /**
     * 특정 가게 리뷰 목록 조회
     *
     * @param storePublicId 가게 공개 식별자
     * @return 가게 리뷰 DTO 목록
     */
    List<StoreReviewDto> findReviewsByStore(UUID storePublicId);

    /**
     * 특정 가게(Store)의 최근 사진 리뷰 최대 3개 조회
     *
     * @param storePublicId 조회 대상 가게 공개 ID
     * @return 최근 사진 리뷰 최대 3개
     */
    List<StoreReviewDto> findTop3PhotoReviewsByStore(UUID storePublicId);
}
