package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.store.command.application.StoreReviewFacade;
import com.house.biet.store.command.application.StoreReviewService;
import com.house.biet.store.command.application.dto.StoreReviewCreateRequestDto;
import com.house.biet.store.command.application.dto.StoreReviewCreateResponseDto;
import com.house.biet.store.command.domain.entity.StoreReview;
import com.house.biet.store.query.application.StoreReviewQueryService;
import com.house.biet.store.query.dto.MyStoreReviewDto;
import com.house.biet.store.query.dto.StoreReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreReviewController {

    private final StoreReviewService storeReviewService;
    private final StoreReviewQueryService storeReviewQueryService;
    private final StoreReviewFacade storeReviewFacade;

    // ==============================
    // 1️⃣ 리뷰 생성 (가게 기준)
    // ==============================
    @PostMapping("/stores/{storePublicId}/reviews")
    public ResponseEntity<CustomApiResponse<StoreReviewCreateResponseDto>> createReview(
            @PathVariable UUID storePublicId,
            @RequestBody StoreReviewCreateRequestDto requestDto,
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        StoreReviewCreateResponseDto responseDto =
                storeReviewFacade.createReview(requestDto, storePublicId, principal.accountId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.STORE_REVIEW_CREATE_SUCCESS, responseDto)
        );
    }

    // ==============================
    // 2️⃣ 특정 가게 리뷰 목록 조회
    // ==============================
    @GetMapping("/stores/{storePublicId}/reviews")
    public ResponseEntity<CustomApiResponse<List<StoreReviewDto>>> getStoreReviews(
            @PathVariable UUID storePublicId
    ) {
        List<StoreReviewDto> reviews =
                storeReviewQueryService.findReviewsByStore(storePublicId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.OK, reviews)
        );
    }

    // ==============================
    // 3️⃣ 리뷰 단건 조회 (리뷰 기준)
    // ==============================
    @GetMapping("/reviews/{storeReviewPublicId}")
    public ResponseEntity<CustomApiResponse<StoreReview>> getReview(
            @PathVariable UUID storeReviewPublicId
    ) {
        StoreReview review =
                storeReviewService.findByPublicId(storeReviewPublicId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.OK, review)
        );
    }

    // ==============================
    // 4️⃣ 리뷰 삭제 (리뷰 기준)
    // ==============================
    @DeleteMapping("/reviews/{storeReviewPublicId}")
    public ResponseEntity<CustomApiResponse<Void>> deleteReview(
            @PathVariable UUID storeReviewPublicId,
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        storeReviewFacade.deleteReview(storeReviewPublicId, principal.accountId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.STORE_REVIEW_DELETE_SUCCESS)
        );
    }

    // ==============================
    // 5️⃣ 내가 작성한 리뷰 조회
    // ==============================
    @GetMapping("/reviews/me")
    public ResponseEntity<CustomApiResponse<List<MyStoreReviewDto>>> getMyReviews(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        List<MyStoreReviewDto> reviews =
                storeReviewQueryService.findMyReviews(principal.accountId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.OK, reviews)
        );
    }
}