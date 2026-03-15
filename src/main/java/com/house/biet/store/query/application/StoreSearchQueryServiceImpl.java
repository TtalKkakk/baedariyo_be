package com.house.biet.store.query.application;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.global.route.application.RouteTimeService;
import com.house.biet.store.query.StoreSearchQueryRepository;
import com.house.biet.store.query.dto.StoreSearchQueryDto;
import com.house.biet.store.query.dto.StoreSearchResponseDto;
import com.house.biet.storeSearch.query.common.SearchKeywordNormalizer;
import com.house.biet.storeSearch.query.event.dto.StoreSearchEvent;
import com.house.biet.user.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreSearchQueryServiceImpl implements StoreSearchQueryService {

    private final StoreSearchQueryRepository storeSearchQueryRepository;
    private final SearchKeywordNormalizer normalizer;
    private final RouteTimeService routeTimeService;
    private final UserQueryService userQueryService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<StoreSearchResponseDto> searchStores(
            Long accountId,
            String keyword,
            StoreCategory storeCategory,
            double customerLatitude,
            double customerLongitude,
            int page,
            int size
    ) {
        int offset = page * size;

        keyword = normalizer.normalize(keyword);

        List<StoreSearchQueryDto> stores =
                storeSearchQueryRepository.searchStores(
                        keyword,
                        storeCategory,
                        customerLatitude,
                        customerLongitude,
                        offset,
                        size
                );

        Long userId = userQueryService.getUserIdByAccountId(accountId);
        eventPublisher.publishEvent(new StoreSearchEvent(userId, keyword, stores.size()));

        return stores.stream()
                .map(store -> {

                    int deliveryTime =
                            routeTimeService.calculateEstimatedDeliveryMinutes(
                                    store.storeLatitude(),
                                    store.storeLongitude(),
                                    customerLatitude,
                                    customerLongitude
                            );

                    return new StoreSearchResponseDto(
                            store.storeName(),
                            deliveryTime,
                            store.distance(),
                            store.minOrderPrice(),
                            store.rating(),
                            store.reviewCount()
                    );
                })
                .toList();
    }
}
