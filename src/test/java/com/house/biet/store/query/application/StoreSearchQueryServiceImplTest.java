package com.house.biet.store.query.application;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.global.route.application.RouteTimeService;
import com.house.biet.store.query.StoreSearchQueryRepository;
import com.house.biet.store.query.dto.StoreSearchQueryDto;
import com.house.biet.store.query.dto.StoreSearchResponseDto;
import com.house.biet.storeSearch.query.common.SearchKeywordNormalizer;
import com.house.biet.user.query.application.UserQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StoreSearchQueryServiceImplTest {

    @Mock
    private StoreSearchQueryRepository storeSearchQueryRepository;

    @Mock
    private RouteTimeService routeTimeService;

    @Mock
    private SearchKeywordNormalizer normalizer;

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private StoreSearchQueryServiceImpl storeSearchQueryService;

    @Test
    @DisplayName("성공 - 가게 검색 후 ResponseDto로 변환한다")
    void searchStores_Success_StoreSearch() {

        // Given
        String keyword = "치킨";
        StoreCategory category = StoreCategory.CHICKEN;

        double customerLatitude = 37.5;
        double customerLongitude = 127.0;

        int page = 0;
        int size = 10;

        Long userId = 1L;
        String storeName = "굽네치킨";
        UUID storePublicId = UUID.randomUUID();
        double storeLatitude = 37.6;
        double storeLongitude = 127.1;
        int minOrderPrice = 15000;
        double rating = 4.5;
        int reviewCount = 120;
        double distance = 1.2;

        int deliveryTime = 25;

        StoreSearchQueryDto store = new StoreSearchQueryDto(
                storePublicId,
                null,
                storeName,
                storeLatitude,
                storeLongitude,
                minOrderPrice,
                rating,
                reviewCount,
                distance
        );

        given(storeSearchQueryRepository.searchStores(
                keyword,
                category,
                customerLatitude,
                customerLongitude,
                0,
                size
        )).willReturn(List.of(store));

        given(normalizer.normalize(keyword)).willReturn(keyword);

        given(routeTimeService.calculateEstimatedDeliveryMinutes(
                storeLatitude,
                storeLongitude,
                customerLatitude,
                customerLongitude
        )).willReturn(deliveryTime);

        // When
        List<StoreSearchResponseDto> result =
                storeSearchQueryService.searchStores(
                        userId,
                        keyword,
                        category,
                        customerLatitude,
                        customerLongitude,
                        page,
                        size
                );

        // Then
        assertThat(result).hasSize(1);

        StoreSearchResponseDto response = result.get(0);

        assertThat(response.storeName()).isEqualTo(storeName);
        assertThat(response.deliveryTime()).isEqualTo(deliveryTime);
        assertThat(response.deliveryDistance()).isEqualTo(distance);
        assertThat(response.minOrderPrice()).isEqualTo(minOrderPrice);
        assertThat(response.rating()).isEqualTo(rating);
        assertThat(response.reviewCount()).isEqualTo(reviewCount);
    }
}