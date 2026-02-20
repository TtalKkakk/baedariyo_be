package com.house.biet.store.query.application;

import com.house.biet.store.query.StoreReviewQueryRepository;
import com.house.biet.store.query.dto.MyStoreReviewDto;
import com.house.biet.store.query.dto.StoreReviewDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StoreReviewQueryServiceTest {

    @Mock
    private StoreReviewQueryRepository storeReviewQueryRepository;

    @InjectMocks
    private StoreReviewQueryServiceImpl storeReviewQueryService;

    @Test
    @DisplayName("성공 - userId 기준 내가 작성한 리뷰 목록 조회")
    void findMyReviews_Success() {
        // Given
        Long userId = 100L;
        MyStoreReviewDto myStoreReviewDto = new MyStoreReviewDto(
                UUID.randomUUID(), UUID.randomUUID(), "testStoreName",
                5, null, List.of("image1"), "좋아요"
        );

        given(storeReviewQueryRepository.findMyReviews(userId))
                .willReturn(List.of(myStoreReviewDto));

        // When
        List<MyStoreReviewDto> results = storeReviewQueryService.findMyReviews(userId);

        // Then
        assertThat(results).hasSize(1).contains(myStoreReviewDto);
    }

    @Test
    @DisplayName("성공 - storePublicId 기준 가게 리뷰 목록 조회")
    void findReviewsByStore_Success() {
        // Given
        UUID storePublicId = UUID.randomUUID();
        StoreReviewDto storeReviewDto1 = new StoreReviewDto(
                UUID.randomUUID(), 1L, storePublicId, "testStoreName",
                5, null, List.of("image1"), "좋아요"
        );
        StoreReviewDto storeReviewDto2 = new StoreReviewDto(
                UUID.randomUUID(), 3L, storePublicId, "testStoreName",
                4, null, List.of("image2"), "좋아요22"
        );
        given(storeReviewQueryRepository.findReviewsByStore(storePublicId))
                .willReturn(List.of(storeReviewDto1, storeReviewDto2));

        // When
        List<StoreReviewDto> results = storeReviewQueryService.findReviewsByStore(storePublicId);

        // Then
        assertThat(results).hasSize(2).contains(storeReviewDto1);
    }
}