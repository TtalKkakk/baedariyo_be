package com.house.biet.store.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.application.dto.StoreReviewCreateRequestDto;
import com.house.biet.store.command.application.dto.StoreReviewCreateResponseDto;
import com.house.biet.store.command.domain.entity.StoreReview;
import com.house.biet.store.command.domain.vo.StoreReviewComment;
import com.house.biet.store.command.domain.vo.StoreReviewImages;
import com.house.biet.user.query.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreReviewFacadeTest {

    @Mock
    private StoreReviewService storeReviewService;

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private StoreRatingService storeRatingService;

    @InjectMocks
    private StoreReviewFacade storeReviewFacade;

    private UUID storeId;
    private UUID reviewId;
    private Long accountId;
    private Long userId;

    private StoreReviewCreateRequestDto requestDto;
    private StoreReview review;

    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        reviewId = UUID.randomUUID();
        accountId = 1L;
        userId = 100L;

        requestDto = new StoreReviewCreateRequestDto(
                10L,
                5,
                new StoreReviewImages(List.of("image1.jpg")),
                new StoreReviewComment("맛있어요")
        );

        review = StoreReview.create(
                storeId,
                userId,
                requestDto.orderId(),
                requestDto.rating(),
                requestDto.storeReviewImages(),
                requestDto.storeReviewComment()
        );
    }

    @Test
    @DisplayName("성공 - 리뷰 생성")
    void createReview_Success() {

        // given
        given(userQueryService.getUserIdByAccountId(accountId)).willReturn(userId);
        given(storeReviewService.save(any()))
                .willReturn(review);

        // when
        StoreReviewCreateResponseDto response =
                storeReviewFacade.createReview(requestDto, storeId, accountId);

        // then
        assertThat(response).isNotNull();
        verify(userQueryService).getUserIdByAccountId(accountId);
        verify(storeReviewService).save(any());
        verify(storeRatingService)
                .increaseRating(storeId, requestDto.rating());
    }

    @Test
    @DisplayName("성공 - 리뷰 삭제")
    void deleteReview_Success() {

        // given
        given(storeReviewService.findByPublicId(reviewId)).willReturn(review);
        given(userQueryService.getUserIdByAccountId(accountId)).willReturn(userId);

        // when
        storeReviewFacade.deleteReview(reviewId, accountId);

        // then
        verify(storeReviewService).deleteByPublicId(reviewId);
        verify(storeRatingService)
                .decreaseRating(review.getStorePublicId(), review.getRating());
    }

    @Test
    @DisplayName("실패 - 작성자 불일치")
    void deleteReview_Error_UserMismatch() {

        // given
        Long otherUserId = 999L;

        given(storeReviewService.findByPublicId(reviewId)).willReturn(review);
        given(userQueryService.getUserIdByAccountId(accountId)).willReturn(otherUserId);

        // when & then
        assertThatThrownBy(() ->
                storeReviewFacade.deleteReview(reviewId, accountId)
        ).isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.FORBIDDEN_STORE_REVIEW_MODIFICATION.getMessage());
    }
}