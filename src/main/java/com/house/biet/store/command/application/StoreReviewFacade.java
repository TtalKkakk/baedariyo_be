package com.house.biet.store.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.application.dto.StoreReviewCreateRequestDto;
import com.house.biet.store.command.application.dto.StoreReviewCreateResponseDto;
import com.house.biet.store.command.domain.entity.StoreReview;
import com.house.biet.user.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreReviewFacade {

    private final StoreReviewService storeReviewService;
    private final UserQueryService userQueryService;

    /**
     * 리뷰 생성
     */
    public StoreReviewCreateResponseDto createReview(StoreReviewCreateRequestDto requestDto, UUID storePublicId, Long accountId) {
        // 계정 ID → 실제 userId 변환
        Long userId = userQueryService.getUserIdByAccountId(accountId);

        StoreReview storeReview = StoreReview.create(
                storePublicId,
                userId,
                requestDto.orderId(),
                requestDto.rating(),
                requestDto.storeReviewImages(),
                requestDto.storeReviewComment()
        );

        return StoreReviewCreateResponseDto.fromEntity(storeReviewService.save(storeReview));
    }

    /**
     * 리뷰 삭제 (작성자 본인 확인 포함)
     */
    public void deleteReview(UUID storeReviewPublicId, Long accountId) {
        StoreReview review = storeReviewService.findByPublicId(storeReviewPublicId);
        Long userId = userQueryService.getUserIdByAccountId(accountId);

        if (!review.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_STORE_REVIEW_MODIFICATION);
        }

        storeReviewService.deleteByPublicId(storeReviewPublicId);
    }
}