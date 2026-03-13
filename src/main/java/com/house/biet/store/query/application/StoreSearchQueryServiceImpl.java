package com.house.biet.store.query.application;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.global.route.application.RouteTimeService;
import com.house.biet.store.query.StoreSearchQueryRepository;
import com.house.biet.store.query.dto.StoreSearchQueryDto;
import com.house.biet.store.query.dto.StoreSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreSearchQueryServiceImpl implements StoreSearchQueryService {

    private final StoreSearchQueryRepository storeSearchQueryRepository;
    private final RouteTimeService routeTimeService;

    @Override
    public List<StoreSearchResponseDto> searchStores(
            String keyword,
            StoreCategory storeCategory,
            double customerLatitude,
            double customerLongitude,
            int page,
            int size)
    {
        int offset = page * size;

        List<StoreSearchQueryDto> stores =
                storeSearchQueryRepository.searchStores(
                        keyword,
                        storeCategory,
                        customerLatitude,
                        customerLongitude,
                        offset,
                        size
                );

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
