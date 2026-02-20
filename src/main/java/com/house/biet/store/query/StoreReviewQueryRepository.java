package com.house.biet.store.query;

import com.house.biet.store.query.dto.MyStoreReviewDto;
import com.house.biet.store.query.dto.StoreReviewDto;

import java.util.List;
import java.util.UUID;

/**
 * StoreReview 조회 전용 Repository.
 *
 * <p>
 * 본 인터페이스는 Query 영역에 속하며,
 * 화면 표시를 위한 데이터 조회만을 책임진다.
 * 엔티티 반환이나 상태 변경 로직은 포함하지 않는다.
 * </p>
 *
 * <p>
 * JOIN, 정렬, DTO 매핑, 성능 최적화는 모두 이 레이어에서 처리한다.
 * Application 계층에서는 조회 결과를 조합하거나
 * 응답 형태로 변환하는 역할만 수행한다.
 * </p>
 */
public interface StoreReviewQueryRepository {

    /**
     * 사용자가 작성한 리뷰 목록을 조회한다.
     *
     * <p>
     * 마이페이지 등에서 사용되며,
     * 상호명, 별점, 작성일, 대표 이미지(썸네일), 리뷰 내용을 포함한다.
     * </p>
     *
     * @param userId 사용자 식별자
     * @return 내가 작성한 리뷰 목록
     */
    List<MyStoreReviewDto> findMyReviews(Long userId);

    /**
     * 특정 가게에 대한 리뷰 목록을 조회한다.
     *
     * <p>
     * 가게 상세 화면에서 사용되며,
     * 여러 사용자의 리뷰를 포함한다.
     * </p>
     *
     * @param storePublicId 가게 공개 식별자
     * @return 가게에 대한 리뷰 목록
     */
    List<StoreReviewDto> findReviewsByStore(UUID storePublicId);

    /**
     * 특정 가게에 대한 리뷰 목록을 날짜순으로 3개 조회한다.
     *
     * <p>
     * 가게 상세 화면에서 사용되며,
     * 여러 사용자의 리뷰를 포함한다.
     * </p>
     *
     * @param storePublicId 가게 공개 식별자
     * @return 가게에 대한 리뷰 목록
     */
    List<StoreReviewDto> findTop3PhotoReviewsByStore(UUID storePublicId);
}