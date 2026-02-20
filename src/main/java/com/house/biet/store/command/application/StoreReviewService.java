package com.house.biet.store.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.StoreReviewRepository;
import com.house.biet.store.command.domain.entity.StoreReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * StoreReview 도메인 Service
 *
 * <p>
 * StoreReview 엔티티의 생성, 조회, 삭제 등 비즈니스 로직을 담당.
 * Command Repository를 통해 DB 상태 변경 및 조회 수행.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class StoreReviewService {

    private final StoreReviewRepository storeReviewRepository;

    /**
     * StoreReview 저장
     *
     * @param storeReview 저장할 리뷰 엔티티
     * @return 저장된 리뷰 엔티티
     */
    public StoreReview save(StoreReview storeReview) {
        return storeReviewRepository.save(storeReview);
    }

    /**
     * publicStoreReviewId 기준으로 리뷰 삭제
     *
     * @param publicId 삭제 대상 리뷰 공개 식별자
     */
    public void deleteByPublicId(UUID publicId) {
        storeReviewRepository.deleteByPublicId(publicId);
    }

    /**
     * publicStoreReviewId 기준 리뷰 조회
     *
     * @param publicId 조회 대상 리뷰 공개 식별자
     * @return 조회된 StoreReview
     * @throws CustomException 리뷰를 찾지 못하면 STORE_REVIEW_NOT_FOUND 예외 발생
     */
    public StoreReview findByPublicId(UUID publicId) {
        return storeReviewRepository.findByPublicId(publicId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_REVIEW_NOT_FOUND));
    }

    /**
     * 특정 가게(Store) 리뷰 목록 조회
     *
     * @param publicStoreId 조회 대상 가게 공개 식별자
     * @return 해당 가게에 작성된 리뷰 목록
     */
    public List<StoreReview> findByPublicStoreId(UUID publicStoreId) {
        return storeReviewRepository.findByStorePublicId(publicStoreId);
    }

    /**
     * 특정 사용자 작성 리뷰 조회
     *
     * @param userId 조회 대상 사용자 ID
     * @return 해당 사용자가 작성한 리뷰 목록
     */
    public List<StoreReview> findByUserId(Long userId) {
        return storeReviewRepository.findByUserId(userId);
    }
}