package com.house.biet.store.command.application;

import com.house.biet.fixtures.StoreReviewFixture;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.StoreReviewRepository;
import com.house.biet.store.command.domain.entity.StoreReview;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreReviewServiceTest {

    @Mock
    private StoreReviewRepository storeReviewRepository;

    @InjectMocks
    private StoreReviewService storeReviewService;

    @Test
    @DisplayName("성공 - StoreReview 저장")
    void save_Success() {
        // Given
        StoreReview review = StoreReviewFixture.create();
        given(storeReviewRepository.save(review)).willReturn(review);

        // When
        StoreReview saved = storeReviewService.save(review);

        // Then
        assertThat(saved).isEqualTo(review);
        verify(storeReviewRepository).save(review);
    }

    @Test
    @DisplayName("성공 - publicStoreReviewId 기준 리뷰 삭제")
    void deleteByPublicId_Success() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        storeReviewService.deleteByPublicId(id);

        // Then
        verify(storeReviewRepository).deleteByPublicId(id);
    }

    @Test
    @DisplayName("성공 - publicStoreReviewId 기준 리뷰 조회")
    void findByPublicId_Success() {
        // Given
        UUID id = UUID.randomUUID();
        StoreReview review = StoreReviewFixture.create();
        given(storeReviewRepository.findByPublicId(id)).willReturn(Optional.of(review));

        // When
        StoreReview result = storeReviewService.findByPublicId(id);

        // Then
        assertThat(result).isEqualTo(review);
    }

    @Test
    @DisplayName("실패 - publicStoreReviewId 기준 리뷰 조회 | 리뷰 없음")
    void findByPublicId_Error_NotFound() {
        // Given
        UUID id = UUID.randomUUID();
        given(storeReviewRepository.findByPublicId(id)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> storeReviewService.findByPublicId(id))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.STORE_REVIEW_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("성공 - 특정 가게 리뷰 목록 조회")
    void findByPublicStoreId_Success() {
        // Given
        UUID storeId = UUID.randomUUID();
        StoreReview review = StoreReviewFixture.create();
        given(storeReviewRepository.findByStorePublicId(storeId)).willReturn(List.of(review));

        // When
        List<StoreReview> results = storeReviewService.findByPublicStoreId(storeId);

        // Then
        assertThat(results).hasSize(1).contains(review);
    }

    @Test
    @DisplayName("성공 - 특정 사용자 리뷰 목록 조회")
    void findByUserId_Success() {
        // Given
        Long userId = 100L;
        StoreReview review = StoreReviewFixture.create();
        given(storeReviewRepository.findByUserId(userId)).willReturn(List.of(review));

        // When
        List<StoreReview> results = storeReviewService.findByUserId(userId);

        // Then
        assertThat(results).hasSize(1).contains(review);
    }


}